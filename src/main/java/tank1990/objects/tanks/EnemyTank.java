package tank1990.objects.tanks;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;

import tank1990.common.classes.CollisionBox;
import tank1990.common.classes.GameEntity;
import tank1990.common.classes.Vector2D;
import tank1990.common.constants.GameConstants;
import tank1990.common.enums.Direction;
import tank1990.common.enums.EntityType;
import tank1990.manager.GameEntityManager;
import tank1990.manager.animation.Appear;
import tank1990.manager.animation.Shield;

public abstract class EnemyTank extends Tank {
    private String name;
    private int point;
    private String specialTraits;
    private boolean isAppear = true;
    private Appear appear;

    private boolean isShield = false;
    private Shield shield;

    private Direction movingDirection;
    private Random random;
    private Direction lastDirection;
    private Vector2D targetPosition = null;
    public Image[] images;

    public EnemyTank(String name, int health, int point, int bulletSpeed, int movementSpeed, String specialTraits) {
        super(EntityType.ENEMY, health, bulletSpeed, movementSpeed, Direction.DOWN);
        this.name = name;
        this.point = point;
        this.specialTraits = specialTraits;
        setCollision(
                new CollisionBox(this, new Vector2D(2.5, 2.5), GameConstants.TANK_SIZE, GameConstants.TANK_SIZE));
        appear = new Appear(100);
        startAnimation();
        movingDirection = this.direction;
        random = new Random();

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
        if (!isAppear) {
            updatePosition();
            checkAndChangeDirection();
        }
    }

    private void checkAndChangeDirection() {
        ArrayList<GameEntity> gameEntities = GameEntityManager.getGameEntities();
        ArrayList<Direction> availableDirections = checkAvailableDirections(gameEntities);

        if (availableDirections.isEmpty())
            return;

        // Collision handling and direction selection
        if (availableDirections.size() > 2) {
            // More than two directions available, pick one randomly (excluding
            // lastDirection)
            availableDirections.remove(lastDirection);
            movingDirection = availableDirections.get(random.nextInt(availableDirections.size()));
            updateLastDirection();
        } else {
            // Existing handling for one or two available directions
            Vector2D potentialNewPosition = getPosition().add(movingDirection.getVector().scale(movementSpeed));
            if (checkCollisionWithEntities(potentialNewPosition, gameEntities)) {
                if (availableDirections.size() == 1) {
                    movingDirection = availableDirections.get(0);
                } else if (availableDirections.size() == 2) {
                    if (availableDirections.contains(movingDirection)) {
                        // Keep current direction
                    } else {
                        movingDirection = availableDirections.get(0).equals(lastDirection)
                                ? availableDirections.get(1)
                                : availableDirections.get(0);
                    }
                }
                updateLastDirection();
            } else {
                setPosition(potentialNewPosition);
            }
        }
    }

    private boolean checkCollisionWithEntities(Vector2D position, ArrayList<GameEntity> gameEntities) {
        for (GameEntity entity : gameEntities) {
            if (entity != this && checkCollisionWithEntity(position, entity, this.getCollision().width,
                    this.getCollision().height)) {
                return true; // Phát hiện va chạm
            }
        }
        return false; // Không có va chạm
    }

    private void updateLastDirection() {
        if (movingDirection == Direction.UP) {
            lastDirection = Direction.DOWN;
        } else if (movingDirection == Direction.DOWN) {
            lastDirection = Direction.UP;
        } else if (movingDirection == Direction.LEFT) {
            lastDirection = Direction.RIGHT;
        } else if (movingDirection == Direction.RIGHT) {
            lastDirection = Direction.LEFT;
        }
    }

    private ArrayList<Direction> checkAvailableDirections(ArrayList<GameEntity> gameEntities) {
        ArrayList<Direction> availableDirections = new ArrayList<>();

        Direction[] directions = { Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT };

        for (Direction direction : directions) {
            Vector2D tempPosition = getPosition().add(direction.getVector().scale(GameConstants.TANK_SIZE));
            CollisionBox tempCollisionBox = new CollisionBox(this, tempPosition, this.getCollision().width,
                    this.getCollision().height);

            boolean collisionDetected = false;

            for (GameEntity entity : gameEntities) {
                if (entity != this) {
                    if (checkCollisionWithEntity(tempPosition, entity, tempCollisionBox.width,
                            tempCollisionBox.height)) {
                        // Nếu có va chạm với đối tượng không phải TREE hoặc POWER_UP, đánh dấu va chạm
                        if (entity.getType() != EntityType.TREE && entity.getType() != EntityType.POWER_UP) {
                            collisionDetected = true;
                            break;
                        }
                    }
                }
            }

            // Chỉ thêm hướng nếu không có va chạm với bất kỳ đối tượng nào khác ngoài TREE
            // hoặc POWER_UP
            if (!collisionDetected) {
                availableDirections.add(direction);
            }
        }

        // System.out.println(getName() + " Available directions: " +
        // availableDirections);
        return availableDirections;
    }

    private boolean checkCollisionWithEntity(Vector2D position, GameEntity entity, int width, int height) {
        // Kiểm tra va chạm theo hình chữ nhật
        return (position.x < entity.x + entity.width &&
                position.x + width > entity.x &&
                position.y < entity.y + entity.height &&
                position.y + height > entity.y);
    }

    public void updatePosition() {
        // Di chuyển theo hướng đã chọn
        if (movingDirection == Direction.UP) {
            moveIfCollisionClear(getPosition().add(new Vector2D(0, -movementSpeed)));
        } else if (movingDirection == Direction.DOWN) {
            moveIfCollisionClear(getPosition().add(new Vector2D(0, movementSpeed)));
        } else if (movingDirection == Direction.LEFT) {
            moveIfCollisionClear(getPosition().add(new Vector2D(-movementSpeed, 0)));
        } else if (movingDirection == Direction.RIGHT) {
            moveIfCollisionClear(getPosition().add(new Vector2D(movementSpeed, 0)));
        }

        // Cập nhật hình ảnh dựa trên hướng di chuyển
        if (!isAppear) {
            image = images[movingDirection.ordinal()];
        }
    }

    private void moveIfCollisionClear(Vector2D newPosition) {
        CollisionBox newCollisionBox = new CollisionBox(this, newPosition, this.getCollision().width,
                this.getCollision().height);
        ArrayList<GameEntity> gameEntities = GameEntityManager.getGameEntities();

        boolean collisionDetected = false;

        // Kiểm tra va chạm với các đối tượng khác
        for (GameEntity entity : gameEntities) {
            if (entity != this) {
                if (checkCollisionWithEntity(newPosition, entity, newCollisionBox.width, newCollisionBox.height)) {
                    if (entity.getType() != EntityType.TREE && entity.getType() != EntityType.POWER_UP) {
                        collisionDetected = true;
                        break;
                    }
                }
            }
        }

        // Nếu không có va chạm, cập nhật vị trí
        if (!collisionDetected) {
            setPosition(newPosition);
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

    public void draw(Graphics g) {
        if (isAppear) {
            g.drawImage(images[movingDirection.ordinal()], (int) position.x, (int) position.y, null);
        }
        if (isShield) {
            ImageIcon currentShieldFrame = shield.getCurrentFrame();
            if (currentShieldFrame != null) {
                g.drawImage(currentShieldFrame.getImage(), (int) position.x, (int) position.y, null);
            }
        }
    }
}
