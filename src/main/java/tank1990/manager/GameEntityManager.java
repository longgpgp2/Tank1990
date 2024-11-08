package tank1990.manager;

import java.util.*;

import tank1990.common.classes.GameEntity;
import tank1990.common.enums.EntityType;
import tank1990.objects.tanks.Tank;

public class GameEntityManager {
  private static ArrayList<GameEntity> gameEntities = new ArrayList<>();
  private static HashMap<EntityType, ArrayList<GameEntity>> gameCollisionEntites = new HashMap<>();

  public void GameEntity() {
  }

  /*
   * This stupid thing might cause an error
   */
  public static void remove(GameEntity gameEntity) {
    if (gameEntity == null) {
      return;
    }

    for (Iterator<GameEntity> iterator = gameEntities.iterator(); iterator.hasNext();) {
      GameEntity value = iterator.next();
      if (value != gameEntity) {
        continue;
      }

      if (value.getCollision() != null) {
        value.setCollision(null);
      }

      if (value.image != null) {
        value.image = null;
      }

      iterator.remove();
    }
  }

  public static void removeAll() {
    for (Iterator<GameEntity> iterator = gameEntities.iterator(); iterator.hasNext();) {
      GameEntity gameEntity = iterator.next();

      if (gameEntity.getCollision() != null) {
        gameEntity.setCollision(null);
      }

      if (gameEntity.image != null) {
        gameEntity.image = null;
      }

      iterator.remove();
    }
  }

  public static void removeAllExcept(EntityType[] entityTypes) {
    for (Iterator<GameEntity> iterator = gameEntities.iterator(); iterator.hasNext();) {
      GameEntity gameEntity = iterator.next();

      if (Arrays.asList(entityTypes).contains(gameEntity.getType())) {
        continue;
      }

      if (gameEntity.getCollision() != null) {
        gameEntity.setCollision(null);
      }

      if (gameEntity.image != null) {
        gameEntity.image = null;
      }

      iterator.remove();
    }
  }

  public static void add(GameEntity gameEntity) {
    if (gameEntity != null) {
      gameEntities.add(gameEntity);
    }
  }

  public static void removeCollisionEntity(EntityType type, GameEntity gameEntity) {
    if (gameEntity == null) {
      return;
    }

    gameCollisionEntites.get(type).remove(gameEntity);
  }

  public static void addCollisionEntity(EntityType type, GameEntity gameEntity) {
    if (gameEntity == null) {
      return;
    }

    gameCollisionEntites.get(type).add(gameEntity);
  }

  public static ArrayList<GameEntity> getGameEntities() {
    return gameEntities;
  }

  public static GameEntity[] getGameEntity(EntityType type) {
    return gameEntities
        .stream()
        .filter(gameEntity -> gameEntity.getType().equals(type))
        .toArray(GameEntity[]::new);
  }

  public static void setCollisionEntities(
      EntityType type,
      EntityType[] gameEntityTypes) {
    if (gameCollisionEntites.get(type) == null) {
      gameCollisionEntites.put(type, new ArrayList<GameEntity>());
    }

    for (EntityType gameEntityType : gameEntityTypes) {
      for (GameEntity gameEntity : gameEntities) {
        if (gameEntity.getType() == gameEntityType) {
          gameCollisionEntites.get(type).add(gameEntity);
        }
      }
    }
  }

  public static ArrayList<GameEntity> getCollisionEntities(EntityType type) {
    return gameCollisionEntites.get(type);
  }
}
