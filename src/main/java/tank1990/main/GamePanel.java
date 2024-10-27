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
import tank1990.manager.spawner.TankSpawner;
import tank1990.objects.environments.Environment;
import tank1990.objects.powerups.PowerUp;
import tank1990.objects.powerups.Tank;
import tank1990.objects.tanks.Bullet;
import tank1990.objects.tanks.PlayerTank;

public class GamePanel extends JPanel implements ActionListener {
    Timer timer;
    List environments = new ArrayList<Environment>();
    List map = new ArrayList<Integer>();
    List tanks = new ArrayList<Tank>();
    static List<PowerUp> powerUps= new ArrayList<PowerUp>();


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
//        powerUps.add(MapManager.createPowerUp(environments, tanks));
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
        // ConcurrentModificationException will happen when a GameEntity is removed
        try {
            for (GameEntity gameEntity : gameEntities) {
                gameEntity.update(0.01);
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("An entity is removed.");
            return;
        }

        PlayerTank playerTank = MapManager.getPlayerTank(tanks);
        ArrayList<Bullet> bulletsToRemove = new ArrayList<>(); // Lưu trữ đạn để xóa

        for (Bullet bullet : playerTank.getBullets()) {
//            bullet.update(); // Cập nhật vị trí viên đạn

            // Kiểm tra nếu đạn đã bị tiêu diệt hoặc ra ngoài biên
            if (bullet.isCollided() || bullet.isOutOfBound()) {
                bullet.destroyBullet(); // Tạo vụ nổ
                bulletsToRemove.add(bullet); // Đánh dấu viên đạn để xóa
            }
        }

        // Xóa đạn sau khi vụ nổ đã được tạo
        for (Bullet bullet : bulletsToRemove) {
            if (bullet.isExplosionFinished()) { // Kiểm tra nếu vụ nổ đã hoàn tất
                playerTank.getBullets().remove(bullet); // Xóa viên đạn khi vụ nổ hoàn tất
            }

            // Temporary code for testing. Assume that an enemy is defeated.
            if (powerUps.size() < 3) { // Limit the number of power-up can be on the field
                PowerUp powerUp = MapManager.createPowerUp(environments, tanks);
                powerUps.add(powerUp);
                System.out.println("Add a power-up");
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        MapManager.drawTanks(tanks, g, this);
        MapManager.drawEnvironments(environments, g, this);
//        if(powerUps.isEmpty()){
//            PowerUp powerUp = MapManager.createPowerUp(environments, tanks);
//            powerUps.add(powerUp);
//            System.out.println("Powerup: "+powerUp.getPosition());
//            System.out.println(CollisionUtil.getTileIndex(powerUp.getPosition()));
//            MapManager.drawPowerUp((PowerUp) powerUps.get(0), g, this);
////            System.out.println(CollisionUtil.getTileIndex(new Vector2D(512, 512)));
//        } else MapManager.drawPowerUp((PowerUp) powerUps.get(0), g, this);

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