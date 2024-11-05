package tank1990.objects.tanks;

import tank1990.common.classes.GameEntity;
import tank1990.common.classes.Vector2D;
import tank1990.common.constants.GameConstants;
import tank1990.common.enums.Direction;
import tank1990.common.enums.EntityType;
import tank1990.objects.animation.Appear;
import tank1990.objects.animation.Shield;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Tank extends GameEntity {
        public int health;
        public int bulletSpeed;
        public int movementSpeed;
        public int bulletCount = 1;
        public Color color;
        public Image image;
        public List<Bullet> bullets = new ArrayList<>();

        protected Shield shield;
        protected boolean isShield = false;

        protected boolean isAppear = true;
        protected Appear appear;

        protected Direction direction;

        public Tank(EntityType type, int health, int bulletSpeed, int movementSpeed, Direction direction) {
                super(type, new Vector2D(0, 0), GameConstants.TANK_SIZE, GameConstants.TANK_SIZE);
                this.health = health;
                this.bulletSpeed = bulletSpeed;
                this.movementSpeed = movementSpeed;
                this.direction = direction;
        }

        // default shield
        public void startShield() {
                startShield(15);
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

        public Bullet shoot() {
                return null;
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
}
