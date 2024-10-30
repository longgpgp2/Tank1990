package tank1990.common.classes;

public class CollisionBox {
    public double x;
    public double y;
    public int width;
    public int height;
    public Vector2D center;
    public Vector2D extents;
    public Vector2D relativePosition; // relative to game component position
    public Vector2D globalPosition;
    public GameEntity gameEntity;

    private boolean enabled = true;
    private boolean enableFrontCollisionCheck = true;
    private boolean enableCollisionResponse = true;

    public CollisionBox(GameEntity gameEntity, Vector2D relativePosition, int width, int height) {
        x = relativePosition.add(gameEntity.getPosition()).x;
        y = relativePosition.add(gameEntity.getPosition()).y;

        this.width = width;
        this.height = height;

        this.center = new Vector2D(x + width / 2, y + height / 2);
        this.extents = new Vector2D(width / 2, height / 2);
        this.relativePosition = relativePosition;
        this.globalPosition = gameEntity.getPosition().add(relativePosition);
        this.gameEntity = gameEntity;
    }

    public Vector2D getCenter() {
        return this.center;
    }

    public void setPosition(Vector2D gameEntityPosition) {
        globalPosition = gameEntityPosition.add(relativePosition);
        this.x = globalPosition.x;
        this.y = globalPosition.y;
        this.center = new Vector2D(x + (double) width / 2, y + (double) height / 2);
    }

    public void setEnableFrontCollisionCheck(boolean enable) {
        enableFrontCollisionCheck = enable;
    }

    public boolean isFrontCollisionChecked() {
        return enableFrontCollisionCheck;
    }

    public void setEnabled(boolean enable) {
        this.enabled = enable;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnableCollisionResponse(boolean enable) {
        enableCollisionResponse = enable;
    }

    public boolean isCollisionResponseEnabled() {
        return enableCollisionResponse;
    }

    public AABB getAABB() {
        return new AABB(this);
    }

    public String toString() {
        return "CollisionBox(x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + ")";
    }
}
