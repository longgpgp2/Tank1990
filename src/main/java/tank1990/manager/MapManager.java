package tank1990.manager;

import tank1990.common.classes.CollisionBox;
import tank1990.common.classes.Vector2D;
import tank1990.common.constants.GameConstants;
import tank1990.common.enums.EntityType;
import tank1990.common.utils.CollisionUtil;
import tank1990.manager.spawner.PerkSpawner;
import tank1990.objects.environments.*;
import tank1990.objects.powerups.PowerUp;
import tank1990.objects.tanks.EnemyTank;
import tank1990.objects.tanks.PlayerTank;
import tank1990.objects.tanks.Tank;
import tank1990.objects.tanks.enemy_tanks.BasicTank;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class MapManager {
    public static Set<Integer> getUnoccupiedIndex(List<Environment> environments, List<Tank> tanks){
//        Need a GameEntity function that checks for the entity's occupied index
        Set<Integer> occupiedIndices = new HashSet<>();
        for (Tank tank:tanks) {
            int index = CollisionUtil.getTileIndex(tank.getPosition());
            occupiedIndices.add(index);
        }
        for (Environment env: environments) {
            int index = CollisionUtil.getTileIndex(env.getPosition());
            occupiedIndices.add(index);
        }
        Set unoccupiedIndices = new HashSet();
        for (int i = 0; i < 16*33; i++) {
            unoccupiedIndices.add(i);
        }
//        System.out.println(occupiedIndices);
        unoccupiedIndices.removeAll(occupiedIndices);
//        System.out.println(unoccupiedIndices);
        return unoccupiedIndices;
    }
    public static PowerUp createPowerUp(List<Environment> environments, List<Tank> tanks){
        Set<Integer> unoccupiedIndices = getUnoccupiedIndex(environments, tanks);
        Integer[] unoccupiedIndicesAsArray = unoccupiedIndices.toArray(new Integer[0]);

        Random random = new Random();
        int randomArrayIndex = random.nextInt(unoccupiedIndicesAsArray.length);
        Integer randomIndex = unoccupiedIndicesAsArray[randomArrayIndex];
        Vector2D powerupPosition = CollisionUtil.getPositionByIndex(randomIndex, GameConstants.ENTITY_WIDTH, GameConstants.ENTITY_HEIGHT);

        randomArrayIndex = random.nextInt(GameConstants.PERK_LIST.length);
        String perkName = GameConstants.PERK_LIST[randomArrayIndex];

        PowerUp powerUp = PerkSpawner.createPowerUp(perkName,powerupPosition);
//        System.out.println(powerUp);
        return powerUp;

    }
    public static void drawPowerUp(PowerUp powerUp, Graphics g, ImageObserver observer){
        g.drawImage(powerUp.image, (int) (powerUp.getPosition().x), (int) (powerUp.getPosition().y), powerUp.width, powerUp.height, observer);
    }
    public static void drawTanks(List<Tank> tanks, Graphics g, ImageObserver observer){
//        for (int i = 0; i < tanks.size(); i++) {
//            System.out.println("Draw a tank: " + tanks.get(i).toString());
//        }
        int i=4;
        for (Tank tank: tanks) {
            i++;
            if(tank.getType()== EntityType.PLAYER) {
                g.setColor(Color.BLUE);
                g.drawImage(tank.image, (int) (tank.getPosition().x), (int) (tank.getPosition().y), tank.width, tank.height, observer);
//                g.drawRect((int)tank.getCollision().x, (int) tank.getCollision().y, tank.getCollision().width, tank.getCollision().height );
////                System.out.println("Player position: "+ tank.getPosition().x+", " + tank.getPosition().y);
//                g.setColor(Color.red);
//                g.drawRect( (int) (tank.getPosition().x), (int) (tank.getPosition().y), tank.width, tank.height);
            }
            else {
                g.setColor(Color.RED);
//                tank.setPosition(new Vector2D(50*i, 10*i));
                g.fillRect( (int) (tank.getPosition().x), (int) (tank.getPosition().y), tank.width, tank.height);
//                System.out.println("Enemy position: "+ tank.getPosition().x+", " + tank.getPosition().y);
            }
        }
    }

    public static PlayerTank getPlayerTank(List<Tank> tanks) {
        for (Tank tank : tanks) {
            if (tank.getType() == EntityType.PLAYER) {
                return (PlayerTank) tank;
            }
        }
        return null;
    }

    public static List<Environment> generateEnvironments(){
        List<Environment> envs = new ArrayList();
        List<Integer> map = readLevel();
        for (int i = 0; i < map.size(); i++) {
            int envX =  (i % 33) *(GameConstants.ENTITY_WIDTH);
            int envY =  (i /33) *(GameConstants.ENTITY_HEIGHT);
//            System.out.println(map);
            if (map.get(i) != 0) {
                Environment env=null;
                switch (map.get(i)){
                    case 1:{
                        env = new BrickWall(envX, envY);
                        break;
                    }
                    case 2:{
                        env = new SteelWall(envX, envY);
                        break;
                    }
                    case 3:{
                        //base
                        break;
                    }
                    case 4:{
                        env = new Water(envX, envY);
                        break;
                    }
                    case 5:{
                        env = new Trees(envX, envY);
                        break;
                    }
                    default:{
                        break;
                    }
                }
                if(env!=null){
                    env.setPosition(new Vector2D(envX, envY));
//                    System.out.println(env.getPosition()+" + "+ env.getCollision());
                    if(!env.crossable)env.setCollision(new CollisionBox(env, new Vector2D(0, 0), env.width, env.height));
                    envs.add(env);
                }

            }

        }

        return envs;
    }
    public static void drawEnvironments(List<Environment> envs, Graphics g, ImageObserver observer){
        for (Environment env : envs) {
            g.setColor(Color.GRAY);
            g.drawImage(env.image, (int) env.getPosition().x, (int) env.getPosition().y, GameConstants.ENTITY_WIDTH, GameConstants.ENTITY_HEIGHT, observer);
        }
    }
    public static List<Integer> readLevel(){
        List map = new ArrayList<Integer>();
        File file = new File(".\\src\\main\\resources\\battlefield.map");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = (br.readLine())) != null) {
                for (int i = 0; i < line.length(); i++) {
                    char c = line.charAt(i);
                    if (Character.isDigit(c)) {
                        map.add(Integer.parseInt(String.valueOf(c)));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return map;
    }
}
