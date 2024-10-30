package tank1990.objects.tanks.enemy_tanks;

import tank1990.common.enums.Direction;
import tank1990.objects.tanks.EnemyTank;

import javax.swing.*;

public class BasicTank extends EnemyTank {
    public BasicTank() {
        super("Basic Tank", 1, 100, 1, 1, "Basic traits");
        loadImages();
    }

    @Override
    protected void loadImages() {
        images[Direction.UP.ordinal()] = new ImageIcon("src/main/resources/images/tank_basic_up.png").getImage();
        images[Direction.DOWN.ordinal()] = new ImageIcon("src/main/resources/images/tank_basic_down.png").getImage();
        images[Direction.LEFT.ordinal()] = new ImageIcon("src/main/resources/images/tank_basic_left.png").getImage();
        images[Direction.RIGHT.ordinal()] = new ImageIcon("src/main/resources/images/tank_basic_right.png").getImage();
    }
}
