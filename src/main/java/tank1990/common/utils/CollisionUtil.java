package tank1990.common.utils;

import java.util.ArrayList;

import tank1990.common.classes.AABB;
import tank1990.common.classes.CollisionBox;
import tank1990.common.classes.GameEntity;
import tank1990.common.classes.Vector2D;
import tank1990.common.constants.GameConstants;
import tank1990.common.enums.CollisionType;
import tank1990.common.enums.EntityType;
import tank1990.manager.GameEntityManager;

public class CollisionUtil {
  private static final int MAX_WIDTH = GameConstants.MAP_WIDTH + GameConstants.MAP_SHIFT_WIDTH;
  private static final int MAX_HEIGHT = GameConstants.MAP_HEIGHT + GameConstants.MAP_SHIFT_HEIGHT;

  public static void addCollisionToObjects(){
    GameEntityManager.setCollisionEntities(EntityType.ENEMY, GameConstants.IMPASSABLE_ENTITIES);
    GameEntityManager.setCollisionEntities(EntityType.PLAYER, GameConstants.PLAYER_IMPASSABLE_ENTITIES);
    GameEntityManager.setCollisionEntities(EntityType.BULLET, new EntityType[] {
            EntityType.BRICK,
            EntityType.STEEL,
            EntityType.ENEMY, EntityType.BASE,  EntityType.BASE_WALL,
    });
  }

  public static int getTileIndex(Vector2D position) {
    int column = (int) (position.x / (GameConstants.ENTITY_WIDTH));
    int row = (int) (position.y / (GameConstants.ENTITY_HEIGHT));

    return row + column + row * (GameConstants.ENTITY_WIDTH * 2);
  }

  public static Vector2D getPositionByIndex(int index, int tileWidth, int tileHeight) {
    int x = (index % 33) * (tileWidth);
    int y = (index / 33) * (tileHeight);
    // int column = (int) index % tileWidth;
    // int row = (int) index / tileHeight;

    // return new Vector2D(column * tileWidth, row * tileHeight);
    return new Vector2D(x, y);
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

  public static boolean checkIntersection(AABB firstAABB, AABB secondAABB) {
    AABB MinkowskiDiff = secondAABB.minkowskiDifference(firstAABB);

    if (MinkowskiDiff.min.x <= 0 &&
        MinkowskiDiff.max.x >= 0 &&
        MinkowskiDiff.min.y <= 0 &&
        MinkowskiDiff.max.y >= 0) {
      return true;
    } else {
      return false;
    }
  }

  public static boolean checkIntersection(AABB MinkowskiDiff) {
    if (MinkowskiDiff.min.x <= 0 &&
        MinkowskiDiff.max.x >= 0 &&
        MinkowskiDiff.min.y <= 0 &&
        MinkowskiDiff.max.y >= 0) {
      return true;
    } else {
      return false;
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
   * @param gameEntityA - a GameEntity.
   * @param gameEntityB - a GameEntity.
   * @param deltaTime   - time between frames.
   * @return true if AABB collision happened and false otherwise.
   */
  public static boolean checkAABBCollision(
      GameEntity gameEntityA,
      GameEntity gameEntityB,
      double deltaTime) {

    if (gameEntityA.getCollision() == null ||
        gameEntityB.getCollision() == null) {
      return false;
    }

    AABB collBoxA_AABB = gameEntityA.getCollision().getAABB();
    AABB collBoxB_AABB = gameEntityB.getCollision().getAABB();

    AABB MinkowskiDiff = collBoxB_AABB.minkowskiDifference(collBoxA_AABB);

    if (checkIntersection(MinkowskiDiff)) {
      return simpleAABBReponse(gameEntityA, gameEntityB, deltaTime);
    } else {
      return sweptAABBResponse(gameEntityA, gameEntityB, MinkowskiDiff, deltaTime);
    }
  }

  public static boolean simpleAABBReponse(
      GameEntity gameEntityA,
      GameEntity gameEntityB,
      double deltaTime) {
    if (gameEntityA.getCollision().isCollisionResponseEnabled() == false ||
        gameEntityB.getCollision().isCollisionResponseEnabled() == false) {
      return true;
    }

    checkStaticCollision(gameEntityA, gameEntityB, deltaTime);

    if (!gameEntityB.getVelocity().isZero()) {
      checkRigidCollision(gameEntityB, gameEntityA, deltaTime);
    }

    if (!gameEntityA.getVelocity().isZero()) {
      checkRigidCollision(gameEntityA, gameEntityB, deltaTime);
    }

    return true;
  }

  public static boolean sweptAABBResponse(
      GameEntity gameEntityA,
      GameEntity gameEntityB,
      AABB MinkowskiDiff,
      double deltaTime) {
    Vector2D relativeMotion = (gameEntityA.getVelocity().minus(gameEntityB.getVelocity())).multiply(deltaTime);

    Double h = MinkowskiDiff.getRayIntersectionFraction(Vector2D.zero(), relativeMotion);

    if (h < Double.POSITIVE_INFINITY) {
      if (gameEntityA.getCollision().isCollisionResponseEnabled() == false ||
          gameEntityB.getCollision().isCollisionResponseEnabled() == false) {
        return true;
      }

      Vector2D offsetA = gameEntityA.getVelocity().multiply(deltaTime).multiply(h);
      Vector2D offsetB = gameEntityB.getVelocity().multiply(deltaTime).multiply(h);

      gameEntityA.setPosition(gameEntityA.getPosition().add(offsetA));
      gameEntityB.setPosition(gameEntityB.getPosition().add(offsetB));
    }
    return false;
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
      source.setVelocity(new Vector2D(0, 0));
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

    AABB sourceMinkowskiDiff = targetAABB.minkowskiDifference(sourceAABB);

    // Calculation the offset
    double sourceOffsetX = Math.min(Math.abs(sourceMinkowskiDiff.min.x), Math.abs(sourceMinkowskiDiff.max.x));
    double sourceOffsetY = Math.min(Math.abs(sourceMinkowskiDiff.min.y), Math.abs(sourceMinkowskiDiff.max.y));

    // Apply direction to offset
    Vector2D sourceOffset = sourceAABB.getOverlappingOffsetDirection(targetAABB, sourceOffsetX, sourceOffsetY);

    if (source.getCollision().getCollisionType() == CollisionType.RIGID) {
      source.setPosition(source.getPosition().add(sourceOffset.multiply(deltaTime)));
    }

    if (target.getCollision().getCollisionType() == CollisionType.RIGID) {
      target.setPosition(target.getPosition().add(sourceOffset.multiply(-1).multiply(deltaTime)));
    }

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
