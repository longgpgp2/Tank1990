package tank1990.manager.spawner;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import javax.swing.Timer;

import tank1990.common.classes.Vector2D;
import tank1990.common.constants.GameConstants;
import tank1990.common.enums.EntityType;
import tank1990.common.utils.CollisionUtil;
import tank1990.common.utils.CommonUtil;
import tank1990.manager.GameEntityManager;
import tank1990.manager.MapManager;
import tank1990.objects.environments.Environment;
import tank1990.objects.tanks.PlayerTank;
import tank1990.objects.tanks.Tank;

public class TankSpawner {
    public static Timer timer;

    public static void startQueueingEnemies(Queue<String> types, List<Tank> tanks, Set<Integer> unoccupiedIndices) {
        timer = new Timer(5000, (ActionListener) new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addAnEnemyToList(unoccupiedIndices, tanks, types.poll());
                GameEntityManager.setCollisionEntities(EntityType.ENEMY, GameConstants.IMPASSABLE_ENTITIES);
                GameEntityManager.setCollisionEntities(EntityType.PLAYER, GameConstants.PLAYER_IMPASSABLE_ENTITIES);
                GameEntityManager.setCollisionEntities(EntityType.BULLET, new EntityType[] {
                        EntityType.BRICK,
                        EntityType.STEEL,
                        EntityType.ENEMY
                });
                if (types.peek() == null) {
                    timer.stop();
                }
            }
        });
        timer.start();
    }

    // trả ra 1 list tất cả các tank|| được dùng để init tanks
    public static List<Tank> spawnTanks(List<Environment> environments) {
        List<Tank> tanks = new ArrayList<>();
        Set<Integer> unoccupiedIndices = MapManager.getUnoccupiedIndex(environments, tanks);
        Queue<String> enemyTypes = readTanksFromLevel(1);

        // List<Tank> enemies = createEnemies(unoccupiedIndices);

        // for (Tank tank : enemies) {
        // tanks.add(tank);
        // }
        tanks.add(createPlayer());
        startQueueingEnemies(enemyTypes, tanks, unoccupiedIndices);
        return tanks;
    }

    // Tạo 1 player với position cố định // sẽ bỏ hàm này
    public static Tank createPlayer() {
        Tank player = new PlayerTank(1, 5);
        player.setPosition(new Vector2D(100, 80));
        return player;
    }

    // Tapk 1 list các enemies
    public static List<Tank> createEnemies(Set<Integer> unoccupiedIndices) {
        List<Tank> tanks = new ArrayList<>();
        // String[] types = readTanksFromLevel(1);
        String[] types = { "BasicTank", "FastTank", "ArmorTank", "PowerTank" };

        for (String type : types) {
            tanks = addAnEnemyToList(unoccupiedIndices, tanks, type);
        }
        return tanks;
    }

    // xếp enemies vào 1 queue
    public static Queue<Tank> queueEnemies(Set<Integer> unoccupiedIndices) {
        Queue<String> types = readTanksFromLevel(1);
        Queue<Tank> queue = new LinkedList<>();

        for (String type : types) {
            queue = addAnEnemyToQueue(unoccupiedIndices, queue, type);
        }
        return queue;
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
            int randomIndex = CommonUtil.randomInteger(34, 69);
            if (!MapManager.checkIndexAvailability(randomIndex, unoccupiedIndices)) {
                continue;
            }
            Vector2D position = CollisionUtil.getPositionByIndex(
                    randomIndex,
                    GameConstants.ENTITY_WIDTH,
                    GameConstants.ENTITY_HEIGHT);
            tank.setPosition(position);
            tanks.add(tank);
            break;
        }
        return tanks;
    }

    // thêm kẻ địch vào list tank
    public static Queue<Tank> addAnEnemyToQueue(Set<Integer> unoccupiedIndices, Queue<Tank> tanks, String enemyType) {
        Set<Integer> tanksIndices = new HashSet<>();
        if (!tanks.isEmpty()) {
            for (Tank tank : tanks) {
                tanksIndices.add(CollisionUtil.getTileIndex(tank.getPosition()));
            }
        }

        unoccupiedIndices.removeAll(tanksIndices);
        Tank tank = createEnemyTankByType(enemyType);
        while (true) {
            int randomIndex = CommonUtil.randomInteger(34, 69);
            if (!MapManager.checkIndexAvailability(randomIndex, unoccupiedIndices)) {
                continue;
            }
            Vector2D position = CollisionUtil.getPositionByIndex(
                    randomIndex,
                    GameConstants.ENTITY_WIDTH,
                    GameConstants.ENTITY_HEIGHT);
            tank.setPosition(position);
            tanks.add(tank);
            break;
        }
        return tanks;
    }
}
