package tank1990.objects.tanks;

import tank1990.common.classes.GameEntity;
import tank1990.common.classes.Vector2D;
import tank1990.common.constants.GameConstants;
import tank1990.common.enums.Direction;
import tank1990.common.enums.EntityType;
import tank1990.manager.GameEntityManager;
import tank1990.objects.animation.Appear;
import tank1990.objects.animation.Shield;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Tank extends GameEntity {
        public boolean enabled = true;
        public int health;
        public int bulletSpeed;
        public int movementSpeed;
        public Color color;
        public Image image;
        public List<Bullet> bullets = new ArrayList<>();

        protected Shield shield;
        protected boolean isShield = false;

        protected boolean isAppear = true;
        protected Appear appear;

        protected int bulletCount = 1;
        protected Direction direction;

        public Tank(EntityType type, int health, int bulletSpeed, int bulletCount, int movementSpeed,
                        Direction direction) {
                super(type, new Vector2D(0, 0), GameConstants.TANK_SIZE, GameConstants.TANK_SIZE);
                this.health = health;
                this.bulletSpeed = bulletSpeed;
                this.movementSpeed = movementSpeed;
                this.direction = direction;
                this.bulletCount = bulletCount;
        }

        // default shield
        public void startShield() {
                startShield(5);
        }

        public void startShield(int maxAnimationLoops) {
                shield = new Shield(200);
                shield.setMaxAnimationLoops(maxAnimationLoops);
                isShield = true;
                System.out.println(isShielded());

                new Thread(() -> {
                        long startTime = System.currentTimeMillis();
                        while (!shield.isAnimationFinished()) {
                                shield.getCurrentFrame();
                                try {
                                        Thread.sleep(50);
                                } catch (InterruptedException e) {
                                        e.printStackTrace();
                                }
                        }
                        isShield = false;
                        System.out.println("Shield ended for PlayerTank. " + (System.currentTimeMillis() - startTime));
                }).start();
        }

        /**
         * Return bullet which is not moving in the list of available bullets.
         * 
         * @return Bullet
         */
        public Bullet getBullet() {
                Bullet[] availableBullets = bullets
                                .stream().filter((bullet -> bullet.getVelocity().isZero()))
                                .toArray(Bullet[]::new);
                if (availableBullets.length == 0) {
                        return null;
                }

                return availableBullets[0];
        }

        /**
         * Generate bullets based on bullets count.
         */
        public void initalizeBullets() {
                if (bullets.size() > 0) {
                        bullets.clear();
                }

                for (int i = 0; i < bulletCount; i++) {
                        int x = (int) Bullet.DEFAULT_POSITION.x;
                        int y = (int) Bullet.DEFAULT_POSITION.y;

                        Bullet bullet = new Bullet(x, y, this);

                        bullet.setVelocity(new Vector2D(0, 0));
                        bullet.getCollision().setEnabled(false);
                        bullets.add(bullet);
                }
        }

        public void createBullet(Direction direction) {
                Bullet bullet = getBullet();

                if (bullet == null) {
                        return;

                }
                Vector2D offset = new Vector2D(4, 4);

                bullet.destroyed = false;
                bullet.setSpeed(bulletSpeed);
                bullet.setDirection(direction);
                bullet.setPosition(this.center.minus(offset));
                bullet.getCollision().setEnabled(true);
        }

        public String toString() {
                return null;
        }

        public int getHealth() {
                return health;
        }

        public void setHealth(int health) {
                this.health = health;
        }

        public int getBulletSpeed() {
                return bulletSpeed;
        }

        public void setBulletSpeed(int bulletSpeed) {
                this.bulletSpeed = bulletSpeed;
        }

        public int getMovementSpeed() {
                return movementSpeed;
        }

        public void setMovementSpeed(int movementSpeed) {
                this.movementSpeed = movementSpeed;
        }

        public Direction getDirection() {
                return direction;
        }

        public void setDirection(Direction direction) {
                this.direction = direction;
        }

        public Color getColor() {
                return color;
        }

        public void setColor(Color color) {
                this.color = color;
        }

        public int getBulletCount() {
                return bulletCount;
        }

        public void setBulletCount(int bulletCount) {
                this.bulletCount = bulletCount;
        }

        public List<Bullet> getBullets() {
                return bullets;
        }

        public void setBullets(List<Bullet> bullets) {
                this.bullets = bullets;
        }

        public Shield getShield() {
                return shield;
        }

        public boolean isShielded() {
                return isShield;
        }

        public boolean isAppearing() {
                return isAppear;
        }

        public static List<Tank> getTanks() {
                List<Tank> tanks = new ArrayList<>();
                Arrays.asList(GameEntityManager.getGameEntity(EntityType.PLAYER))
                                .stream()
                                .forEach(gameEntity -> tanks.add((Tank) gameEntity));
                Arrays.asList(GameEntityManager.getGameEntity(EntityType.ENEMY))
                                .stream()
                                .forEach(gameEntity -> tanks.add((Tank) gameEntity));
                return tanks;
        }
}
