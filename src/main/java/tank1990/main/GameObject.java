package tank1990.main;

import java.util.ArrayList;

import tank1990.common.classes.GameEntity;
import tank1990.common.classes.GameLoop;
import tank1990.common.constants.GameConstants;
import tank1990.common.enums.EntityType;
import tank1990.manager.GameEntityManager;
import tank1990.manager.MapManager;
import tank1990.manager.PowerUpManager;
import tank1990.manager.spawner.TankSpawner;
import tank1990.objects.tanks.PlayerTank;

import static tank1990.manager.spawner.TankSpawner.playerTank;

public class GameObject extends GameLoop {
    private static GameObject instance;
    private GameFrame gameFrame;
    public int currentLevel = 1;

    public GameObject() {
        gameFrame = new GameFrame();
    }

    public void startGame() {
        GameEntityManager.setCollisionEntities(EntityType.ENEMY, GameConstants.IMPASSABLE_ENTITIES);
        GameEntityManager.setCollisionEntities(EntityType.PLAYER, GameConstants.PLAYER_IMPASSABLE_ENTITIES);
        GameEntityManager.setCollisionEntities(EntityType.BULLET, new EntityType[] {
                EntityType.BRICK,
                EntityType.STEEL,
                EntityType.ENEMY,
                EntityType.PLAYER

        });
        run();
    }

    public static GameObject getInstance() {
        if (instance == null) {
            instance = new GameObject();
        }
        return instance;
    }

    @Override
    protected void processGameLoop() {
        long initalFrameTime = System.nanoTime();
        double lastFrameTime = 0.0;

        while (isGameRunning()) {
            double currentFrameTime = (System.nanoTime() - initalFrameTime) / 1E9;
            double deltaTime = currentFrameTime - lastFrameTime;

            update(deltaTime);
            render();

            lastFrameTime = currentFrameTime;
        }
    }

    protected void render() {
        if (gameFrame != null) {
            gameFrame.draw();
        }
    }

    protected void update(double deltaTime) {
        try {
            for (GameEntity gameEntity : GameEntityManager.getGameEntities()) {
                gameEntity.update(deltaTime);
            }
        } catch (Exception e) {
            // System.out.println(e);
        }
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void eraseGame() {
        TankSpawner.removePlayer();
        TankSpawner.removeEnemies();
        TankSpawner.disableEnemySpawner();
        PowerUpManager.resetPowerUps();
        GameEntityManager.removeAllExcept(new EntityType[] {
                EntityType.PLAYER,
                EntityType.BULLET,
        });
    }

    public void newGame() {
        currentLevel = 1;
        PowerUpManager.startAutoSpawn();
        TankSpawner.spawnPlayer();
        MapManager.generateEnvironments(currentLevel);
        TankSpawner.enableEnemySpawner(currentLevel);
        playerTank.setHealth(1);
        GameInfoPanel.getInstance().resetEnemyPanel();
        GameInfoPanel.getInstance().updateLevelLabel();
        GameInfoPanel.getInstance().resetPoint();
    }

    public void nextLevel() {
        currentLevel += 1;
        TankSpawner.removePlayer();
        TankSpawner.removeEnemies();
        TankSpawner.disableEnemySpawner();
        PowerUpManager.resetPowerUps();
        GameEntityManager.removeAllExcept(new EntityType[] {
                EntityType.PLAYER,
                EntityType.BULLET,
        });

        TankSpawner.spawnPlayer();
        PowerUpManager.startAutoSpawn();
        TankSpawner.enableEnemySpawner(currentLevel);
        MapManager.generateEnvironments(currentLevel);
        GameInfoPanel.getInstance().resetEnemyPanel();
        GameInfoPanel.getInstance().updateLevelLabel();
    }
}
