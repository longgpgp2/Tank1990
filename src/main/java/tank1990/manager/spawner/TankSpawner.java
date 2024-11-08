package tank1990.manager.spawner;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;

import javax.swing.Timer;

import tank1990.common.classes.Vector2D;
import tank1990.common.constants.GameConstants;
import tank1990.common.enums.EntityType;
import tank1990.common.utils.CollisionUtil;
import tank1990.common.utils.CommonUtil;
import tank1990.main.GamePanel;
import tank1990.main.GameState;
import tank1990.manager.GameEntityManager;
import tank1990.manager.MapManager;
import tank1990.manager.SoundManager;
import tank1990.objects.environments.Environment;
import tank1990.objects.tanks.EnemyTank;
import tank1990.objects.tanks.PlayerTank;
import tank1990.objects.tanks.Tank;

public class TankSpawner {
    public static Timer timer;
    public static Queue<String> enemyTypes;
    public static PlayerTank playerTank;
    public static List<EnemyTank> enemyTanks = new ArrayList<>();
    private static SoundManager enemySound = new SoundManager();
    private static SoundManager playerSound = new SoundManager();
    private static GameState gameState = GameState.getInstance();

    static {
        enemySound.soundLoader(".\\src\\main\\resources\\sounds\\appearance-effect.wav");
        playerSound.soundLoader(".\\src\\main\\resources\\sounds\\tank-spawn.wav");
    }

    // Tạo 1 timer để queue từng enemy trong Queue vào vào list
    public static void startQueueingEnemies(Queue<String> types) {
        // if (types.peek() != null) {
        // addAnEnemyToList(unoccupiedIndices, tanks, types.poll());
        // CollisionUtil.addCollisionToObjects();
        // }
        timer = new Timer(5000, (ActionListener) new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isBoardFull = getNumberOfAliveTanks(Tank.getTanks()) >= 6;
                if (types.peek() == null) {
                    timer.stop();
                    return;
                }
                if (!isBoardFull) {
                    addAnEnemyToList(MapManager.getUnoccupiedIndex(), Tank.getTanks(), types.poll());
                    CollisionUtil.addCollisionToObjects();
                }
            }
        });
        timer.start();
    }

    // tắt timer để dừng queue enemies
    public static void stopQueueingEnemies() {
        timer.stop();
    }

    // Xoá tank trên map và tank trong queue
    // public static void removeAllTanks(){
    // enemyTypes = null;
    // stopQueueingEnemies();
    //// Tank.getTanks().stream().forEach(tank -> tank = null);
    // PlayerTank tank = (PlayerTank) Tank.getTanks().getFirst();
    // removePlayer();
    // spawnPlayer();
    // }

    public static void removePlayer() {
        PlayerTank tank = (PlayerTank) Tank.getTanks().get(0);
        tank.enabled = false;
        if (tank.getCollision() != null) {
            tank.getCollision().setEnabled(false);
            tank.setPosition(GameConstants.TANK_DISABLED_POSITION);
        }
        tank.image = null;
    }

    public static void spawnPlayer() {
        PlayerTank tank = (PlayerTank) Tank.getTanks().get(0);
        tank.enabled = true;
        if (tank.getCollision() != null) {
            tank.getCollision().setEnabled(true);
            tank.setPosition(CollisionUtil.getPositionByIndex(GameConstants.PLAYER_SPAWNING_INDEX, 16, 16));
        }
        tank.startShield();
    }

    public static void removeEnemies() {
        Tank.getTanks().stream()
                .filter(tank -> tank.getType().equals(EntityType.ENEMY))
                .forEach(tank -> {
                    tank.enabled = false;
                    tank.getCollision().setEnabled(false);
                    tank.setPosition(GameConstants.TANK_DISABLED_POSITION);
                    GameEntityManager.remove(tank);
                });

    }

    public static void enableEnemySpawner(int level) {
        enemyTypes = readTanksFromLevel(level);
        startQueueingEnemies(enemyTypes);
    }

    public static void disableEnemySpawner() {
        timer.stop();
    }

    // trả ra 1 list tất cả các tank|| được dùng để init tanks
    public static List<Tank> spawnTanks(int level) {
        List<Tank> tanks = new ArrayList<>();
        Set<Integer> unoccupiedIndices = MapManager.getUnoccupiedIndex();
        enemyTypes = readTanksFromLevel(level);
        playerTank = (PlayerTank) createPlayer();
        tanks.add(playerTank);
        startQueueingEnemies(enemyTypes);
        return tanks;
    }

    // truyền list tanks vào để check victory: player còn sống && kẻ địch đã spawn
    // hết && health của tất cả địch = 0
    public static boolean checkVictory(List<Tank> tanks) {
        boolean isPlayerAlive = MapManager.getPlayerTank().getHealth() != 0;
        boolean areEnemiesAllSpawned = true;
        if (enemyTypes != null) {
            areEnemiesAllSpawned = enemyTypes.peek() == null;
        }
        boolean areEnemiesAllDead = tanks.stream()
                .filter(tank -> tank != MapManager.getPlayerTank() && tank.getHealth() != 0)
                .collect(Collectors.toList()).isEmpty();
        if (isPlayerAlive && areEnemiesAllSpawned && areEnemiesAllDead) {
            return true;
        }
        return false;
    }

    // Tạo 1 player với position cố định // sẽ bỏ hàm này
    public static Tank createPlayer() {
        Tank player = new PlayerTank(1);
        player.setPosition(CollisionUtil.getPositionByIndex(GameConstants.PLAYER_SPAWNING_INDEX, 16, 16));
        if (gameState.isSoundOn()) {
            playerSound.resetSound();
            playerSound.playSound();
        } else {
            playerSound.stopSound();
        }
        return player;
    }

    public static void respawnPlayer() {
        PlayerTank playerTank = (PlayerTank) GameEntityManager.getGameEntity(EntityType.PLAYER)[0];
        if (playerTank != null) {
            playerTank.setPosition(new Vector2D(100, 80));
            if (gameState.isSoundOn()) {
                playerSound.resetSound();
                playerSound.playSound();
            } else {
                playerSound.stopSound();
            }
        }
    }

    // Tạo 1 list các enemies
    public static List<Tank> createEnemies(Set<Integer> unoccupiedIndices) {
        List<Tank> tanks = new ArrayList<>();
        // String[] types = readTanksFromLevel(1);
        String[] types = { "BasicTank", "FastTank", "ArmorTank", "PowerTank" };

        for (String type : types) {
            tanks = addAnEnemyToList(unoccupiedIndices, tanks, type);
        }
        return tanks;
    }

    // Tạo instance của enemy tank dựa trên className
    public static Tank createEnemyTankByType(String className) {
        try {
            String classPath = "tank1990.objects.tanks.enemy_tanks." + className;
            Class<?> clazz = Class.forName(classPath);

            Constructor<?> constructor = clazz.getConstructor();

            return (Tank) constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Đọc file level và trả ra 1 String array chứa tên enemy tank
    public static Queue<String> readTanksFromLevel(int level) {
        Queue<String> tanks = new LinkedList<>();
        File file = new File(".\\src\\main\\resources\\levels\\" + level + "\\tanks.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            Arrays.stream(line.split(" ")).forEach(tank -> tanks.add(tank));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return tanks;
    }

    // thêm kẻ địch vào list tank
    public static List<Tank> addAnEnemyToList(Set<Integer> unoccupiedIndices, List<Tank> tanks, String enemyType) {
        Set<Integer> tanksIndices = new HashSet<>();
        if (!tanks.isEmpty()) {
            for (Tank tank : tanks) {
                tanksIndices.add(CollisionUtil.getTileIndex(tank.getPosition()));
            }
        }

        unoccupiedIndices.removeAll(tanksIndices);
        Tank tank = createEnemyTankByType(enemyType);
        while (true) {
            Vector2D position = CollisionUtil.getPositionByIndex(
                    GameConstants.ENEMY_SPAWNING_INDEX,
                    GameConstants.ENTITY_WIDTH,
                    GameConstants.ENTITY_HEIGHT);
            tank.setPosition(position);
            tanks.add(tank);
            enemyTanks.add((EnemyTank) tank);
            if (gameState.isSoundOn()) {
                enemySound.resetSound();
                enemySound.playSound();
            } else {
                enemySound.stopSound();
            }
            break;
        }
        return tanks;
    }

    public static int getNumberOfAliveTanks(List<Tank> tanks) {
        return (int) tanks.stream().filter(tank -> tank.getHealth() > 0).count();
    }

}
