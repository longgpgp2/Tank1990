package tank1990.objects.tanks;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.HashSet;

import tank1990.common.classes.CollisionBox;
import tank1990.common.classes.GameEntity;
import tank1990.common.classes.GameSprite;
import tank1990.common.classes.Vector2D;
import tank1990.common.enums.Direction;
import tank1990.common.enums.EntityType;
import tank1990.common.interfaces.DestructibleEntity;
import tank1990.main.GameInfoPanel;
import tank1990.manager.MapManager;
import tank1990.objects.animation.BulletExplosion;
import tank1990.objects.environments.Base;
import tank1990.objects.environments.BaseWall;
import tank1990.objects.environments.BrickWall;

public class Bullet extends GameEntity {
    public static final Vector2D DEFAULT_POSITION = new Vector2D(-360, -360);
    public boolean isFromPlayer;
    public boolean destroyed = false;

    private int damage = 1;
    private double speed;
    private double lastX, lastY;
    private Image image;
    private Direction direction = Direction.DOWN;
    private GameEntity source;
    private BulletExplosion bulletExplosion;

    private GameSprite leftSprite = new GameSprite("src/main/resources/images/bullet_left.png");
    private GameSprite rightSprite = new GameSprite("src/main/resources/images/bullet_right.png");
    private GameSprite upSprite = new GameSprite("src/main/resources/images/bullet_up.png");
    private GameSprite downSprite = new GameSprite("src/main/resources/images/bullet_down.png");

    public Bullet(int startX, int startY, GameEntity source) {
        super(EntityType.BULLET, new Vector2D(startX, startY), 5, 5);
        this.source = source;

        setBulletImage();
        setCollision(new CollisionBox(this, new Vector2D(0, 0), 5, 5));
        collisionBox.setEnabled(false);
        collisionBox.setEnableFrontCollisionCheck(false);
        collisionBox.setEnableCollisionResponse(false);
    }

    public void setBulletImage() {
        switch (direction) {
            case LEFT -> image = leftSprite.getBufferedImage();
            case RIGHT -> image = rightSprite.getBufferedImage();
            case UP -> image = upSprite.getBufferedImage();
            case DOWN -> image = downSprite.getBufferedImage();
            case NONE -> {
                return;
            }
        }
    }

    public void update(double deltaTime) {
        HashSet<GameEntity> collidedGameEntities = checkCollision(deltaTime);

        if (collidedGameEntities != null) {
            damageEntities(collidedGameEntities);
        }

        if (isOutOfBound() && collisionBox.isEnabled()) {
            destroy();
        }

        move(deltaTime);
    }

    private void handleBulletExplosion() {
        if (bulletExplosion != null) {
            bulletExplosion.update(() -> {
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
        image = null;
        destroyed = true;
        collisionBox.setEnabled(false);
        speed = 0;
        velocity = new Vector2D(0, 0);
        position = DEFAULT_POSITION;
        handleBulletExplosion();
    }

    public void move(double deltaTime) {
        lastX = x;
        lastY = y;

        setVelocity(direction.getValue().multiply(speed));
        setBulletImage();
        setPosition(position.add(velocity.multiply(deltaTime)));
    }

    public void draw(Graphics2D graphics2d) {
        if (!destroyed) {
            graphics2d.drawImage(image, (int) x, (int) y, null);
        }

        if (destroyed && bulletExplosion != null) {
            bulletExplosion.update(() -> {
            }, new BulletExplosion.BulletExplosionImageCallback() {
                @Override
                public void updateImage(Image newImage) {
                    image = newImage;
                }
            });
            bulletExplosion.render(graphics2d);
        }
    }

    public boolean isOutOfBound() {
        if (x < 0 || y < 0 || x > 600 || y > 600) {
            return true;
        }
        return false;
    }

    private void damageEntities(HashSet<GameEntity> collidedGameEntities) {
        boolean enemyDestroyed = false;

        for (GameEntity collidedGameEntity : collidedGameEntities) {
            if (collidedGameEntity.getCollision() == null) {
                continue;
            }

            if (collidedGameEntity.equals(source)) {
                continue;
            }

            if (collidedGameEntity instanceof BrickWall) {
                ((BrickWall) collidedGameEntity).hitComponent(this);
            }

            if (collidedGameEntity instanceof BaseWall) {
                ((BaseWall) collidedGameEntity).hitComponent(this);
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
                    if (source instanceof PlayerTank) {
                        PlayerTank playerTank = MapManager.getPlayerTank();
                        playerTank.setPoint(playerTank.getPoint() + enemyTank.getPoint());
                        GameInfoPanel.getInstance().updatePoint();
                        GameInfoPanel.getInstance().removeEnemyIcon();
                    }
                    bulletExplosion = new BulletExplosion((int) lastX, (int) lastY);
                    this.destroy();
                }
            }

            if (collidedGameEntity instanceof PlayerTank) {
                PlayerTank playerTank = (PlayerTank) collidedGameEntity;
                if (playerTank.isAppearing()) {
                    continue;
                }

                if (playerTank.isShielded()) {
                    this.destroy();
                    continue;
                }

                if (playerTank.getHealth() <= 0) {
                    playerTank.destroy();
                }

                if (playerTank.getHealth() > 0) {
                    playerTank.setHealth(playerTank.getHealth() - damage);
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

            this.destroy();
        }
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public boolean isFromPlayer() {
        return isFromPlayer;
    }

    public void setFromPlayer(boolean fromPlayer) {
        isFromPlayer = fromPlayer;
    }
}
