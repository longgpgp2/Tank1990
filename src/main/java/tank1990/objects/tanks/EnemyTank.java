package tank1990.objects.tanks;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;

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
import tank1990.manager.animation.Shield;

public abstract class EnemyTank extends Tank {
    public Image[] images;

    private int point;
    private boolean isAppear = true;
    private boolean isShield = false;

    private String name;
    private String specialTraits;
    private Appear appear;
    private Shield shield;

    private Direction currentDirection = Direction.DOWN;

    // (adjustable) After how long can entity change direction
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

        images = new Image[4]; // UP, DOWN, LEFT, RIGHT
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

    public void update(double deltaTime) {
        if (isAppear) {
            return;
        }

        if (!isAppear) {
            image = images[currentDirection.ordinal()];
        }

        ArrayList<GameEntity> collisionEntities = GameEntityManager.getCollisionEntities(type);
        ArrayList<GameEntity> collidedEntities = checkCollision(collisionEntities, deltaTime);

        if (collidedEntities == null) {
            move(deltaTime);
        }

        availableDirections = findAvailableDirections(collisionEntities, deltaTime);

        if (collidedEntities != null && directionChangeTimer > directionChangeInterval) {
            changeDirection(availableDirections);
            directionChangeTimer = 0;
        }

        if (collidedEntities == null && availableDirections.size() > prevAvailableDirections.size()
                && directionChangeTimer > directionChangeInterval) {
            changeDirection(availableDirections);
        }

        prevAvailableDirections = availableDirections;
        directionChangeTimer += deltaTime;
    }

    public void changeDirection(ArrayList<Direction> availableDirections) {
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

            // (adjustable): No pixels to check in the future (E.g.: 5 pixels)
            Vector2D expectedVelocity = (direction.getValue().multiply(6));
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
}
