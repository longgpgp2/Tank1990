package tank1990.common.constants;

import tank1990.common.classes.Vector2D;
import tank1990.common.enums.EntityType;

public class GameConstants {
        public static final int FRAME_WIDTH = 1000;
        public static final int FRAME_HEIGHT = 800;
        public static final int NUMBER_OF_BLOCK = 33 * 33;
        public static final int MAP_WIDTH = 16 * 33;
        public static final int MAP_HEIGHT = 16 * 33;
        public static final int ENTITY_WIDTH = 16;
        public static final int ENTITY_HEIGHT = 16;
        public static final int TANK_SIZE = 32;
        public static final int PLAYER_SPAWNING_INDEX = 1002;
        public static final int ENEMY_SPAWNING_INDEX = 63;
        public static final Vector2D TANK_DISABLED_POSITION = new Vector2D(-100, -100);
        public static final int POWERUP_SIZE = 32;
        public static final int MAP_SHIFT_WIDTH = 50;
        public static final int MAP_SHIFT_HEIGHT = 50;
        public static final int FPS = 60;
        public static final double deltaTime = 1 / FPS;
        public static final String[] PERK_LIST = { "Star", "Grenade", "Helmet", "Tank", "Timer", "Shovel" };
        public static final EntityType[] IMPASSABLE_ENTITIES = {
                        EntityType.PLAYER, EntityType.BASE, EntityType.EDGE,
                        EntityType.BRICK, EntityType.RIVER, EntityType.STEEL, EntityType.BASE_WALL, EntityType.BORDER
        };
        public static final EntityType[] PLAYER_IMPASSABLE_ENTITIES = {
                        EntityType.ENEMY, EntityType.BASE, EntityType.EDGE,
                        EntityType.BRICK, EntityType.RIVER, EntityType.STEEL, EntityType.BASE_WALL, EntityType.POWER_UP,
                        EntityType.BORDER
        };
        public static final EntityType[] BULLET_IMPASSABLE_ENTITIES = {
                        EntityType.BRICK,
                        EntityType.STEEL,
                        EntityType.ENEMY,
                        EntityType.BASE,
                        EntityType.BASE_WALL,
                        EntityType.PLAYER,
                        EntityType.BORDER,
                        EntityType.BULLET
        };

}
