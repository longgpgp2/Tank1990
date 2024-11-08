package tank1990.objects.tanks;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashSet;

import javax.swing.ImageIcon;

import tank1990.common.classes.CollisionBox;
import tank1990.common.classes.GameEntity;
import tank1990.common.classes.Vector2D;
import tank1990.common.constants.GameConstants;
import tank1990.common.enums.CollisionType;
import tank1990.common.enums.Direction;
import tank1990.common.enums.EntityType;
import tank1990.common.utils.CollisionUtil;
import tank1990.main.GameObject;
import tank1990.main.GamePanel;
import tank1990.manager.GameEntityManager;
import tank1990.manager.KeyHandler;
import tank1990.manager.PowerUpManager;
import tank1990.objects.animation.Appear;
import tank1990.objects.animation.Shield;
import tank1990.manager.spawner.TankSpawner;
import tank1990.objects.powerups.PowerUp;

public class PlayerTank extends Tank {
    private static PlayerTank instance;

    private int owner;
    public long lastShotTime = 0;
    public long shotDelay = 300;

    public int maxBullets;

    public int speed = 80;
    // public boolean isShield = false;
    // public boolean isAppear = true;

    public KeyHandler keyHandler;

    private int star = 1;
    // private int lives = 3;
    private int point = 0;
    // private Appear appear;
    // private Shield shield;

    // (adjustable) How long before the tank can attack.
    private double attackInterval = 0;
    private double attackIntervalTimer = 0.5;

    public PlayerTank(int tankOwner) {
        super(EntityType.PLAYER, 1, 150, 1, 1, Direction.UP);

        owner = tankOwner;
        appear = new Appear(100);
        image = appear.getCurrentFrame().getImage();
        keyHandler = new KeyHandler(this);

        setColor(Color.YELLOW);

        CollisionBox collisionBox = new CollisionBox(this,
                new Vector2D(4, 4),
                GameConstants.TANK_SIZE - 8,
                GameConstants.TANK_SIZE - 8);
        setCollision(collisionBox);
        collisionBox.setEnableFrontCollisionCheck(true);
        collisionBox.setCollisionType(CollisionType.RIGID);

        initalizeBullets();

        startAnimation();
    }
    public static PlayerTank getInstance(int tankOwner) {
        if (instance == null) {
            instance = new PlayerTank(tankOwner);
        }
        return instance;
    }
    public void startAnimation() {
        new Thread(() -> {
            while (!appear.isAnimationFinished()) {
                image = appear.getCurrentFrame().getImage();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            isAppear = false;
            image = new ImageIcon("src/main/resources/images/tank_player1_up_c0_t1.png").getImage(); // Hiển thị hình
                                                                                                     // ảnh tank
            startShield();
        }).start();
    }

    public void respawn() {
        TankSpawner.respawnPlayer();
        startShield();
    }

    public void destroy() {
        // GameEntityManager.remove(this);
        this.collisionBox.setEnabled(false);
        this.image = null;
    }

    public void shoot() {
        if (this.image == null) {
            return;
        }

        if (attackIntervalTimer > attackInterval) {
            createBullet(direction);
            attackIntervalTimer = 0;
        }
    }

    @Override
    public void update(double deltaTime) {

//        if (health == 0){
//            System.out.println("Game Over");
//
//            return;
//        }


        keyHandler.updateVelocity();

        if (isAppear) {
            return;
        }

        if (isShield) {
            shield.getCurrentFrame();
        }

        HashSet<GameEntity> collidedEntities = checkCollision(deltaTime);

        if (collidedEntities == null) {
            move(deltaTime);
        } else {
            // System.out.println("--------------------");
            for (GameEntity e : collidedEntities) {
                // System.out.println(e);
                if (e.getType() == EntityType.POWER_UP) {
                    // remove and trigger power-up effect
                    PowerUpManager.triggerPowerUp((PowerUp) e);
                }
            }
        }

        attackIntervalTimer += deltaTime;
    }

    public void move(double deltaTime) {
        setPosition(getPosition().add(getVelocity().multiply(deltaTime)));
    }

    public void keyPressed(KeyEvent e) {
        if (isAppear) {
            return;
        }
        keyHandler.keyPressed(e);
    }

    public void keyReleased(KeyEvent e) {
        keyHandler.keyReleased(e);
    }

    public void draw(Graphics g) {
        // Nếu shield đang hoạt động, vẽ shield lên tank
        if (isShield) {
            ImageIcon currentShieldFrame = shield.getCurrentFrame();
            if (currentShieldFrame != null) {
                g.drawImage(currentShieldFrame.getImage(), (int) position.x, (int) position.y, null);
            }
        }
    }

    public void disablePlayer() {
        enabled = false;
        if (collisionBox != null) {
            collisionBox.setEnabled(false);
            setPosition(GameConstants.TANK_DISABLED_POSITION);
        }
        image = null;
    }

    public void enablePlayer() {
        enabled = true;
        if (collisionBox != null) {
            collisionBox.setEnabled(true);
            setPosition(CollisionUtil.getPositionByIndex(GameConstants.PLAYER_SPAWNING_INDEX, 16, 16));
        }
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    // public int getLives() {
    // return lives;
    // }

    // public void setLives(int lives) {
    // this.lives = lives;
    // }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return "PlayerTank{" +
                ", health=" + this.getHealth() +
                ", bulletSpeed=" + this.getBulletSpeed() +
                ", movementSpeed=" + this.getMovementSpeed() +
                ", color='" + this.getColor() + '\'' +
                '}';
    }

}