package tank1990.manager;

import java.util.ArrayList;
import java.util.Iterator;

import tank1990.common.classes.GameEntity;
import tank1990.common.enums.EntityType;

public class GameEntityManager {
  private static ArrayList<GameEntity> gameEntities = new ArrayList<>();
  private static ArrayList<GameEntity> playerCollisionComponents = new ArrayList<>();
  private static ArrayList<GameEntity> bulletCollisionComponents = new ArrayList<>();
  private static ArrayList<GameEntity> enemyCollisionComponents = new ArrayList<>();

  public void GameEntity() {
  }

  /*
   * This stupid thing might cause an error
   */
  public static void remove(GameEntity gameComponent) {
    if (gameComponent == null) {
      return;
    }

    for (Iterator<GameEntity> iterator = gameEntities.iterator(); iterator.hasNext();) {
      GameEntity value = iterator.next();
      if (value == gameComponent) {
        iterator.remove();
      }
    }
  }

  public static void add(GameEntity gameComponent) {
    if (gameComponent != null) {
      gameEntities.add(gameComponent);
    }
  }

  public static ArrayList<GameEntity> getGameEntitys() {
    return gameEntities;
  }

  public static GameEntity[] getGameEntity(EntityType type) {
    return gameEntities
        .stream()
        .filter(gameComponent -> gameComponent.getType().equals(type))
        .toArray(GameEntity[]::new);
  }

  public static void setPlayerCollisionComponents(EntityType[] gameComponentTypes) {
    for (EntityType gameComponentType : gameComponentTypes) {
      for (GameEntity gameComponent : GameEntityManager.getGameEntitys()) {
        if (gameComponent.getType() == gameComponentType) {
          playerCollisionComponents.add(gameComponent);
        }
      }
    }
  }

  public static ArrayList<GameEntity> getPlayerCollisionComponents() {
    return playerCollisionComponents;
  }

  public static void setBulletCollisionComponents(EntityType[] gameComponentTypes) {
    for (EntityType gameComponentType : gameComponentTypes) {
      for (GameEntity gameComponent : GameEntityManager.getGameEntitys()) {
        if (gameComponent.getType() == gameComponentType) {
          bulletCollisionComponents.add(gameComponent);
        }
      }
    }
  }

  public static ArrayList<GameEntity> getBulletCollisionComponents() {
    return bulletCollisionComponents;
  }

  public static void setEnemyCollisionComponents(EntityType[] gameComponentTypes) {
    for (EntityType gameComponentType : gameComponentTypes) {
      for (GameEntity gameComponent : GameEntityManager.getGameEntitys()) {
        if (gameComponent.getType() == gameComponentType) {
          enemyCollisionComponents.add(gameComponent);
        }
      }
    }
  }

  public static ArrayList<GameEntity> getEnemyCollisionComponents() {
    return enemyCollisionComponents;
  }
}
