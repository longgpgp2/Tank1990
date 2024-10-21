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

  /**
   * Check for simple AABB and swept AABB colision.
   * 
   * Ref:
   * https://blog.hamaluik.ca/posts/swept-aabb-collision-using-minkowski-difference/
   * https://developer.mozilla.org/en-US/docs/Games/Techniques/3D_collision_detection
   * 
   * Note:
   * - Workable but some bugs may still occur
   * 
   * @param gameComponentA - a GameEntity.
   * @param gameComponentB - a GameEntity.
   * @param deltaTime      - time between frames.
   * @return true if AABB collision happened and false otherwise.
   */
  public static boolean checkAABBCollision(
      GameEntity gameComponentA,
      GameEntity gameComponentB,
      double deltaTime) {

    if (gameComponentA.getCollision() == null ||
        gameComponentB.getCollision() == null) {
      return false;
    }

    AABB collBoxA_AABB = gameComponentA.getCollision().getAABB();
    AABB collBoxB_AABB = gameComponentB.getCollision().getAABB();

    AABB MinkowskiDiff = collBoxB_AABB.minkowskiDifference(collBoxA_AABB);

    if (MinkowskiDiff.min.x <= 0 &&
        MinkowskiDiff.max.x >= 0 &&
        MinkowskiDiff.min.y <= 0 &&
        MinkowskiDiff.max.y >= 0) {
      checkStaticCollision(gameComponentA, gameComponentB, deltaTime);
      if (!gameComponentA.getVelocity().isZero()) {
        checkRigidCollision(gameComponentA, gameComponentB, deltaTime);
      }

      if (!gameComponentB.getVelocity().isZero()) {
        checkRigidCollision(gameComponentB, gameComponentA, deltaTime);
      }
      return true;
    } else {
      Vector2D relativeMotion = (gameComponentA.getVelocity().minus(gameComponentB.getVelocity())).multiply(deltaTime);

      Double h = MinkowskiDiff.getRayIntersectionFraction(Vector2D.zero(), relativeMotion);

      if (h < Double.POSITIVE_INFINITY) {
        Vector2D offsetA = gameComponentA.getVelocity().multiply(deltaTime).multiply(h);
        Vector2D offsetB = gameComponentB.getVelocity().multiply(deltaTime).multiply(h);
        gameComponentA.setPosition(gameComponentA.getPosition().add(offsetA));
        gameComponentB.setPosition(gameComponentB.getPosition().add(offsetB));

        return true;
      }
      return false;
    }
  }

  /**
   * Check the collision of a moving GameEntity by detecting which component
   * is in front of it and change its position based on its velocity.
   * 
   * @param source    - a GameEntity which is used as origin to detect
   *                  collision with other GameEntity(s).
   * @param target    - a GameEntity which is checked by source GameEntity
   *                  for collision.
   * @param deltaTime - time between frames.
   * @return true if rigid collision happened and false otherwise.
   */
  private static boolean checkRigidCollision(GameEntity source, GameEntity target, double deltaTime) {
    boolean hasCollision = false;

    if (!source.getCollision().isFrontCollisionChecked()) {
      return true;
    }

    // Check every single point to see if GameEntity has collided in the front
    for (Vector2D collisionPoint : forwardCollisionPoints(source, 5, deltaTime)) {
      AABB targetAABB = target.getCollision().getAABB();
      double pointToAABBFraction = targetAABB.getRayIntersectionFraction(collisionPoint, source.getVelocity());

      if (pointToAABBFraction < Double.POSITIVE_INFINITY) {
        hasCollision = true;
      }
    }

    // Adjust collision based on velocity
    if (target.getCollision().isEnabled() && hasCollision) {
      Vector2D offset = source.getVelocity().normalized().multiply(-1);
      source.setPosition(source.getPosition().add(offset));
    }
    return hasCollision;
  }

  /**
   * Check the collision of a static GameEntity by calculating the overlapping
   * area between 2 AABB(s) and change source GameEntity by the offset.
   * 
   * @param source - a GameEntity which is used as origin to detect
   *               collision with other GameEntity(s) and then be offset.
   * @param target - a GameEntity which is checked by source GameEntity
   *               for collision.
   * @return true if static collision happened and false otherwise.
   */
  private static boolean checkStaticCollision(GameEntity source, GameEntity target, double deltaTime) {
    AABB sourceAABB = source.getCollision().getAABB();
    AABB targetAABB = target.getCollision().getAABB();

    AABB MinkowskiDiff = targetAABB.minkowskiDifference(sourceAABB);

    // Calculation the offset
    double offsetX = Math.min(Math.abs(MinkowskiDiff.min.x), Math.abs(MinkowskiDiff.max.x));
    double offsetY = Math.min(Math.abs(MinkowskiDiff.min.y), Math.abs(MinkowskiDiff.max.y));

    // Apply direction to offset
    Vector2D totalOffset = sourceAABB.getOverlappingOffsetDirection(targetAABB, offsetX, offsetY);

    source.setPosition(source.getPosition().add(totalOffset));

    return true;
  }

  /**
   * Generate collision points which are placed in front of a moving GameEntity
   * for collision detection.
   * 
   * @param gameComponent        - a GameEntity
   * @param collisionPointsCount - number of collision points for detection, more
   *                             points equal more computation
   * @param deltaTime            - time between frames
   * @return
   */
  private static ArrayList<Vector2D> forwardCollisionPoints(
      GameEntity gameComponent,
      int collisionPointsCount,
      double deltaTime) {
    ArrayList<Vector2D> collisionPoints = new ArrayList<>();
    CollisionBox collisionBox = gameComponent.getCollision();

    if (collisionBox == null) {
      return new ArrayList<>();
    }

    Vector2D direction = gameComponent.getVelocity().normalized();
    Vector2D startingPoint = collisionBox.globalPosition;

    // GameEntity is moving horizontally
    if (direction.x != 0 && !Double.isNaN(direction.x)) {
      Vector2D startingPointH = startingPoint;

      if (direction.x > 0) {
        startingPointH = startingPointH.add(new Vector2D(collisionBox.width, 0));
      }

      collisionPoints.add(startingPointH);

      for (int i = 1; i < collisionPointsCount; i++) {
        double collisionPointY = ((double) collisionBox.height / (collisionPointsCount - 1)) * i;
        Vector2D collisionPointPosition = new Vector2D(0, collisionPointY);

        collisionPoints.add(startingPointH.add(collisionPointPosition));
      }
    }

    // GameEntity is moving vertically
    if (direction.y != 0 && !Double.isNaN(direction.y)) {
      Vector2D startingPointV = startingPoint;

      if (direction.y > 0) {
        startingPointV = startingPointV.add(new Vector2D(0, collisionBox.height));
      }

      collisionPoints.add(startingPointV);

      for (int i = 1; i < collisionPointsCount; i++) {
        double collisionPointX = ((double) collisionBox.width / (collisionPointsCount - 1)) * i;
        Vector2D collisionPointPosition = new Vector2D(collisionPointX, 0);

        collisionPoints.add(startingPointV.add(collisionPointPosition));
      }
    }

    return collisionPoints;
  }
}
