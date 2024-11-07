package tank1990.manager;

import java.util.*;

import tank1990.common.classes.GameEntity;
import tank1990.common.enums.EntityType;
import tank1990.objects.tanks.PlayerTank;
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
      if (value == gameEntity) {
        iterator.remove();
      }
    }
  }

  public static void add(GameEntity gameEntity) {
    if (gameEntity != null) {
      gameEntities.add(gameEntity);
    }
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

  public static List<Tank> getTanks(){
    List<Tank> tanks = new ArrayList<>();
    Arrays.asList(GameEntityManager.getGameEntity(EntityType.PLAYER))
            .stream()
            .forEach(gameEntity -> tanks.add((Tank) gameEntity));
    Arrays.asList(GameEntityManager.getGameEntity(EntityType.ENEMY))
            .stream()
            .forEach(gameEntity -> tanks.add((Tank) gameEntity));
    return tanks;
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
