package tank1990.objects.tanks;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import tank1990.common.classes.CollisionBox;
import tank1990.common.classes.Vector2D;
import tank1990.common.constants.GameConstants;
import tank1990.common.enums.Direction;
import tank1990.common.enums.EntityType;
import tank1990.manager.GameEntityManager;
import tank1990.manager.KeyHandler;

public class PlayerTank extends Tank {
    private static int counter = 0;
    private int owner;
    public List<Bullet> bullets = new ArrayList<>();
    public long lastShotTime = 0;
    public long shotDelay = 300; // delay 0.3s

    public int maxBullets;
    public int velocity = 5;
    KeyHandler keyHandler;

    public PlayerTank(int owner, int maxBullets) {
        super(EntityType.PLAYER, 1, 1, 1, Direction.UP);
        this.owner = owner;
        this.setColor(Color.YELLOW);
        image = new ImageIcon("src/main/resources/images/tank_player1_up_c0_t1.png").getImage();
        setCollision(
                new CollisionBox(this, new Vector2D(2.5, 2.5), GameConstants.TANK_SIZE - 5,
                        GameConstants.TANK_SIZE - 5));
        this.maxBullets = maxBullets;
        keyHandler = new KeyHandler(this);
    }

    @Override
    public Bullet shoot() {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastShotTime < shotDelay || bullets.size() >= maxBullets) {
            return null;
        }
        int bulletX = (int) getPosition().x;
        int bulletY = (int) getPosition().y;

        switch (getDirection()) {
            case LEFT:
                bulletX -= -20;
                bulletY -= -12;
                break;
            case RIGHT:
                bulletX += 8;
                bulletY -= -12;
                break;
            case UP:
                bulletY -= -15;
                bulletX += 12;
                break;
            case DOWN:
                bulletY += 10;
                bulletX += 12;
                break;
        }

        Bullet bullet = new Bullet(bulletX, bulletY, getDirection(), 5);
        bullets.add(bullet);
        System.out.println("Bullet fired from: (" + bulletX + ", " + bulletY + ") with direction: " + getDirection());

        lastShotTime = currentTime;
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

    // @Override
    // public void update(double deltaTime){
    // ArrayList collidedEntities =
    // checkCollision(GameEntityManager.getPlayerCollisionComponents(), deltaTime);
    //// if(collidedEntities!=null)
    //// System.out.println(collidedEntities);
    //
    // }
    @Override
    public void update(double deltaTime) {
        // counter++;
        // System.out.print(" "+counter);
        keyHandler.updatePosition();
        // vị trí đạn
        for (Bullet bullet : bullets) {
            bullet.move();
            // va cham
        }
        ArrayList collidedEntities = checkCollision(GameEntityManager.getPlayerCollisionComponents(), deltaTime);
        // System.out.println(GameEntityManager.getPlayerCollisionComponents());
        if (collidedEntities != null) {
            for (Object e : collidedEntities) {
                System.out.println(e);
            }
            velocity = 0;
        } else
            velocity = 5;

        bullets.removeIf(bullet -> bullet.checkBulletOutOfBound() || bullet.isCollided());
    }

    public void keyPressed(KeyEvent e) {
        keyHandler.keyPressed(e);
        // int key = e.getKeyCode();
        // if (key == KeyEvent.VK_A) {
        //
        // setPosition(position.add(new Vector2D(-velocity,0)));
        // ImageIcon ii = null;
        // if (spriteNum == 1) {
        // ii = new ImageIcon("src/main/resources/images/tank_player1_left_c0_t1.png");
        // }
        // if (spriteNum == 2) {
        // ii = new ImageIcon("src/main/resources/images/tank_player1_left_c0_t2.png");
        // }
        // image = ii.getImage();
        // direction = Direction.LEFT;
        // } if (key == KeyEvent.VK_D) {
        //
        // setPosition(position.add(new Vector2D(+velocity,0)));
        // ImageIcon ii = null;
        // if (spriteNum == 1) {
        // ii = new ImageIcon("src/main/resources/images/tank_player1_right_c0_t1.png");
        // }
        // if (spriteNum == 2) {
        // ii = new ImageIcon("src/main/resources/images/tank_player1_right_c0_t2.png");
        // }
        // image = ii.getImage();
        // direction = Direction.RIGHT;
        // } if (key == KeyEvent.VK_W) {
        //
        // setPosition(position.add(new Vector2D(0,-velocity)));
        // ImageIcon ii = null;
        // if (spriteNum == 1) {
        // ii = new ImageIcon("src/main/resources/images/tank_player1_up_c0_t1.png");
        // }
        // if (spriteNum == 2) {
        // ii = new ImageIcon("src/main/resources/images/tank_player1_up_c0_t2.png");
        // }
        // image = ii.getImage();
        // direction = Direction.UP;
        // } if (key == KeyEvent.VK_S) {
        //
        // setPosition(position.add(new Vector2D(0,+velocity)));
        // ImageIcon ii = null;
        // if (spriteNum == 1) {
        // ii = new ImageIcon("src/main/resources/images/tank_player1_down_c0_t1.png");
        // }
        // if (spriteNum == 2) {
        // ii = new ImageIcon("src/main/resources/images/tank_player1_down_c0_t1.png");
        // }
        // image = ii.getImage();
        // direction = Direction.DOWN;
        // } if (key == KeyEvent.VK_SPACE) { // Bắn khi nhấn phím SPACE
        // shoot();
        // }
        // spriteCounter++;
        // if (spriteCounter > 6) { // animation speed
        // if (spriteNum == 1) {
        // spriteNum = 2;
        // } else if (spriteNum == 2) {
        // spriteNum = 1;
        // }
        // spriteCounter = 0;
        // }

    }

    public void keyReleased(KeyEvent e) {

        keyHandler.keyReleased(e);
    }

}