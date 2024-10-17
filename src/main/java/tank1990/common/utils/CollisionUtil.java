package tank1990.common.utils;

import java.util.ArrayList;

import tank1990.common.classes.AABB;
import tank1990.common.classes.CollisionBox;
import tank1990.common.classes.GameEntity;
import tank1990.common.classes.Vector2D;
import tank1990.common.constants.GameConstants;
import tank1990.common.enums.EntityType;

public class CollisionUtil {
  private static final int MAX_WIDTH = GameConstants.MAP_WIDTH + GameConstants.MAP_SHIFT_WIDTH;
  private static final int MAX_HEIGHT = GameConstants.MAP_HEIGHT + GameConstants.MAP_SHIFT_HEIGHT;

  public static int getTileIndex(Vector2D position) {
    int column = (int) (position.x / (GameConstants.ENTITY_WIDTH * 2));
    int row = (int) (position.y / (GameConstants.ENTITY_HEIGHT * 2));

    return column + row * (GameConstants.ENTITY_WIDTH * 2);
  }

  public static Vector2D getPositionByIndex(int index, int tileWidth, int tileHeight) {
    int column = (int) index % tileWidth;
    int row = (int) index / tileHeight;

    return new Vector2D(column * tileWidth, row * tileHeight);
  }

  public static Vector2D getOutOfBoundOffset(GameEntity gameEntity) {
    double dx1 = gameEntity.getCenter().x + gameEntity.width / 2 - MAX_WIDTH;
    double dx2 = GameConstants.MAP_SHIFT_WIDTH - (gameEntity.getCenter().x - gameEntity.width / 2);

    if (dx1 > 0 || dx2 > 0) {
      if (dx1 > 0) {
        return new Vector2D(-dx1, 0);
      }
      if (dx2 > 0) {
        return new Vector2D(dx2, 0);
      }
    }

    double dy1 = gameEntity.getCenter().y + gameEntity.height / 2 - MAX_HEIGHT;
    double dy2 = GameConstants.MAP_SHIFT_HEIGHT - (gameEntity.getCenter().y - gameEntity.height / 2);

    if (dy1 > 0 || dy2 > 0) {
      if (dy1 > 0) {
        return new Vector2D(0, -dy1);
      }
      if (dy2 > 0) {
        return new Vector2D(0, dy2);
      }
    }

    return Vector2D.zero();
  }

  public static Vector2D getCollisionOffset(GameEntity gameEntityA, GameEntity gameEntityB) {

    return null;
  }

  public static boolean isOutOfBound(GameEntity gameEntity) {
    return !getOutOfBoundOffset(gameEntity).isEqual(Vector2D.zero());
  }

  public static boolean isWorldPositionSolid(Vector2D position, GameEntity[] tileMap) {
    int column = (int) (position.x / (GameConstants.ENTITY_WIDTH * 2));
    int row = (int) (position.y / (GameConstants.ENTITY_HEIGHT * 2));

    if (column >= GameConstants.FRAME_WIDTH / (GameConstants.ENTITY_WIDTH * 2))
      return true;

    if (row >= GameConstants.FRAME_HEIGHT / (GameConstants.ENTITY_HEIGHT * 2))
      return true;

    int tileMapIndex = getTileIndex(position);

    boolean tileIsSolid = false;
    GameEntity gameEntity = tileMap[tileMapIndex];

    if (gameEntity != null) {
      tileIsSolid = true;
    }

    return tileIsSolid;
  }

  public static void checkEdgeCollision(GameEntity gameEntityA) {
    if (isOutOfBound(gameEntityA)) {
      Vector2D boundOffset = getOutOfBoundOffset(gameEntityA);
      gameEntityA.setPosition(gameEntityA.getPosition().add(boundOffset));
    }
  }

  public static boolean checkAABBCollision(
      GameEntity gameEntityA,
      GameEntity gameEntityB,
      double deltaTime) {

    if (gameEntityB.getCollision() == null) {
      return false;
    }

    AABB collBoxA_AABB = new AABB(gameEntityA.getCollision());
    AABB collBoxB_AABB = new AABB(gameEntityB.getCollision());
    AABB MinkowskiDiff = collBoxB_AABB.minkowskiDifference(collBoxA_AABB);

    if (MinkowskiDiff.min.x <= 0 &&
        MinkowskiDiff.max.x >= 0 &&
        MinkowskiDiff.min.y <= 0 &&
        MinkowskiDiff.max.y >= 0) {

      // Lazy calculation of offset
      double offsetX = Math.min(Math.abs(MinkowskiDiff.min.x), Math.abs(MinkowskiDiff.max.x));
      double offsetY = Math.min(Math.abs(MinkowskiDiff.min.y), Math.abs(MinkowskiDiff.max.y));

      if (!gameEntityA.getVelocity().isZero()) { // For rigid body (1 component has velocity)
        boolean hasCollision = false;

        // TODO: Fix this ASAP, need a case for no need to check front
        if (gameEntityA.getType() == EntityType.BULLET) {
          return true;
        }

        for (Vector2D collisionPoint : forwardCollisionPoints(gameEntityA,
            deltaTime)) {
          if (collBoxB_AABB.getRayIntersectionFraction(collisionPoint,
              gameEntityA.getVelocity()) < Double.POSITIVE_INFINITY) {
            hasCollision = true;
          }
        }

        if (hasCollision) {
          Vector2D offset = gameEntityA.getVelocity().normalized().multiply(-1);
          gameEntityA.setPosition(gameEntityA.getPosition().add(offset));
          gameEntityA.setVelocity(new Vector2D(0, 0));
        }
      } else { // For static body (2 components have zero velocity)
        Vector2D totalOffset = collBoxB_AABB.getOverlappingDirection(collBoxA_AABB,
            offsetX, offsetY);

        gameEntityB.setPosition(
            gameEntityB.getPosition().add(totalOffset));

        /*
         * Note: In some rare case, the offset will be messed up*
         */
      }

      if (!gameEntityB.getVelocity().isZero()) {
        boolean hasCollision = false;

        if (gameEntityB.getType() == EntityType.BULLET) {
          return true;
        }

        for (Vector2D collisionPoint : forwardCollisionPoints(gameEntityB,
            deltaTime)) {
          if (collBoxA_AABB.getRayIntersectionFraction(collisionPoint,
              gameEntityB.getVelocity()) < Double.POSITIVE_INFINITY) {
            hasCollision = true;
          }
        }

        if (hasCollision) {
          Vector2D offset = gameEntityB.getVelocity().normalized().multiply(-1);
          gameEntityB.setPosition(gameEntityB.getPosition().add(offset));
          gameEntityB.setVelocity(new Vector2D(0, 0));
        }
      } else {
        Vector2D totalOffset = collBoxA_AABB.getOverlappingDirection(collBoxB_AABB,
            offsetX, offsetY);

        gameEntityA.setPosition(
            gameEntityA.getPosition().add(totalOffset));
      }

      return true;
    } else {
      Vector2D relativeMotion = (gameEntityA.getVelocity().minus(gameEntityB.getVelocity())).multiply(deltaTime);

      Double h = MinkowskiDiff.getRayIntersectionFraction(Vector2D.zero(), relativeMotion);

      if (h < Double.POSITIVE_INFINITY) {
        Vector2D offsetA = gameEntityA.getVelocity().multiply(deltaTime).multiply(h);
        Vector2D offsetB = gameEntityB.getVelocity().multiply(deltaTime).multiply(h);
        gameEntityA.setPosition(gameEntityA.getPosition().add(offsetA));
        gameEntityB.setPosition(gameEntityB.getPosition().add(offsetB));

        return true;
      }
      return false;
    }
  }

  public static ArrayList<Vector2D> forwardCollisionPoints(GameEntity gameEntity, double deltaTime) {
    final int collisionPointsCount = 5;
    ArrayList<Vector2D> collisionPoints = new ArrayList<>();
    CollisionBox collisionBox = gameEntity.getCollision();

    if (collisionBox == null) {
      return new ArrayList<>();
    }

    Vector2D direction = gameEntity.getVelocity().normalized();
    Vector2D startingPoint = collisionBox.globalPosition;

    if (direction.x != 0 && !Double.isNaN(direction.x)) { // Moving horizontally
      Vector2D startingPointH = startingPoint;

      if (direction.x > 0) {
        startingPointH = startingPointH.add(new Vector2D(collisionBox.width, 0));
      }

      collisionPoints.add(startingPointH);
      for (int i = 1; i < collisionPointsCount; i++) {
        collisionPoints
            .add(startingPointH.add(new Vector2D(0, ((double) collisionBox.height / (collisionPointsCount - 1)) * i)));
      }
    }

    if (direction.y != 0 && !Double.isNaN(direction.y)) { // Moving vertically
      Vector2D startingPointV = startingPoint;

      if (direction.y > 0) {
        startingPointV = startingPointV.add(new Vector2D(0, collisionBox.height));
      }

      collisionPoints.add(startingPointV);

      for (int i = 1; i < collisionPointsCount; i++) {
        collisionPoints
            .add(startingPointV.add(new Vector2D(((double) collisionBox.width / (collisionPointsCount - 1)) * i, 0)));
      }
    }

    return collisionPoints;
  }
}
