package tank1990.manager;

import tank1990.common.enums.EntityType;
import tank1990.objects.tanks.EnemyTank;
import tank1990.objects.tanks.PlayerTank;
import tank1990.objects.tanks.Tank;
import tank1990.objects.tanks.enemy_tanks.BasicTank;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MapManager {
    public static void drawTanks(List<Tank> tanks, Graphics g){
//        for (int i = 0; i < tanks.size(); i++) {
//            System.out.println("Draw a tank: " + tanks.get(i).toString());
//        }
        int i=0;
        for (Tank tank: tanks) {
            i++;
            if(tank.getType()== EntityType.PLAYER)
                g.setColor(Color.BLUE);
            else g.setColor(Color.RED);
            g.fillRect(tank.x+i*50, tank.y+i*50, tank.width, tank.height);
        }
    }
}
