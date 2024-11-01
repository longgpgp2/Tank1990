package tank1990.objects.tanks.enemy_tanks;

import tank1990.common.enums.Direction;
import tank1990.objects.tanks.EnemyTank;

import javax.swing.*;
import java.awt.*;

public class BasicTank extends EnemyTank {
    private int frameCounter = 0;
    private final int animationInterval = 10;
    public BasicTank() {
        super("Basic Tank", 1, 100, 1, 60, "Basic traits");
        loadImages();
    }

    @Override
    protected void loadImages() {
        images[Direction.UP.ordinal()][0] =
                new ImageIcon("src/main/resources/images/tank_basic_up.png").getImage();
        images[Direction.UP.ordinal()][1] =
                new ImageIcon("src/main/resources/images/tank_basic_up_c0_t2.png").getImage();

        images[Direction.DOWN.ordinal()][0] =
                new ImageIcon("src/main/resources/images/tank_basic_down.png").getImage();
        images[Direction.DOWN.ordinal()][1] =
                new ImageIcon("src/main/resources/images/tank_basic_down_c0_t2.png").getImage();

        images[Direction.LEFT.ordinal()][0] =
                new ImageIcon("src/main/resources/images/tank_basic_left.png").getImage();
        images[Direction.LEFT.ordinal()][1] =
                new ImageIcon("src/main/resources/images/tank_basic_left_c0_t2.png").getImage();

        images[Direction.RIGHT.ordinal()][0] =
                new ImageIcon("src/main/resources/images/tank_basic_right.png").getImage();
        images[Direction.RIGHT.ordinal()][1] =
                new ImageIcon("src/main/resources/images/tank_basic_right_c0_t2.png").getImage();
    }

    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);

        frameCounter++;
        if (frameCounter >= animationInterval) {
            frameCounter = 0;
        }

        int imageIndex = (frameCounter < animationInterval / 2) ? 0 : 1;
        image = images[currentDirection.ordinal()][imageIndex];
    }
}
