package tank1990.objects.tanks.enemy_tanks;

import tank1990.objects.tanks.EnemyTank;

import javax.swing.*;

public class ArmorTank extends EnemyTank {
    public ArmorTank() {
        super("Armor Tank", 4, 400, 2, 2, "Armor traits");
        image = new ImageIcon("src/main/resources/images/tank_armor.png").getImage();
    }
}
