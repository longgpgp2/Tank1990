package tank1990.objects.tanks;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import tank1990.common.classes.CollisionBox;
import tank1990.common.classes.Vector2D;
import tank1990.common.constants.GameConstants;
import tank1990.common.enums.Direction;
import tank1990.common.enums.EntityType;
import tank1990.manager.GameEntityManager;

import javax.swing.*;

public class PlayerTank extends Tank {
    private int owner;
    private Direction direction;
    private List<Bullet> bullets = new ArrayList<>();



    public PlayerTank(int owner) {
        super(EntityType.PLAYER, 1, 1, 1, Direction.UP);
        this.owner = owner;
        this.setColor(Color.YELLOW);
        image = new ImageIcon("src/main/resources/images/tank_player1_up_c0_t1.png").getImage();
        setCollision(new CollisionBox(this, new Vector2D(0,0), GameConstants.TANK_SIZE-2,GameConstants.TANK_SIZE-2));
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

    @Override
    public void update(double deltaTime){
        ArrayList collidedEntities = checkCollision(GameEntityManager.getPlayerCollisionComponents(), deltaTime);
//        if(collidedEntities!=null)
//        System.out.println(collidedEntities);
    }

    public void keyPressed(KeyEvent e) {
        int velocity = 5;
        int time;
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A) {
                x-=velocity;
                position.x -=velocity;
                ImageIcon ii = new ImageIcon("src/main/resources/images/tank_player1_left_c0_t1.png");
                image = ii.getImage();
                direction = Direction.LEFT;
        } else if (key == KeyEvent.VK_D) {
                x+=velocity;
            position.x +=velocity;
                ImageIcon ii = new ImageIcon("src/main/resources/images/tank_player1_right_c0_t1.png");
                image = ii.getImage();
                direction = Direction.RIGHT;
        } else if (key == KeyEvent.VK_W) {
            y-=velocity;
            position.y -=velocity;
            ImageIcon ii = new ImageIcon("src/main/resources/images/tank_player1_up_c0_t1.png");
            image = ii.getImage();
            direction = Direction.UP;
        } else if (key == KeyEvent.VK_S) {
            y+=velocity;
            position.y +=velocity;
            ImageIcon ii = new ImageIcon("src/main/resources/images/tank_player1_down_c0_t1.png");
            image = ii.getImage();
            direction = Direction.DOWN;
        }
    }

    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_A) {
        }

        if (key == KeyEvent.VK_D) {
        }

        if (key == KeyEvent.VK_W) {
        }

        if (key == KeyEvent.VK_S) {
        }
    }
}
