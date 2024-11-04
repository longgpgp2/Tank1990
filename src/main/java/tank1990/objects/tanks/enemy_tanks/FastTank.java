package tank1990.objects.tanks.enemy_tanks;

import tank1990.common.enums.Direction;
import tank1990.objects.tanks.EnemyTank;

import javax.swing.*;
import java.awt.*;

public class FastTank extends EnemyTank {
        private int frameCounter = 0;
        private final int animationInterval = 10;

        public FastTank() {
                super("Fast Tank", 1, 200, 2, 80, "Fast traits");
                loadImages();
        }

        @Override
        protected void loadImages() {
                images[Direction.UP.ordinal()][0] = new ImageIcon("src/main/resources/images/tank_fast_up_c0_t1.png")
                                .getImage();
                images[Direction.UP.ordinal()][1] = new ImageIcon("src/main/resources/images/tank_fast_up_c0_t2.png")
                                .getImage();

                images[Direction.DOWN.ordinal()][0] = new ImageIcon(
                                "src/main/resources/images/tank_fast_down_c0_t1.png").getImage();
                images[Direction.DOWN.ordinal()][1] = new ImageIcon(
                                "src/main/resources/images/tank_fast_down_c0_t2.png").getImage();

                images[Direction.LEFT.ordinal()][0] = new ImageIcon(
                                "src/main/resources/images/tank_fast_left_c0_t1.png").getImage();
                images[Direction.LEFT.ordinal()][1] = new ImageIcon(
                                "src/main/resources/images/tank_fast_left_c0_t2.png").getImage();

                images[Direction.RIGHT.ordinal()][0] = new ImageIcon(
                                "src/main/resources/images/tank_fast_right_c0_t1.png").getImage();
                images[Direction.RIGHT.ordinal()][1] = new ImageIcon(
                                "src/main/resources/images/tank_fast_right_c0_t2.png").getImage();
        }

        @Override
        public void update(double deltaTime) {
                if (isAppearing()) {
                        return;
                }
                super.update(deltaTime);

                frameCounter++;
                if (frameCounter >= animationInterval) {
                        frameCounter = 0;
                }

                int imageIndex = (frameCounter < animationInterval / 2) ? 0 : 1;
                image = images[direction.ordinal()][imageIndex];
        }

}
