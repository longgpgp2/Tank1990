package tank1990.main;

import java.awt.*;
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
import java.util.Set;

import javax.swing.*;

import tank1990.common.classes.GameEntity;
import tank1990.common.constants.GameConstants;
import tank1990.common.utils.CollisionUtil;
import tank1990.manager.GameEntityManager;
import tank1990.manager.MapManager;
import tank1990.manager.PowerUpManager;
import tank1990.manager.SoundManager;
import tank1990.manager.spawner.TankSpawner;
import tank1990.objects.environments.Environment;
import tank1990.objects.powerups.PowerUp;
import tank1990.objects.tanks.Bullet;
import tank1990.objects.tanks.PlayerTank;
import tank1990.objects.tanks.Tank;

public class GamePanel extends JPanel implements ActionListener {
    Timer timer;
    static List<Environment> environments = new ArrayList<>();
    static List<Tank> tanks = new ArrayList<>();
    static List<PowerUp> powerUps = PowerUpManager.getPowerUps();
    int currentLevel = 1;
    private boolean isGameOver = false;
    private Image gameOverImage;
    GamePanel() {
        this.setPreferredSize(new Dimension(GameConstants.MAP_WIDTH, GameConstants.MAP_HEIGHT));
        this.setBackground(Color.WHITE);
        this.addKeyListener(new TAdapter());
        this.setFocusable(true);
        setBackground(Color.BLACK);
        gameOverImage = new ImageIcon(".\\src\\main\\resources\\images\\game_over.png").getImage();
        environments = MapManager.generateEnvironments(currentLevel);
        tanks = TankSpawner.spawnTanks(currentLevel);
        // powerUps.add(MapManager.createPowerUp(environments, tanks));
        // startTimer();
        // start a timer that spawn a power-up occasionally
        PowerUpManager.startAutoSpawn();
    }

    ArrayList<GameEntity> gameEntities = GameEntityManager.getGameEntities();

    /**
     * Cập nhật physic cho từng game entity
     *
     * @param: deltaTime khoảng thời gian giữa các tick hoặc giữa các frame
     */

    public void draw() {
        repaint();
    }

    public void drawGameOver(Graphics g) {
        if (isGameOver) {
            // Vẽ hình ảnh Game Over ở giữa màn hình
            int x = (getWidth() - gameOverImage.getWidth(null)) / 2;
            int y = (getHeight() - gameOverImage.getHeight(null)) / 2;
            g.drawImage(gameOverImage, x, y, null);
        }
    }
    public void setGameOver(boolean gameOver) {
        this.isGameOver = gameOver;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
//        if (TankSpawner.checkVictory(tanks)) {
//            setGameOver(true);
//            drawGameOver(g);
//            System.out.println("Victory");
//        }
        Graphics2D g2D = (Graphics2D) g;
        MapManager.drawTanks(g, this);
        MapManager.drawEnvironments(g, this);

        // draw every available power-ups
        if (!powerUps.isEmpty()) {
            for (PowerUp powerUp : powerUps) {
                MapManager.drawPowerUp(powerUp, g, this);
            }
        }

        PlayerTank playerTank = MapManager.getPlayerTank();
        // playerTank.draw(g);
        for (Bullet bullet : playerTank.getBullets()) {
            bullet.draw((Graphics2D) g); // Vẽ viên đạn và vụ nổ nếu có

        }

        try {
            for (GameEntity gameEntity : GameEntityManager.getGameEntities()) {
                gameEntity.draw((Graphics2D) g);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        drawGameOver(g);
        g2D.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        PlayerTank playerTank = MapManager.getPlayerTank();  // Lấy PlayerTank từ MapManager

        if (playerTank.getHealth() <= 0) {
            // Khi PlayerTank có health <= 0, gọi destroy và đặt game over
            playerTank.destroy();  // Phương thức destroy trong PlayerTank sẽ xử lý logic phá hủy tank
            setGameOver(true);      // Đặt game over
        }
        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            MapManager.getPlayerTank().keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            MapManager.getPlayerTank().keyPressed(e);
        }
    }

    public static List<Environment> getEnvironments() {
        return environments;
    }

    public static List<Tank> getTanks() {
        return tanks;
    }

}