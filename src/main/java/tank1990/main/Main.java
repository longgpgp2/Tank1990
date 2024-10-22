package tank1990.main;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
//        Tank p1Tank = new PlayerTank(1, 2, 2, 2, "YELLOW");
//        Tank enemyBasicTank = new EnemyTank("Basic", 1, 100, 1, 1, 1);
//        System.out.println(p1Tank.toString());
//        p1Tank.shoot();
//        System.out.println(enemyBasicTank.toString());
//        enemyBasicTank.shoot();
        JFrame frame = new JFrame("Tank 1990");
        GameState gameState = GameState.getInstance();
        Menu menu = new Menu(frame);
        frame.add(menu);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }
}