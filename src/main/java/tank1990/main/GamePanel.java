package tank1990.main;

import tank1990.objects.environments.BrickWall;
import tank1990.objects.environments.Environment;
import tank1990.objects.tanks.Bullet;
import tank1990.objects.tanks.Direction;
import tank1990.objects.tanks.PlayerTank;
import tank1990.objects.tanks.Tank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class GamePanel extends JPanel {

    final int PANEL_WIDTH = 700;
    final int PANEL_HEIGHT = 700;
    String tankImageSrc;
    Timer timer;
    int velocity = 5;
    int x = 200;
    int y = 100;
    java.util.List<Environment> environments = new ArrayList<>();
    PlayerTank p1Tank = new PlayerTank(1);
    boolean upPressed, downPressed, leftPressed, rightPressed, shoot;

    GamePanel() {
        for (int i = 0; i < 10; i++) {
            environments.add(new BrickWall(i*25, i*25));
        }

        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setBackground(Color.WHITE);
        this.addKeyListener(new KeyHandler());
        this.setFocusable(true);

        // Initialize the timer for 60 FPS
        timer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGame();
                repaint();
            }
        });
        timer.start();
    }
    private void updateBullets(){
        if(!p1Tank.getBullets().isEmpty())
            for (Bullet bullet : p1Tank.getBullets()) {
                bullet.update();
                System.out.println(p1Tank.getBullets().size());
                checkCollisions(bullet);
                if (bullet.checkBulletOutOfBound() || bullet.isCollided()) {
                    java.util.List<Bullet> bullets = p1Tank.getBullets();
                    bullets.remove(bullet);
                    p1Tank.setBullets(bullets);
                    updateBullets();
                    break;
                }
            }
    }
    private void updateGame() {
        Set<String> blockedDirections = checkTankCollisions();

        if (upPressed) {
            if(checkTankOutOfBound()!="UP" && !blockedDirections.contains("UP"))
            y -= velocity;
            else System.out.println(blockedDirections);
        }
        if (downPressed) {
            if(checkTankOutOfBound()!="DOWN" && !blockedDirections.contains("DOWN"))
            y += velocity;
            else System.out.println(blockedDirections);
        }
        if (leftPressed) {
            if(checkTankOutOfBound()!="LEFT" && !blockedDirections.contains("LEFT"))
            x -= velocity;
            else System.out.println(blockedDirections);
        }
        if (rightPressed) {
            if(checkTankOutOfBound()!="RIGHT" && !blockedDirections.contains("RIGHT"))
            x += velocity;
            else System.out.println(blockedDirections);
        }

        p1Tank.setX(x);
        p1Tank.setY(y);
        updateBullets();

    }

    private String checkTankOutOfBound(){
        int x = p1Tank.getX();
        int y = p1Tank.getY();
        if (x<=0) return "LEFT";
        if (x>=700-26) return "RIGHT";
        if (y<=0) return "UP";
        if (y>=700-26) return "DOWN";
        return "";
    }
    private Set<String> checkTankCollisions() {
        Set<String> blockedDirections = new HashSet<>();
        for (Environment env : environments) {
            if(!isTankColliding(env).isEmpty())
            {
                for (String s: isTankColliding(env)) {
                    blockedDirections.add(s);
                }
            }

        }
        return blockedDirections;
    }

    private boolean checkCollisions(Bullet bullet) {
            for (Environment env : environments) {

                if (isColliding(bullet, env)) {
                    bullet.setCollided(true);
                    return true;
                }
            }
        return false;
    }

    private Set<String> isTankColliding(Environment env) {
        Set<String> blockedDirections = new HashSet<>();
        int tankX = p1Tank.getX();
        int tankY = p1Tank.getY();

        int envX = env.getX();
        int envY = env.getY();

        if(envX-tankX<=20&&envX-tankX>=0){

            if (envY - tankY <= 20 && envY - tankY >= 0){
                blockedDirections.add("RIGHT");
                blockedDirections.add("DOWN");
                if (envY - tankY < 20 && envY - tankY > 0)
                    blockedDirections.remove("DOWN");
            }
            if (tankY - envY <= 25 && tankY - envY  >= 0){
                blockedDirections.add("UP");
                blockedDirections.add("RIGHT");
                if (tankY - envY < 25 && tankY - envY  > 0)
                    blockedDirections.remove("UP");
            }
        }

        if(tankX - envX<=25&&tankX - envX>=0){

            if (envY - tankY <= 20 && envY - tankY >= 0){
                blockedDirections.add("LEFT");
                blockedDirections.add("DOWN");
            }
            if (tankY - envY <= 25 && tankY - envY  >= 0){
                blockedDirections.add("UP");
                blockedDirections.add("LEFT");
            }

        }
        return blockedDirections;
    }
    private boolean isColliding(Bullet bullet, Environment env) {
        int bulletX = (int) bullet.getX();
        int bulletY = (int) bullet.getY();

        int envX = env.getX();
        int envY = env.getY();
        int envWidth = 25;
        int envHeight = 25;

        return bulletX < envX + envWidth &&
                bulletX + 10 > envX &&
                bulletY < envY + envHeight &&
                bulletY + 10 > envY;
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image tankImage = new ImageIcon(tankImageSrc).getImage();
        Graphics2D g2D = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();

        // Draw vertical lines (columns)
        for (int i = 0; i <= width; i += 25) {
            g.drawLine(i, 0, i, height);
        }
        // Draw horizontal lines (rows)
        for (int i = 0; i <= height; i += 25) {
            g.drawLine(0, i, width, i);
        }

        g2D.setColor(p1Tank.getColor());
        g2D.drawImage(tankImage, p1Tank.getX(), p1Tank.getY(), 20, 20, this);
        for (Environment env : environments) {

            g.setColor(Color.GRAY);
            g.fillRect(env.getX(), env.getY(), 25, 25); // Placeholder
        }
        if(!p1Tank.getBullets().isEmpty())
        for (Bullet bullet : p1Tank.getBullets()) {
            bullet.draw(g);
        }

        g2D.dispose();
    }

    private class KeyHandler implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            int code = e.getKeyCode();
            if (code == KeyEvent.VK_W) {
                upPressed = true;
                p1Tank.setDirection(Direction.UP);
            }
            if (code == KeyEvent.VK_A) {
                leftPressed = true;
                p1Tank.setDirection(Direction.LEFT);
            }
            if (code == KeyEvent.VK_S) {
                downPressed = true;
                p1Tank.setDirection(Direction.DOWN);
            }
            if (code == KeyEvent.VK_D) {
                rightPressed = true;
                p1Tank.setDirection(Direction.RIGHT);
            }
            if (code == KeyEvent.VK_L){
                shoot=true;
                p1Tank.setBulletCount(3);
                if(p1Tank.getBullets().size()<p1Tank.getBulletCount())
                {
                    p1Tank.shoot();
                }
            }
            tankImageSrc = ".\\src\\tank" + p1Tank.getDirection() + ".png";
        }

        @Override
        public void keyReleased(KeyEvent e) {
            int code = e.getKeyCode();
            if (code == KeyEvent.VK_W) {
                upPressed = false;
            }
            if (code == KeyEvent.VK_A) {
                leftPressed = false;
            }
            if (code == KeyEvent.VK_S) {
                downPressed = false;
            }
            if (code == KeyEvent.VK_D) {
                rightPressed = false;
            }
            if (code == KeyEvent.VK_L){
                shoot=false;
            }
        }
    }

}