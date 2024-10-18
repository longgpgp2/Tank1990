package tank1990.common.classes;

import java.awt.*;
import java.util.ArrayList;

import tank1990.common.enums.EntityType;
import tank1990.common.utils.CollisionUtil;
import tank1990.manager.GameEntityManager;

import javax.swing.*;

/**
 * Class cơ bản nhất cho game, extends thằng Component để có thể dùng cho Java
 * swing
 */
public abstract class GameEntity extends Component {
    public double x;
    public double y;
    public int width;
    public int height;

    public int spriteCounter = 0;
    public int spriteNum = 1;

    protected EntityType type;
    protected Vector2D velocity;
    protected Vector2D center;
    protected Vector2D position;

    protected CollisionBox collisionBox;
    public Image image;
    // protected GameSprite sprite;

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

    // public GameSprite getSprite() {
    // return this.sprite;
    // }

    // public void setSprite(GameSprite sprite) {
    // this.sprite = sprite;
    // }

    public EntityType getType() {
        return type;
    }

    public String toString() {
        return "GameComponent(type=" + type + ", Position=" + getPosition() + ", width=" + width + ", height=" + height
                + ")";
    }

    public ArrayList<GameEntity> checkCollision(ArrayList<GameEntity> gameComponents, double deltaTime) {
        if (!getCollision().enabled) {
            return null;
        }
        CollisionUtil.checkEdgeCollision(this);

        ArrayList<GameEntity> collidedGameComponents = new ArrayList<>();

        for (GameEntity gameComponent : gameComponents) {
            if (gameComponent.getCollision() == null ||
                    !gameComponent.getCollision().enabled ||
                    gameComponent == this) {
                continue;
            }

            boolean hasCollision = CollisionUtil.checkAABBCollision(this, gameComponent, deltaTime);

            if (hasCollision) {
                collidedGameComponents.add(gameComponent);
            }
        }

        if (collidedGameComponents.size() > 0) {
            return collidedGameComponents;
        }

        return null;
    }
}
