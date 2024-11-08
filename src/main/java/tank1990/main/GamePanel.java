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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.Timer;

import tank1990.common.classes.GameEntity;
import tank1990.common.constants.GameConstants;
import tank1990.common.enums.EntityType;
import tank1990.common.utils.CollisionUtil;
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
    static List<Environment> environments = new ArrayList<>();
    static List<Tank> tanks;
    static List<PowerUp> powerUps = PowerUpManager.getPowerUps();
    int currentLevel = 3;

    GamePanel() {
        this.setPreferredSize(new Dimension(GameConstants.MAP_WIDTH, GameConstants.MAP_HEIGHT));
        this.setBackground(Color.WHITE);
        this.addKeyListener(new TAdapter());
        this.setFocusable(true);
        setBackground(Color.BLACK);
        environments = MapManager.generateEnvironments(currentLevel);
        tanks = TankSpawner.spawnTanks(currentLevel);
        PowerUpManager.startAutoSpawn();
    }

    /**
     * Cập nhật physic cho từng game entity
     *
     * @param deltaTime khoảng thời gian giữa các tick hoặc giữa các frame
     */

    public void draw() {
        repaint();
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
        if (playerTank == null) {
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