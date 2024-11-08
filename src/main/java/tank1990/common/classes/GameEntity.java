package tank1990.common.classes;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;

import tank1990.common.constants.GameConstants;
import tank1990.common.enums.EntityType;
import tank1990.common.utils.CollisionUtil;
import tank1990.manager.GameEntityManager;

import javax.swing.*;
//import tank1990.objects.animation.Game;

/**
 * Class cơ bản nhất cho game, extends thằng Component để có thể dùng cho Java
 * swing
 */
public abstract class GameEntity extends Component {
    public double x;
    public double y;
    public int width;
    public int height;

    public Image image;
    public int spriteNum = 1;
    public int spriteCounter = 0;

    protected EntityType type;
    protected Vector2D velocity;
    protected Vector2D center;
    protected Vector2D position;

    protected CollisionBox collisionBox;
    protected GameSprite sprite;

    public GameEntity(EntityType type, Vector2D position, int width, int height) {
        this.x = (int) position.x;
        this.y = (int) position.y;
        this.width = width;
        this.height = height;

        this.type = type;
        this.velocity = new Vector2D(0, 0);
        this.center = new Vector2D(x + width / 2, y + height / 2);
        this.position = new Vector2D(x, y);

        GameEntityManager.add(this);
    }

    /**
     * Vẽ game entity có thể tùy từng game entity có thể vẽ là sprite hoặc shape cơ
     * bản
     * 
     * @param graphics2d graphics của JComponent trong Java Swing
     */
    public void draw(Graphics2D graphics2d) {
    };

    public void draw(Graphics2D graphics2d, double deltaTime) {
    };

    /**
     * Cập nhật physic cho game entity qua từng deltaTime, khoảng thời gian giữa các
     * frame hoặc giữa các tick
     * 
     * @param deltaTime
     */
    public void update(double deltaTime) {
    };

    public void destroy() {
    };

    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
        this.x = position.x;
        this.y = position.y;

        setCenter(new Vector2D(position.x + width / 2, position.y + height / 2));

        if (collisionBox != null) {
            collisionBox.setPosition(position);
        }
    }

    public Vector2D getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2D velocity) {
        this.velocity = velocity;
    }

    public Vector2D getCenter() {
        return center;
    }

    public void setCenter(Vector2D center) {
        this.center = center;
    }

    public CollisionBox getCollision() {
        return this.collisionBox;
    }

    public void setCollision(CollisionBox collisionBox) {
        this.collisionBox = collisionBox;
    }

    public GameSprite getSprite() {
        return this.sprite;
    }

    public void setSprite(GameSprite sprite) {
        this.sprite = sprite;
    }

    public EntityType getType() {
        return type;
    }

    public int getX() {
        return (int) this.x;
    }

    public void setX(int x) {
        this.x = x;
        this.position.x = x;
    }

    public int getY() {
        return (int) y;
    }

    public void setY(int y) {
        this.y = y;
        this.position.y = y;
    }

    public String toString() {
        return "GameEntity(type=" + type + ", Position=" + getPosition() + ", width=" + width + ", height=" + height
                + ")";
    }

    public boolean equals(GameEntity gameEntity) {
        if (gameEntity == this) {
            return true;
        }

        if (!(gameEntity instanceof GameEntity)) {
            return false;
        }

        return this.getType() == gameEntity.getType();
    }

    /**
     * Get a set of GameEntity that collided with this GameEntity.
     * HashSet is used to prevent GameEntity duplication
     *
     * @param gameEntities list of player's collidable GameEntity
     * @param deltaTime    time between frame
     * @return a HashSet that contains all GameEntity that this GameEntity has
     *         collided with
     */
    public HashSet<GameEntity> checkCollision(double deltaTime) {
        ArrayList<GameEntity> collisionEntities = GameEntityManager.getCollisionEntities(type);

        if (!getCollision().isEnabled()) {
            return null;
        }
        // CollisionUtil.checkEdgeCollision(this);

        HashSet<GameEntity> collidedGameEntities = new HashSet<>();

        for (GameEntity gameEntity : collisionEntities) {
            if (gameEntity.getCollision() == null ||
                    !gameEntity.getCollision().isEnabled() ||
                    gameEntity == this) {
                continue;
            }

            boolean hasCollision = CollisionUtil.checkAABBCollision(this, gameEntity, deltaTime);

            if (hasCollision && !collidedGameEntities.contains(gameEntity)) {
                collidedGameEntities.add(gameEntity);
            }
        }

        if (collidedGameEntities.size() > 0) {
            return collidedGameEntities;
        }

        return null;
    }
}
