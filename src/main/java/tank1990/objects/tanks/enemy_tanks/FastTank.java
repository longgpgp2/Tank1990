package tank1990.objects.tanks.enemy_tanks;

import tank1990.common.enums.Direction;
import tank1990.objects.tanks.EnemyTank;

import javax.swing.*;

public class FastTank extends EnemyTank {
    public FastTank() {
        super("Fast Tank", 1, 200, 2, 260, "Fast traits");
        loadImages();
    }

    @Override
    protected void loadImages() {
        images[Direction.UP.ordinal()] = new ImageIcon("src/main/resources/images/tank_fast_up_c0_t1.png").getImage();
        images[Direction.DOWN.ordinal()] = new ImageIcon("src/main/resources/images/tank_fast_down_c0_t1.png")
                .getImage();
        images[Direction.LEFT.ordinal()] = new ImageIcon("src/main/resources/images/tank_fast_left_c0_t1.png")
                .getImage();
        images[Direction.RIGHT.ordinal()] = new ImageIcon("src/main/resources/images/tank_fast_right_c0_t1.png")
                .getImage();
    }
}
