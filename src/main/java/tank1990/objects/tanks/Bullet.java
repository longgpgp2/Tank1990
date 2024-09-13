package tank1990.objects.tanks;

import java.awt.Color;
import java.awt.Graphics;

public class Bullet {
    private double x, y;
    private Direction direction;
    private double speed;
    private boolean isCollided=false;

    public Bullet(double startX, double startY, Direction direction, double speed) {
        this.x = startX+10;
        this.y = startY+10;
        this.direction = direction;
        this.speed = speed;
    }

    public void update() {
        if(!checkBulletOutOfBound())
        {
            double directionValue = 0;
            switch (direction) {
                case Direction.UP: {
                    directionValue = 3 * Math.PI / 2;
                    break;
                }
                case Direction.DOWN: {
                    directionValue = Math.PI / 2;
                    break;
                }
                case Direction.LEFT: {
                    directionValue = Math.PI;
                    break;
                }
                case Direction.RIGHT: {
                    directionValue = 0;
                    break;
                }

            }
            x += speed * Math.cos(directionValue);
            y += speed * Math.sin(directionValue);
        }
    }

    public void draw(Graphics g) {
        if(!checkBulletOutOfBound())
        {
            g.setColor(Color.RED);
            if (direction == Direction.DOWN || direction == Direction.UP)
                g.fillRect((int) x, (int) y, 5, 10);
            else
                g.fillRect((int) x, (int) y, 10, 5);
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    public boolean checkBulletOutOfBound(){
        if(x<0 || y<0 ||x>700|| y>700)
        return true;
        return false;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public boolean isCollided() {
        return isCollided;
    }

    public void setCollided(boolean collided) {
        isCollided = collided;
    }
}
