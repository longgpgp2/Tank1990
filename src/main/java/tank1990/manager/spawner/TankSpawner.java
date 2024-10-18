package tank1990.manager.spawner;

import tank1990.objects.tanks.PlayerTank;
import tank1990.objects.tanks.Tank;
import tank1990.objects.tanks.enemy_tanks.BasicTank;

import java.util.ArrayList;
import java.util.List;

public class TankSpawner {
    public static Tank spawnPlayer(){
        Tank player = new PlayerTank(1);
        return player;
    }
    public static List<Tank> spawnEnemy(){
        ArrayList tanks = new ArrayList<Tank>();
        tanks.add(new BasicTank());
        tanks.add(new BasicTank());
        tanks.add(new BasicTank());
        return tanks;
    }
    public static List<Tank> spawnTanks(){
        List tanks = new ArrayList<Tank>();
        List<Tank> enemies = spawnEnemy();
        for (Tank tank: enemies) {
            tanks.add(tank);
        }
        tanks.add(spawnPlayer());
        return tanks;
    }

}
