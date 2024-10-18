package tank1990.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

import tank1990.common.classes.GameEntity;
import tank1990.common.constants.GameConstants;
import tank1990.manager.GameEntityManager;
import tank1990.manager.MapManager;
import tank1990.manager.spawner.TankSpawner;
import tank1990.objects.environments.Environment;
import tank1990.objects.tanks.Bullet;
import tank1990.objects.tanks.PlayerTank;

public class GamePanel extends JPanel implements ActionListener {
    Timer timer;
    java.util.List environments = new ArrayList<Environment>();
    java.util.List map = new ArrayList<Integer>();
    java.util.List tanks;

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
        this.addKeyListener(new TAdapter());
        this.setFocusable(true);
        setBackground(Color.BLACK);

        tanks = TankSpawner.spawnTanks();
        environments = MapManager.generateEnvironments();
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
            gameEntity.update(0.01);
        }
        PlayerTank playerTank = MapManager.getPlayerTank(tanks);
        for (Bullet bullet : playerTank.getBullets()) {
            bullet.update(0.01); // Cập nhật vị trí viên đạn
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        MapManager.drawTanks(tanks, g, this);
        MapManager.drawEnvironments(environments, g, this);

        PlayerTank playerTank = MapManager.getPlayerTank(tanks);
        for (Bullet bullet : playerTank.getBullets()) {
            bullet.draw(g);
        }
        g2D.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            MapManager.getPlayerTank(tanks).keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            MapManager.getPlayerTank(tanks).keyPressed(e);
        }
    }
}