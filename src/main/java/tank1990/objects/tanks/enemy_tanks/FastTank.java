package tank1990.objects.tanks.enemy_tanks;

import tank1990.objects.tanks.EnemyTank;

import javax.swing.*;

public class FastTank extends EnemyTank {
    public FastTank() {
        super("Fast Tank", 1, 200, 2, 3, "Fast traits");
        image = new ImageIcon("src/main/resources/images/tank_fast.png").getImage();
    }
}
