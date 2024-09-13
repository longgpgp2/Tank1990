package tank1990.main;

import javax.swing.*;

public class GameObject extends JFrame {
    GamePanel panel;

    GameObject(){

        panel = new GamePanel();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
