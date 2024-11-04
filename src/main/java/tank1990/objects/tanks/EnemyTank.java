package tank1990.objects.tanks;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import javax.swing.*;

import tank1990.common.classes.AABB;
import tank1990.common.classes.CollisionBox;
import tank1990.common.classes.GameEntity;
import tank1990.common.classes.Vector2D;
import tank1990.common.constants.GameConstants;
import tank1990.common.enums.CollisionType;
import tank1990.common.enums.Direction;
import tank1990.common.enums.EntityType;
import tank1990.common.utils.CollisionUtil;
import tank1990.common.utils.CommonUtil;
import tank1990.manager.GameEntityManager;
import tank1990.manager.animation.Appear;
import tank1990.manager.animation.ExplosionAnimation;
import tank1990.manager.animation.Shield;

public abstract class EnemyTank extends Tank {
    public Image[][] images;
    private int frameCounter = 0;
    private final int animationInterval = 100;

    private int point;
    private boolean isAppear = true;
    private boolean isShield = false;

    private String name;
    private String specialTraits;
    private Appear appear;
    private Shield shield;

    protected Direction currentDirection = Direction.DOWN;

    // (adjustable) The position of 4 potential collision boxes in order from TOP
    // DOWN LEFT RIGHT. For example if set to 5, 4 future collision boxes are placed
    // 5 pixels off TOP DOWN LEFT RIGHT respectively from origin collision box.
    private int directionPixelCheck = 10;

    // (adjustable) After how long before the entity can change direction. Will
    // reset after each direction change. Measure in second.
    private double directionChangeInterval = 1;
    private double directionChangeTimer = 0;

    private ArrayList<Direction> availableDirections = new ArrayList<>();
    private ArrayList<Direction> prevAvailableDirections = new ArrayList<>();

    public EnemyTank(String name, int health, int point, int bulletSpeed, int movementSpeed, String specialTraits) {
        super(EntityType.ENEMY, health, bulletSpeed, movementSpeed, Direction.DOWN);
        this.name = name;
        this.point = point;
        this.specialTraits = specialTraits;

        CollisionBox collisionBox = new CollisionBox(
                this, new Vector2D(2.5, 2.5),
                GameConstants.TANK_SIZE - 5,
                GameConstants.TANK_SIZE - 5);

        setCollision(collisionBox);
        collisionBox.setEnableFrontCollisionCheck(true);
        collisionBox.setCollisionType(CollisionType.RIGID);

        appear = new Appear(100);

        startAnimation();

        this.images = new Image[4][2]; // UP, DOWN, LEFT, RIGHT
        loadImages();
    }

    protected abstract void loadImages();

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
            String className = this.getClass().getSimpleName().toLowerCase();
            String typeName = className.replace("tank", "");
            image = new ImageIcon("src/main/resources/images/tank_" + typeName + ".png").getImage();

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

                if (System.currentTimeMillis() - startTime >= 3000) { // 3 giây
                    isShield = false;
                    System.out.println("Shield ended for EnemyTank.");
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
    public boolean isAppearing() {
        return isAppear;
    }
    public void update(double deltaTime) {
        if (isAppear) {
            return;
        }
            frameCounter += deltaTime * 1000; // Tăng theo thời gian thực
            if (frameCounter >= animationInterval) {
                frameCounter -= animationInterval;
                int imageIndex = (frameCounter < animationInterval / 2) ? 0 : 1;
                image = images[currentDirection.ordinal()][imageIndex];
            }

            ArrayList<GameEntity> collisionEntities = GameEntityManager.getCollisionEntities(type);
            HashSet<GameEntity> collidedEntities = checkCollision(collisionEntities, deltaTime);

            if (collidedEntities == null) {
                move(deltaTime);
            }

            availableDirections = findAvailableDirections(collisionEntities, deltaTime);

            if (collidedEntities != null && directionChangeTimer > directionChangeInterval) {
                changeDirection(availableDirections);
                directionChangeTimer = 0;
            }
            if (collidedEntities !=null)
            if(collidedEntities.stream().filter(gameEntity -> gameEntity.getType()==EntityType.ENEMY).count()>=1 && directionChangeTimer > directionChangeInterval){
                System.out.println(availableDirections);
                changeDirection(availableDirections);
                directionChangeTimer = 0;
            }
            if (collidedEntities == null && availableDirections.size() > prevAvailableDirections.size()
                    && directionChangeTimer > directionChangeInterval) {
                changeDirection(availableDirections);
                directionChangeTimer = 0;
            }

            prevAvailableDirections = availableDirections;
            directionChangeTimer += deltaTime;
        }


    public void changeDirection(ArrayList<Direction> availableDirections) {
        if(availableDirections.size()>=4 && availableDirections.contains(currentDirection.getOpposite()) && availableDirections.contains(currentDirection)) {
            availableDirections.remove(currentDirection.getOpposite());
            availableDirections.remove(currentDirection);
        }
        if(availableDirections.size()>=3 && availableDirections.contains(currentDirection.getOpposite())) {
            availableDirections.remove(currentDirection.getOpposite());}
        int randomIndex = CommonUtil.randomInteger(0, availableDirections.size() - 1);
        Direction randomDirection = availableDirections.get(randomIndex);

        currentDirection = randomDirection;
    }

    // Add more chance to explore new direction
    public void changeDirection(
            ArrayList<Direction> prevDirections,
            ArrayList<Direction> newDirections,
            int prevDirectionChance,
            int newDirectionChance) {
        ArrayList<Direction> availableDirections = new ArrayList<>();

        for (int i = 0; i < prevDirectionChance; i++) {
            availableDirections.addAll(prevDirections);
        }

        for (int i = 0; i < newDirectionChance; i++) {
            availableDirections.addAll(newDirections);
        }

        int randomIndex = CommonUtil.randomInteger(0, availableDirections.size() - 1);
        Direction randomDirection = availableDirections.get(randomIndex);

        currentDirection = randomDirection;
    }

    public void move(double deltaTime) {
        setVelocity(currentDirection.getValue().multiply(movementSpeed));
        setPosition(getPosition().add(getVelocity().multiply(deltaTime)));
    }

    public ArrayList<Direction> findAvailableDirections(ArrayList<GameEntity> collisionEntities, double deltaTime) {
        ArrayList<AABB> potentialAABBs = potentialAABBs(potentialCollisionBoxPosition(deltaTime));
        ArrayList<Direction> directions = new ArrayList<>();

        try {
            int index = 0;
            for (AABB expectedAABB : potentialAABBs) { // TOP >> DOWN >> LEFT >> RIGHT
                boolean isAvailable = true;

                for (GameEntity entity : collisionEntities) {
                    if (entity == null || entity.getCollision() == null) {
                        continue;
                    }
                    if (entity.getType() == EntityType.ENEMY) {
                        continue;
                    }
                    if (CollisionUtil.checkIntersection(expectedAABB, entity.getCollision().getAABB())) {
                        isAvailable = false;
                        break;
                    }
                }

                if (isAvailable) {
                    directions.add(Direction.values()[index]);
                }
                index++;
            }

        } catch (Exception e) {
            // System.out.println(e);
        }

        return directions;
    }

    public ArrayList<Vector2D> potentialCollisionBoxPosition(double deltaTime) {
        ArrayList<Vector2D> potentialPositions = new ArrayList<>();

        for (Direction direction : Direction.values()) {
            if (direction.equals(Direction.NONE)) {
                continue;
            }

            Vector2D expectedVelocity = (direction.getValue().multiply(directionPixelCheck));
            Vector2D expectedPosition = collisionBox.globalPosition.add(expectedVelocity);

            potentialPositions.add(expectedPosition);
        }

        return potentialPositions;
    }

    public ArrayList<AABB> potentialAABBs(ArrayList<Vector2D> potentialCollisionPositions) {
        ArrayList<AABB> potentialAABBs = new ArrayList<>();

        for (Vector2D collBoxPosition : potentialCollisionPositions) {
            Vector2D expectedCenter = new Vector2D(
                    collBoxPosition.x + collisionBox.width / 2.0,
                    collBoxPosition.y + collisionBox.height / 2.0);

            Vector2D expectedExtent = new Vector2D(collisionBox.width / 2.0, collisionBox.height / 2.0);

            AABB expectedAABB = new AABB(expectedCenter, expectedExtent);

            potentialAABBs.add(expectedAABB);
        }

        return potentialAABBs;
    }

    @Override
    public void destroy() {
        // animation nổ
        ExplosionAnimation explosion = new ExplosionAnimation();
        explosion.startAnimation(() -> {
            image = null;
        }, new ExplosionAnimation.ImageUpdateCallback() {
            @Override
            public void updateImage(Image newImage) {
                image = newImage; // tao new image de thay the tung anh animation
            }
        });

        // Xóa CollisionBox
        if (collisionBox != null) {
            collisionBox = null;
        }
    }

    @Override
    public Bullet shoot() {
        System.out.println(name + " is shooting!");

        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialTraits() {
        return specialTraits;
    }

    public void setSpecialTraits(String specialTraits) {
        this.specialTraits = specialTraits;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return this.health;
    }

    @Override
    public String toString() {
        return "EnemyTank{" +
                "name='" + name + '\'' +
                ", health=" + this.getHealth() +
                ", bulletSpeed=" + this.getBulletSpeed() +
                ", movementSpeed=" + this.getMovementSpeed() +
                ", point=" + point +
                ", specialTraits=" + specialTraits +
                '}';
    }

//    public void draw(Graphics g) {
//        if (isAppear) {
//            g.drawImage(images[movingDirection.ordinal()], (int) position.x, (int) position.y, null);
//        }
//        if (isShield) {
//            ImageIcon currentShieldFrame = shield.getCurrentFrame();
//            if (currentShieldFrame != null) {
//                g.drawImage(currentShieldFrame.getImage(), (int) position.x, (int) position.y, null);
//            }
//        }
//    }

    public void freeze() {
        // save original speed
        int defaultBulletSpeed = bulletSpeed;
        int defaultMovementSpeed = movementSpeed;

        // freeze the tank
        this.bulletSpeed = 0;
        this.movementSpeed = 0;

        // unfreeze the tank after some time
        Timer timer = new Timer(5000, e -> {
            this.bulletSpeed = defaultBulletSpeed;
            this.movementSpeed = defaultMovementSpeed;
            System.out.println("Freeze over");
        });
        timer.setRepeats(false);
        timer.start();
    }
}
