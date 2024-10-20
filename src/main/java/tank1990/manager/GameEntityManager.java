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

  public static ArrayList<GameEntity> getGameEntities() {
    return gameEntities;
  }

  public static GameEntity[] getGameEntity(EntityType type) {
    return gameEntities
        .stream()
        .filter(gameComponent -> gameComponent.getType().equals(type))
        .toArray(GameEntity[]::new);
  }

  // TODO: make this function more generic
  /**
   * Chọn type để player va chạm với các entity thuộc type đó
   * 
   * @param gameComponentTypes
   */
  public static void setPlayerCollisionComponents(EntityType[] gameComponentTypes) {
    System.out.println(getGameEntities());
    for (EntityType gameComponentType : gameComponentTypes) {

      for (GameEntity gameComponent : GameEntityManager.getGameEntities()) {
//        System.out.println(gameComponentType);
//        System.out.println(gameComponent);
        if (gameComponent.getType() == gameComponentType) {
          playerCollisionComponents.add(gameComponent);

        }
      }
    }
  }

  /**
   * Lấy các entities mà player va chạm
   * 
   * @return
   */
  public static ArrayList<GameEntity> getPlayerCollisionComponents() {
    return playerCollisionComponents;
  }

  public static void setBulletCollisionComponents(EntityType[] gameComponentTypes) {
    for (EntityType gameComponentType : gameComponentTypes) {
      for (GameEntity gameComponent : GameEntityManager.getGameEntities()) {
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
      for (GameEntity gameComponent : GameEntityManager.getGameEntities()) {
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
