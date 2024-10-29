package tank1990.objects.tanks.enemy_tanks;

import tank1990.common.enums.Direction;
import tank1990.objects.tanks.EnemyTank;

import javax.swing.*;

public class ArmorTank extends EnemyTank {
    public ArmorTank() {
        super("Armor Tank", 4, 400, 2, 2, "Armor traits");
        loadImages();}

    @Override
    protected void loadImages() {
        images[Direction.UP.ordinal()] = new ImageIcon("src/main/resources/images/tank_armor_up_c0_t1.png").getImage();
        images[Direction.DOWN.ordinal()] = new ImageIcon("src/main/resources/images/tank_armor_down_c0_t1.png").getImage();
        images[Direction.LEFT.ordinal()] = new ImageIcon("src/main/resources/images/tank_armor_left_c0_t1.png").getImage();
        images[Direction.RIGHT.ordinal()] = new ImageIcon("src/main/resources/images/tank_armor_right_c0_t1.png").getImage();
    }
}
