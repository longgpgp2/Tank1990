package tank1990.common.constants;

import tank1990.common.enums.EntityType;

import java.util.Arrays;
import java.util.Set;

public class GameConstants {
    public static final int FRAME_WIDTH = 1000;
    public static final int FRAME_HEIGHT = 800;
    public static final int NUMBER_OF_BLOCK = 33*33;
    public static final int MAP_WIDTH = 16 * 33;
    public static final int MAP_HEIGHT = 16 * 33;
    public static final int ENTITY_WIDTH = 16;
    public static final int ENTITY_HEIGHT = 16;
    public static final int TANK_SIZE = 32;
    public static final int POWERUP_SIZE = 32;
    public static final int MAP_SHIFT_WIDTH = 50;
    public static final int MAP_SHIFT_HEIGHT = 50;
    public static final int FPS = 60;
    public static final double deltaTime = 1/FPS;
    public static final String[] PERK_LIST = {"Star", "Grenade", "Helmet", "Tank", "Timer", "Shovel"};
    public static final EntityType[] IMPASSABLE_ENTITIES = {EntityType.BASE, EntityType.EDGE, EntityType.BRICK, EntityType.RIVER, EntityType.STEEL};
    public static final EntityType[] PLAYER_IMPASSABLE_ENTITIES = {EntityType.ENEMY, EntityType.BASE, EntityType.EDGE, EntityType.BRICK, EntityType.RIVER, EntityType.STEEL, EntityType.POWER_UP};

}
