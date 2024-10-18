package tank1990.main;

import tank1990.common.classes.GameEntity;
import tank1990.common.constants.GameConstants;
import tank1990.manager.GameEntityManager;
import tank1990.manager.KeyHandler;
import tank1990.objects.environments.Environment;
import tank1990.objects.tanks.Tank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class GamePanel extends JPanel {
    Timer timer;
    java.util.List<Environment> environments = new ArrayList<>();
    private java.util.List<Integer> map = new ArrayList<Integer>();

    GamePanel() {

        File file = new File(".\\src\\main\\resources\\battlefield.map");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = (br.readLine())) != null) {
                for (int i = 0; i < line.length(); i++) {
                    char c = line.charAt(i);
                    if (Character.isDigit(c)) {
                        map.add(Integer.parseInt(String.valueOf(c)));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.setPreferredSize(new Dimension(GameConstants.MAP_WIDTH, GameConstants.MAP_HEIGHT));
        this.setBackground(Color.WHITE);
        this.addKeyListener(new KeyHandler());
        this.setFocusable(true);
        startTimer();

    }

    public void startTimer() {
        timer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGame();
                repaint();
            }
        });
        timer.start();
    }

    ArrayList<GameEntity> gameEntities = GameEntityManager.getGameEntities();

    /**
     * Cập nhật physic cho từng game entity
     * 
     * @param deltaTime khoảng thời gian giữa các tick hoặc giữa các frame
     */

    private void updateGame() {
        for (GameEntity gameEntity : gameEntities) {
            gameEntity.update(0.1);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.dispose();
    }

}