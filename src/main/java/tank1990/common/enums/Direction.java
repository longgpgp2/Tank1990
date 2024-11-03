package tank1990.common.enums;

import tank1990.common.classes.Vector2D;

public enum Direction {
    UP(new Vector2D(0, -1)),
    DOWN(new Vector2D(0, 1)),
    LEFT(new Vector2D(-1, 0)),
    RIGHT(new Vector2D(1, 0)),
    NONE(new Vector2D(0, 0));

    private static final Direction[] directions = values();
    private final Vector2D value;

    private Direction(Vector2D value) {
        this.value = value;
    }

    public Vector2D getValue() {
        return value;
    }

    public static Direction getEnum(Vector2D vector2d) {
        if (vector2d.x == 0 && vector2d.y < 0) {
            return UP;
        }
        if (vector2d.x == 0 && vector2d.y > 0) {
            return UP;
        }
        if (vector2d.x < 0 && vector2d.y == 0) {
            return LEFT;
        }
        if (vector2d.x > 0 && vector2d.y == 0) {
            return RIGHT;
        }
        return NONE;
    }

    public Vector2D getVector() {
        switch (this) {
            case UP:
                return new Vector2D(0, -1);
            case DOWN:
                return new Vector2D(0, 1);
            case LEFT:
                return new Vector2D(-1, 0);
            case RIGHT:
                return new Vector2D(1, 0);
            default:
                return new Vector2D(0, 0);
        }
    }

    public Direction getOpposite() {
        switch (this) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            default:
                return this;
        }
    }

    public Direction next(boolean inReverse) {
        if (inReverse) {
            return directions[(this.ordinal() - 1) % directions.length];
        }

        return directions[(this.ordinal() + 1) % directions.length];
    }

    public Direction next() {
        return directions[(this.ordinal() + 1) % directions.length];
    }
}
