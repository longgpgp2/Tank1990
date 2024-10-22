package tank1990.manager.spawner;

import tank1990.common.classes.Vector2D;
import tank1990.common.constants.GameConstants;
import tank1990.common.enums.EntityType;
import tank1990.common.utils.CollisionUtil;
import tank1990.manager.GameEntityManager;
import tank1990.manager.MapManager;
import tank1990.objects.environments.Environment;
import tank1990.objects.powerups.PowerUp;
import tank1990.objects.tanks.PlayerTank;
import tank1990.objects.tanks.Tank;
import tank1990.objects.tanks.enemy_tanks.BasicTank;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.*;

public class TankSpawner {
    public static Tank spawnPlayer(){
        Tank player = new PlayerTank(1,5);
        player.setPosition(new Vector2D(100, 100));
        return player;
    }
    public static List<Tank> spawnEnemy(Set<Integer> unoccupiedIndices){
//        System.out.println(unoccupiedIndices);
        List tanks = new ArrayList<Tank>();
        String[] types = {"BasicTank", "FastTank", "ArmorTank", "PowerTank"};
        for (String type: types) {
            tanks = addAnEnemy(unoccupiedIndices,tanks, type );
        }
        return tanks;
    }
    public static List<Tank> addAnEnemy(Set<Integer> unoccupiedIndices, List<Tank> tanks, String enemyType){
        Set<Integer> tanksIndices = new HashSet<>();
        if(!tanks.isEmpty()){
            for (Tank tank:tanks) {
                tanksIndices.add(CollisionUtil.getTileIndex(tank.getPosition()));
            }
        }
        System.out.println("tanks indices: "+ tanksIndices);
        unoccupiedIndices.removeAll(tanksIndices);
        System.out.println(unoccupiedIndices.containsAll(tanksIndices));
        Tank tank = createEnemyTankByType(enemyType);
        while(true){
            Random random = new Random();
            int randomIndex = random.nextInt(34,66);
            if(!MapManager.checkIndexAvailability(randomIndex, unoccupiedIndices)) continue;
            Vector2D position = CollisionUtil.getPositionByIndex(randomIndex, GameConstants.ENTITY_WIDTH, GameConstants.ENTITY_HEIGHT);
            tank.setPosition(position);
            System.out.println(position);
            System.out.println(tank);
            tanks.add(tank);
            break;
        }
        return tanks;
    }
    public static List<Tank> spawnTanks(List<Environment> environments){
        Set<Integer>unoccupiedIndices = MapManager.getUnoccupiedIndex(environments, null);
        List tanks = new ArrayList<Tank>();
        List<Tank> enemies = spawnEnemy(unoccupiedIndices);
        for (Tank tank: enemies) {
            tanks.add(tank);
        }
        tanks.add(spawnPlayer());

        return tanks;
    }


    public static Tank createEnemyTankByType(String className){
        try {
            String classPath = "tank1990.objects.tanks.enemy_tanks."+className;
            Class<?> clazz = Class.forName(classPath);
            System.out.println(clazz.getName());
            Constructor<?> constructor = clazz.getConstructor();
//            System.out.println(constructor.getName());
            return (Tank) constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

