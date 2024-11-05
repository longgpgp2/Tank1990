package tank1990.objects.tanks;

import java.awt.*;
import java.util.HashSet;

import tank1990.common.classes.CollisionBox;
import tank1990.common.classes.GameEntity;
import tank1990.common.classes.Vector2D;
import tank1990.common.enums.Direction;
import tank1990.common.enums.EntityType;
import tank1990.common.interfaces.DestructibleEntity;
import tank1990.main.GameState;
import tank1990.manager.SoundManager;
import tank1990.objects.animation.BulletExplosion;
import tank1990.objects.environments.Base;
import tank1990.objects.environments.BrickWall;

import javax.swing.*;

public class Bullet extends GameEntity {
    public double speed;
    public boolean isCollided = false;
    public boolean isFromPlayer;
    public Direction direction;
    private SoundManager shootingSound;

    private int damage = 1;
    private boolean destroyed = false;
    private double lastX, lastY;
    private Image image;
    private GameEntity source;
    private BulletExplosion bulletExplosion;
    private GameState gameState;

    public Bullet(int startX, int startY, Direction direction, double speed, GameEntity source) {
        super(EntityType.BULLET, new Vector2D(startX, startY), 5, 5);
        this.direction = direction;
        this.speed = speed;
        this.source = source;
        this.gameState = GameState.getInstance();
        this.shootingSound = new SoundManager();
        this.shootingSound.soundLoader(".\\src\\main\\resources\\sounds\\shoot-5-102360.wav");
        switch (direction) {
            case UP -> y -= 15;
            case DOWN -> y += 15;
            case LEFT -> x -= 15;
            case RIGHT -> x += 15;
        }

        setBulletImage();
        setCollision(new CollisionBox(this, new Vector2D(0, 0), 5, 5));
        collisionBox.setEnableFrontCollisionCheck(false);
        collisionBox.setEnableCollisionResponse(false);
    }

    private void setBulletImage() {
        switch (direction) {
            case LEFT -> image = new ImageIcon("src/main/resources/images/bullet_left.png").getImage();
            case RIGHT -> image = new ImageIcon("src/main/resources/images/bullet_right.png").getImage();
            case UP -> image = new ImageIcon("src/main/resources/images/bullet_up.png").getImage();
            case DOWN -> image = new ImageIcon("src/main/resources/images/bullet_down.png").getImage();
            case NONE -> {
                return;
            }
        }
    }

    public boolean isExplosionFinished() {
        return bulletExplosion != null && bulletExplosion.isFinished();
    }

    public void update(double deltaTime) {
        if (destroyed) {
            handleBulletExplosion();
            return;
        }

        HashSet<GameEntity> collidedGameEntities = checkCollision(deltaTime);

        if (collidedGameEntities != null) {
            damageEntities(collidedGameEntities);
        }

        if (checkCollision() || isOutOfBound()) {
            destroy();
        }

        move(deltaTime);
    }

    public void update() {
        if (!isOutOfBound()) {
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
                case NONE: {
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

    private void handleBulletExplosion() {
        if (bulletExplosion != null) {
            bulletExplosion.update(() -> {
                isCollided = true;
                bulletExplosion = null; // xóa animation
            }, new BulletExplosion.BulletExplosionImageCallback() {
                @Override
                public void updateImage(Image newImage) {
                    image = newImage; // animation
                }
            });
        }
    }

    /**
     * Kiểm tra xem bulletExplosion có nên được kích hoạt hay không. Nếu bắn trúng
     * enemy thì đạn k nổ check để remove khỏi list bullets
     */
    public void destroy() {
        destroyed = true;
        if (bulletExplosion == null) {
            isCollided = true;
            image = null;
        }
        collisionBox.setEnabled(false);
    }

    public void move(double deltaTime) {
        lastX = x;
        lastY = y;
        double distance = speed * deltaTime;
        switch (direction) {
            case LEFT -> x -= distance;
            case RIGHT -> x += distance;
            case UP -> y -= distance;
            case DOWN -> y += distance;
            case NONE -> {
                return;
            }
        }
        setPosition(new Vector2D(x, y));
    }

    public void draw(Graphics2D graphics2d) {
        if (!destroyed) {
            graphics2d.drawImage(image, (int) x, (int) y, null);
        } else if (bulletExplosion != null) {
            bulletExplosion.update(() -> {
                isCollided = true;
            }, new BulletExplosion.BulletExplosionImageCallback() {
                @Override
                public void updateImage(Image newImage) {
                    image = newImage;
                }
            });
            bulletExplosion.render(graphics2d);
        }
        if (gameState.isSoundOn()) {
            shootingSound.playSound();
        } else {
            shootingSound.stopSound();
        }

    }

    public boolean isOutOfBound() {
        if (x < 0 || y < 0 || x > 600 || y > 600) {
            return true;
        }
        return false;
    }

    public boolean isCollided() {
        return isCollided;
    }

    private void damageEntities(HashSet<GameEntity> collidedGameEntities) {
        boolean enemyDestroyed = false;

        for (GameEntity collidedGameEntity : collidedGameEntities) {
            if (collidedGameEntity.equals(source)) {
                continue;
            }

            if (collidedGameEntity instanceof BrickWall) {
                ((BrickWall) collidedGameEntity).hitComponent(this);
            }

            if (collidedGameEntity instanceof EnemyTank) {
                EnemyTank enemyTank = (EnemyTank) collidedGameEntity;
                if (enemyTank.isAppearing()) {
                    continue;
                }

                if (enemyTank.getHealth() > 0) {
                    enemyTank.setHealth(enemyTank.getHealth() - damage);
                    this.destroy();
                }

                if (enemyTank.getHealth() <= 0) {
                    enemyDestroyed = true;
                    enemyTank.destroy();
                    this.destroy();
                }
            }

            if (collidedGameEntity instanceof PlayerTank) {
                PlayerTank playerTank = (PlayerTank) collidedGameEntity;

                if (playerTank.getLives() <= 0) {
                    playerTank.destroy();
                }

                if (playerTank.getLives() > 0) {
                    playerTank.setLives(playerTank.getLives() - damage);
                    playerTank.respawn();
                    this.destroy();
                }
            }
            if (collidedGameEntity instanceof Base) {
                ((Base) collidedGameEntity).hit(damage);
                this.destroy();
                return;
            }
            if (collidedGameEntity instanceof DestructibleEntity) {
                ((DestructibleEntity) collidedGameEntity).hit(damage);
            }

            if (!enemyDestroyed) {
                bulletExplosion = new BulletExplosion((int) lastX, (int) lastY);
            }

            destroy();

        }
    }
}
