package tank1990.objects.tanks;

import java.awt.Color;
import java.awt.Graphics;

import tank1990.common.classes.GameEntity;
import tank1990.common.classes.Vector2D;
import tank1990.common.enums.Direction;
import tank1990.common.enums.EntityType;

public class Bullet extends GameEntity {
    public double x, y;
    public Direction direction;
    public double speed;
    public boolean isCollided = false;

    public Bullet(int startX, int startY, Direction direction, double speed) {
        super(EntityType.BULLET, new Vector2D(startX, startY), 5, 5);
        this.x = startX + 10;
        this.y = startY + 10;
        this.direction = direction;
        switch (direction) {
            case UP -> y -= 15;
            case DOWN -> y += 15;
            case LEFT -> x -= 15;
            case RIGHT -> x += 15;
        }

        this.speed = speed;
    }

    public void update() {
        if (!checkBulletOutOfBound()) {
            double directionValue = 0;
            switch (direction) {
                case UP: {
                    directionValue = 3 * Math.PI / 2;
                    break;
                }
                case DOWN: {
                    directionValue = Math.PI / 2;
                    break;
                }
                case LEFT: {
                    directionValue = Math.PI;
                    break;
                }
                case RIGHT: {
                    directionValue = 0;
                    break;
                }

            }
            x += speed * Math.cos(directionValue);
            y += speed * Math.sin(directionValue);
        }
    }

    public void draw(Graphics g) {
        if (!checkBulletOutOfBound()) {
            g.setColor(Color.RED);
            if (direction == Direction.DOWN || direction == Direction.UP)
                g.fillRect((int) x, (int) y, 3, 10);
            else
                g.fillRect((int) x, (int) y, 10, 3);
        }
    }

    public boolean checkBulletOutOfBound() {
        if (x < 0 || y < 0 || x > 700 || y > 700)
            return true;
        return false;
    }

}
