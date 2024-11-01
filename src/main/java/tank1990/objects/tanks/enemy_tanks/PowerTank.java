package tank1990.objects.tanks.enemy_tanks;

import tank1990.common.enums.Direction;
import tank1990.objects.tanks.EnemyTank;

import javax.swing.*;

public class PowerTank extends EnemyTank {
    public PowerTank() {
        super("Power Tank", 1, 300, 3, 200, "Power traits");
        loadImages();
    }

    @Override
    protected void loadImages() {
        images[Direction.UP.ordinal()] = new ImageIcon("src/main/resources/images/tank_power_up_c0_t1.png").getImage();
        images[Direction.DOWN.ordinal()] = new ImageIcon("src/main/resources/images/tank_power_down_c0_t1.png")
                .getImage();
        images[Direction.LEFT.ordinal()] = new ImageIcon("src/main/resources/images/tank_power_left_c0_t1.png")
                .getImage();
        images[Direction.RIGHT.ordinal()] = new ImageIcon("src/main/resources/images/tank_power_right_c0_t1.png")
                .getImage();
    }
}
