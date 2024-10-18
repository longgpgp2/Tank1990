package tank1990.manager;

import tank1990.common.constants.GameConstants;
import tank1990.common.enums.EntityType;
import tank1990.objects.environments.*;
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
import java.util.ArrayList;
import java.util.List;

public class MapManager {
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
//                System.out.println("Player position: "+ tank.getPosition().x+", " + tank.getPosition().y);
            }
            else {
                g.setColor(Color.RED);
                tank.getPosition().x = 50*i;
                tank.getPosition().y = 10*i;
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
