package tank1990.objects.tanks.enemy_tanks;

import tank1990.objects.tanks.EnemyTank;

import javax.swing.*;

public class BasicTank extends EnemyTank {

    public BasicTank() {
        super("Basic Tank", 1, 100, 1, 1, "Basic traits");
        image = new ImageIcon("src/main/resources/images/tank_basic.png").getImage();
    }
}
