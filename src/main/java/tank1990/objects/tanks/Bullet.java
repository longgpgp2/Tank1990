package tank1990.objects.tanks;

import java.awt.*;
import java.awt.image.BufferedImage;

import tank1990.common.classes.CollisionBox;
import tank1990.common.classes.GameEntity;
import tank1990.common.classes.Vector2D;
import tank1990.common.enums.Direction;
import tank1990.common.enums.EntityType;

import javax.swing.*;

public class Bullet extends GameEntity {
    public double x, y;
    public Direction direction;
    public double speed;
    public boolean isCollided = false;
    private Image image;

    public Bullet(int startX, int startY, Direction direction, double speed) {
        super(EntityType.BULLET, new Vector2D(startX, startY), 5, 5);
        this.x = startX;
        this.y = startY;
        this.direction = direction;
        switch (direction) {
            case UP -> y -= 15;
            case DOWN -> y += 15;
            case LEFT -> x -= 15;
            case RIGHT -> x += 15;
        }
        setBulletImage();
        this.speed = speed;
    }
    private void setBulletImage() {
        // Chọn hình ảnh viên đạn dựa trên hướng
        switch (direction) {
            case LEFT -> image = new ImageIcon("src/main/resources/images/bullet_left.png").getImage();
            case RIGHT -> image = new ImageIcon("src/main/resources/images/bullet_right.png").getImage();
            case UP -> image = new ImageIcon("src/main/resources/images/bullet_up.png").getImage();
            case DOWN -> image = new ImageIcon("src/main/resources/images/bullet_down.png").getImage();
        }
    }
    public void update(double deltaTime) {
        switch (direction) {
            case LEFT -> x -= speed * deltaTime;
            case RIGHT -> x += speed * deltaTime;
            case UP -> y -= speed * deltaTime;
            case DOWN -> y += speed * deltaTime;
        }
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
    public void move() {
        switch (direction) {
            case LEFT -> x -= speed;
            case RIGHT -> x += speed;
            case UP -> y -= speed;
            case DOWN -> y += speed;
        }
    }
    public void draw(Graphics g) {
        if (!isCollided) { // Chỉ vẽ nếu viên đạn chưa va chạm
            g.drawImage(image, (int) x, (int) y, null);
        }
    }

    public CollisionBox getCollisionBox() {
        return new CollisionBox(this, new Vector2D(x, y), image.getWidth(null), image.getHeight(null));
    }
    public boolean checkBulletOutOfBound() {
        if (x < 0 || y < 0 || x > 700 || y > 700)
            return true;
        return false;
    }
    public boolean isOutOfBound() {
        return checkBulletOutOfBound();
    }

    public boolean isCollided() {
        return isCollided;
    }

}
