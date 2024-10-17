package tank1990.main;

import javax.swing.*;

import tank1990.common.constants.GameConstants;

import java.awt.*;

public class GameObject extends JFrame {
    JPanel panel;

    GameObject() {

        panel = new GamePanel();
        setSize(new Dimension(GameConstants.FRAME_WIDTH, GameConstants.FRAME_HEIGHT));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
