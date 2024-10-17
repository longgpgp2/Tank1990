package tank1990.objects.common;

public abstract class Entity {
    int x;
    int y;
    int width;
    int height;
    CollisionBox collisionBox;

    public Entity(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        collisionBox = new CollisionBox(x, y, width, height);
    }
}
