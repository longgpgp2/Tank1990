package tank1990.objects.tanks;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import tank1990.common.enums.Direction;

public class PlayerTank extends Tank {
    private int owner;
    private Direction direction;
    private List<Bullet> bullets = new ArrayList<>();

    public PlayerTank(int owner) {
        super(3, 1, 1, Direction.UP);
        this.owner = owner;
        this.setColor(Color.YELLOW);
    }

    @Override
    public Bullet shoot() {
        Bullet bullet = new Bullet(getX(), getY(), getDirection(), 10);
        bullets.add(bullet);
        System.out.println("Bullet fired from: (" + getX() + ", " + getY() + ") with direction: " + getDirection());
        return bullet;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "PlayerTank{" +
                "owner=" + owner +
                ", health=" + this.getHealth() +
                ", bulletSpeed=" + this.getBulletSpeed() +
                ", movementSpeed=" + this.getMovementSpeed() +
                ", color='" + this.getColor() + '\'' +
                '}';
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public void setBullets(List<Bullet> bullets) {
        this.bullets = bullets;
    }

}
