package tank1990.objects.tanks;

import tank1990.main.GamePanel;
import tank1990.objects.common.enums.Direction;
import tank1990.objects.common.Entity;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Tank extends Entity {
        public  int health;
        public  int bulletSpeed;
        public  int movementSpeed;
        public  Direction direction;
        public  int x;
        public  int y;
        public  Color color;
        public  List<Bullet> bullets = new ArrayList<>();
        public  int bulletCount=1;
        public Tank(int health, int bulletSpeed, int movementSpeed, Direction direction) {
                super(0,0,GamePanel.BRICK_WIDTH, GamePanel.BRICK_HEIGHT);
                this.health = health;
                this.bulletSpeed = bulletSpeed;
                this.movementSpeed = movementSpeed;
                this.direction = direction;
        }


        public Bullet shoot(){
                Bullet bullet = new Bullet(getX(), getY(), getDirection(), 10);
                bullets.add(bullet);
                System.out.println("Bullet fired from: (" + getX() + ", " + getY() + ") with direction: " +getDirection());
                return bullet;
        }
        public String toString(){
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

        public int getX() {
                return x;
        }

        public void setX(int x) {
                this.x = x;
        }

        public int getY() {
                return y;
        }

        public void setY(int y) {
                this.y = y;
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
}