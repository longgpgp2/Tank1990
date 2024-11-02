package tank1990.objects.tanks;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import tank1990.common.classes.CollisionBox;
import tank1990.common.classes.GameEntity;
import tank1990.common.classes.Vector2D;
import tank1990.common.constants.GameConstants;
import tank1990.common.enums.CollisionType;
import tank1990.common.enums.Direction;
import tank1990.common.enums.EntityType;
import tank1990.manager.GameEntityManager;
import tank1990.manager.KeyHandler;
import tank1990.manager.animation.Appear;
import tank1990.manager.animation.Shield;

public class PlayerTank extends Tank {
    private static int counter = 0;
    private int owner;
    public List<Bullet> bullets = new ArrayList<>();
    public long lastShotTime = 0;
    public long shotDelay = 300;

    public int maxBullets;
    public int speed = 80;
    public int star = 1;
    KeyHandler keyHandler;

    private boolean isAppear = true;
    private Appear appear;

    private Shield shield;
    private boolean isShield = false;

    public PlayerTank(int owner, int maxBullets) {
        super(EntityType.PLAYER, 1, 1, 1, Direction.UP);
        this.owner = owner;
        this.setColor(Color.YELLOW);
        appear = new Appear(100);
        image = appear.getCurrentFrame().getImage();
        startAnimation();
        setCollision(
                new CollisionBox(this, new Vector2D(4, 4), GameConstants.TANK_SIZE - 8,
                        GameConstants.TANK_SIZE - 8));

        this.maxBullets = maxBullets;
        keyHandler = new KeyHandler(this);

        collisionBox.setEnableFrontCollisionCheck(true);
        collisionBox.setCollisionType(CollisionType.RIGID);
    }

    private void startAnimation() {
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

    private void startShield() {
        shield = new Shield(200);
        isShield = true;

        new Thread(() -> {
            long startTime = System.currentTimeMillis();
            while (!shield.isAnimationFinished()) {
                shield.getCurrentFrame();

                if (System.currentTimeMillis() - startTime >= 3000) { // 3s
                    isShield = false;
                    break;
                }

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public Bullet shoot() {
        long currentTime = System.currentTimeMillis();
        System.out.println(bullets.size());
        if (currentTime - lastShotTime < shotDelay || bullets.size() >= maxBullets) {
            return null;
        }
        int bulletX = (int) getPosition().x;
        int bulletY = (int) getPosition().y;
        //vị trí đạn theo hướng
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

        Bullet bullet = new Bullet(bulletX, bulletY, getDirection(), 50,true);
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

    @Override
    public void update(double deltaTime) {
        keyHandler.updateVelocity();

        if (isAppear) {
            return;
        }

        if (isShield) {
            shield.getCurrentFrame();
        }

        for (Bullet bullet : bullets) {
            bullet.move(deltaTime);
        }

        ArrayList<GameEntity> collisionEntities = GameEntityManager.getCollisionEntities(type);
        ArrayList<GameEntity> collidedEntities = checkCollision(collisionEntities, deltaTime);

        if (collidedEntities == null) {
            move(deltaTime);
        }

        bullets.removeIf(bullet -> bullet.checkBulletOutOfBound() || bullet.isCollided());
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
}