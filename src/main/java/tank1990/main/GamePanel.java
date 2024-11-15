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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.*;

import tank1990.common.classes.GameEntity;
import tank1990.common.constants.GameConstants;
import tank1990.common.enums.EntityType;
import tank1990.common.utils.CollisionUtil;
import tank1990.manager.GameEntityManager;
import tank1990.manager.MapManager;
import tank1990.manager.PowerUpManager;
import tank1990.manager.spawner.TankSpawner;
import tank1990.objects.environments.Base;
import tank1990.objects.environments.BaseWall;
import tank1990.objects.environments.Environment;
import tank1990.objects.powerups.PowerUp;
import tank1990.objects.tanks.Bullet;
import tank1990.objects.tanks.PlayerTank;
import tank1990.objects.tanks.Tank;

public class GamePanel extends JPanel implements ActionListener {
    static List<Environment> environments = new ArrayList<>();
    static List<PowerUp> powerUps = PowerUpManager.getPowerUps();
    int currentLevel = 1;
    private boolean isGameOver = false;
    private static GamePanel instance;
    private Image gameOverImage;

    GamePanel() {
        this.setPreferredSize(new Dimension(GameConstants.MAP_WIDTH, GameConstants.MAP_HEIGHT));
        this.setBackground(Color.WHITE);
        this.addKeyListener(new TAdapter());
        this.setFocusable(true);
        gameOverImage = new ImageIcon(".\\src\\main\\resources\\images\\game_over.png").getImage();
        setBackground(Color.BLACK);
        environments = MapManager.generateEnvironments(currentLevel);
        TankSpawner.spawnTanks(currentLevel);
        PowerUpManager.startAutoSpawn();
    }
    public static GamePanel getInstance() {
        if (instance == null) {
            instance = new GamePanel();
        }
        return instance;
    }
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

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2D = (Graphics2D) g;
        MapManager.drawTanks(g, this);

        // draw every available power-ups
        if (!powerUps.isEmpty()) {
            Iterator<PowerUp> iterator = powerUps.iterator();
            while (iterator.hasNext()) {
                PowerUp powerUp = iterator.next();
                MapManager.drawPowerUp(powerUp, g, this);
            }
        }

        PlayerTank playerTank = MapManager.getPlayerTank();
        Base base = MapManager.getBase();

        if (playerTank == null) {
            return;
        }

        if (base == null) {
            return;
        }
        // Game Over
        if (playerTank.getHealth() == 0 || (base == null || base.isDestroy())) {
            isGameOver = true;
            TankSpawner.removePlayer();
            TankSpawner.removeEnemies();
            TankSpawner.disableEnemySpawner();
            PowerUpManager.resetPowerUps();
            drawGameOver(g);
            return;
        }


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

        g2D.dispose();
    }
    public void showVictoryDialog() {
        PlayerTank playerTank = MapManager.getPlayerTank();
        String victoryMessage = "<html><h2>Victory!</h2>"
                + "<p>Your Points: " + playerTank.getPoint() + "</p>"
                + "</html>";
        int option = JOptionPane.showOptionDialog(this, victoryMessage, "Victory",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, new Object[]{"OK"}, "OK");

        // Kiểm tra nếu người dùng nhấn "OK"
        if (option == JOptionPane.OK_OPTION) {
            System.exit(0);  // Thoát khỏi ứng dụng
        }    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            PlayerTank playerTank = MapManager.getPlayerTank();
            if (playerTank != null) {
                playerTank.keyReleased(e);

            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            MapManager.getPlayerTank().keyPressed(e);
        }
    }

    public static List<Environment> getEnvironments() {
        return environments;
    }

}