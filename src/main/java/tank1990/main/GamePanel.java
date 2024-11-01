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
import java.util.ConcurrentModificationException;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import tank1990.common.classes.GameEntity;
import tank1990.common.constants.GameConstants;
import tank1990.manager.GameEntityManager;
import tank1990.manager.MapManager;
import tank1990.manager.PowerUpManager;
import tank1990.manager.spawner.TankSpawner;
import tank1990.objects.environments.Environment;
import tank1990.objects.powerups.PowerUp;
import tank1990.objects.tanks.Bullet;
import tank1990.objects.tanks.PlayerTank;
import tank1990.objects.tanks.Tank;

public class GamePanel extends JPanel implements ActionListener {
    Timer timer;
    static List environments = new ArrayList<Environment>();
    List map = new ArrayList<Integer>();
    static List tanks = new ArrayList<Tank>();
    static List<PowerUp> powerUps = PowerUpManager.getPowerUps();

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

        environments = MapManager.generateEnvironments();
        tanks = TankSpawner.spawnTanks(environments);
        // powerUps.add(MapManager.createPowerUp(environments, tanks));
        startTimer();
        PowerUpManager.startAutoSpawn(environments, tanks);
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
        // ConcurrentModificationException will happen when a GameEntity is removed
        try {
            for (GameEntity gameEntity : gameEntities) {
                gameEntity.update(0.01);
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("An entity is removed.");
        }

        PlayerTank playerTank = MapManager.getPlayerTank(tanks);
        ArrayList<Bullet> bulletsToRemove = new ArrayList<>();

        for (Bullet bullet : playerTank.getBullets()) {
            if (bullet.isCollided() || bullet.isOutOfBound()) {
                bullet.destroyBullet();
                bulletsToRemove.add(bullet);
                // Temporary code for testing. Assume that an enemy is defeated.
                PowerUpManager.addPowerUp(environments, tanks);
            }
        }

        for (Bullet bullet : bulletsToRemove) {
            if (bullet.isExplosionFinished()) {
                playerTank.getBullets().remove(bullet);
            }
        }

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        MapManager.drawTanks(tanks, g, this);
        MapManager.drawEnvironments(environments, g, this);

        // draw every available power-ups
        if (!powerUps.isEmpty()) {
            for (PowerUp powerUp : powerUps) {
                MapManager.drawPowerUp(powerUp, g, this);
            }
        }

        PlayerTank playerTank = MapManager.getPlayerTank(tanks);
        playerTank.draw(g);
        for (Bullet bullet : playerTank.getBullets()) {
            bullet.draw(g); // Vẽ viên đạn và vụ nổ nếu có
        }

        try {
            for (GameEntity gameEntity : GameEntityManager.getGameEntities()) {
                gameEntity.draw((Graphics2D) g);
            }
        } catch (Exception e) {
            // TODO: handle exception
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

    public static List<Environment> getEnvironments() {
        return environments;
    }

    public static void removeEntity(GameEntity gameEntity) {
        switch (gameEntity.getType()) {
            case POWER_UP:
                powerUps.remove((PowerUp) gameEntity);
                break;
            default:
                break;
        }
    }
}