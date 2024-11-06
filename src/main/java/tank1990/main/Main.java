package tank1990.main;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
            JFrame frame = new JFrame("Tank 1990");
            Menu menu = new Menu(frame);
            frame.add(menu);
            frame.setSize(800, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

        }
    }
