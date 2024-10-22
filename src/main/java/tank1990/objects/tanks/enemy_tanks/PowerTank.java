package tank1990.objects.tanks.enemy_tanks;

import tank1990.objects.tanks.EnemyTank;

import javax.swing.*;

public class PowerTank extends EnemyTank {
    public PowerTank() {
        super("Power Tank", 1, 300, 3, 2, "Power traits");
        image = new ImageIcon("src/main/resources/images/tank_power.png").getImage();
    }
}
