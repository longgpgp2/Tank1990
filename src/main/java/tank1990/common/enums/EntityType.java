package tank1990.common.enums;

public enum EntityType {
  BRICK(1),
  STEEL(2),
  BASE(3),
  RIVER(4),
  TREE(5),
  EDGE(6),
  BASE_WALL(7),
  PLAYER(8),
  BULLET(9),
  PLAYER_SPAWNER(10),
  ENEMY(11),
  POWER_UP(12),
  BORDER(13);

  private final int value;

  private EntityType(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public static EntityType getTypeFromInt(int value) {
    return EntityType.values()[value];
  }
}
