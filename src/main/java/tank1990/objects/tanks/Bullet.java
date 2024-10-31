package tank1990.objects.tanks;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;

import tank1990.common.classes.CollisionBox;
import tank1990.common.classes.GameEntity;
import tank1990.common.classes.Vector2D;
import tank1990.common.enums.Direction;
import tank1990.common.enums.EntityType;
import tank1990.common.interfaces.DestructibleEntity;
import tank1990.manager.GameEntityManager;
import tank1990.manager.animation.BulletExplosion;
import tank1990.objects.environments.BaseWall;
import tank1990.objects.environments.BrickWall;

import javax.swing.*;

public class Bullet extends GameEntity {
    public double x, y;
    public Direction direction;
    public double speed;
    public boolean isCollided = false;
    private Image image;
    private int damage = 1;

    private boolean destroyed = false;
    private BulletExplosion bulletExplosion;
    private double lastX, lastY;

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

        setCollision(new CollisionBox(this, new Vector2D(0, 0), 5, 5));
    }

    private void setBulletImage() {
        switch (direction) {
            case LEFT -> image = new ImageIcon("src/main/resources/images/bullet_left.png").getImage();
            case RIGHT -> image = new ImageIcon("src/main/resources/images/bullet_right.png").getImage();
            case UP -> image = new ImageIcon("src/main/resources/images/bullet_up.png").getImage();
            case DOWN -> image = new ImageIcon("src/main/resources/images/bullet_down.png").getImage();
        }
    }

    public boolean isExplosionFinished() {
        return bulletExplosion != null && bulletExplosion.isFinished();
    }

    public void update(double deltaTime) {
        ArrayList<GameEntity> collisionEntities = GameEntityManager.getCollisionEntities(type);
        HashSet<GameEntity> collidedGameEntities = checkCollision(collisionEntities, deltaTime);

        if (collidedGameEntities != null && collidedGameEntities.size() > 0) {
            damageComponents(collidedGameEntities);
            destroyBullet();
        }

        if (!destroyed) {
            move();
            if (checkCollision() || checkBulletOutOfBound()) {
                destroyBullet();
            }
        } else if (bulletExplosion != null) {
            bulletExplosion.update();
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

    private boolean checkCollision() {
        return isCollided;
    }

    public void destroyBullet() {
        destroyed = true;
        bulletExplosion = new BulletExplosion((int) lastX, (int) lastY); // Create explosion at last position
        GameEntityManager.remove(this);
    }

    public void move() {
        lastX = x;
        lastY = y;

        switch (direction) {
            case LEFT -> x -= speed;
            case RIGHT -> x += speed;
            case UP -> y -= speed;
            case DOWN -> y += speed;
        }

        setPosition(new Vector2D(x, y));
    }

    public void draw(Graphics g) {
        if (!destroyed) {
            g.drawImage(image, (int) x, (int) y, null);
        } else if (bulletExplosion != null) {
            bulletExplosion.update();
            bulletExplosion.render(g);
            if (bulletExplosion.isFinished()) {
                bulletExplosion = null; //
            }
        }
    }

    public CollisionBox getCollisionBox() {
        return new CollisionBox(this, new Vector2D(x, y), image.getWidth(null), image.getHeight(null));
    }

    public boolean checkBulletOutOfBound() {
        if (x < 0 || y < 0 || x > 500 || y > 500)
            return true;
        return false;
    }

    public boolean isOutOfBound() {
        return checkBulletOutOfBound();
    }

    public boolean isCollided() {
        return isCollided;
    }

    private void damageComponents(HashSet<GameEntity> collidedGameEntities) {
        for (GameEntity collidedGameEntity : collidedGameEntities) {
            // special case. Could generalize by using health component but lazy
            if (collidedGameEntity instanceof BrickWall) {
                ((BrickWall) collidedGameEntity).hitComponent(this);
            }

            if (collidedGameEntity instanceof DestructibleEntity) {
                ((DestructibleEntity) collidedGameEntity).hit(damage);
            }

//            if (i == collidedGameEntities.size() - 1) {
//                destroy();
//            }
        }

        destroy();
    }
}
