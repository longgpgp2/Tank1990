package tank1990.main;

import tank1990.common.enums.Direction;
import tank1990.objects.environments.BrickWall;
import tank1990.objects.environments.Environment;
import tank1990.objects.tanks.Bullet;
import tank1990.objects.tanks.PlayerTank;
import tank1990.objects.tanks.Tank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class GamePanelcopy extends JPanel {

    public static final int GAME_SIZE = 400;
    public static final int PANEL_WIDTH = 700;
    public static final int PANEL_HEIGHT = 700;
    public static final int BRICK_WIDTH = (int) (PANEL_WIDTH / (Math.sqrt(GAME_SIZE)));
    public static final int BRICK_HEIGHT = (int) (PANEL_HEIGHT / (Math.sqrt(GAME_SIZE)));
    String p1TankImageSrc, p2TankImageSrc;
    Timer timer;
    int velocity = BRICK_HEIGHT / 5;
    int x = BRICK_HEIGHT * 10;
    int y = BRICK_HEIGHT * 10;
    int x2 = 400;
    int y2 = 500;
    java.util.List<Environment> environments = new ArrayList<>();
    private java.util.List<Integer> map = new ArrayList<Integer>();
    Tank p1Tank, p2Tank;
    boolean upPressed, downPressed, leftPressed, rightPressed, shoot, p2UpPressed, p2DownPressed, p2LeftPressed,
            p2RightPressed, p2Shoot;

    GamePanelcopy() {

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
        System.out.println(map);

        for (int i = 0; i < map.size(); i++) {
            int envX = (int) (i % (Math.sqrt(GAME_SIZE)));
            int envY = (int) (i / (Math.sqrt(GAME_SIZE)));
            if (map.get(i) == 1)
                environments.add(new BrickWall(envX * BRICK_WIDTH, envY * BRICK_HEIGHT));
            else
                environments.add(new BrickWall(envX * BRICK_WIDTH / 2, envY * BRICK_HEIGHT / 2));
        }
        initPlayerTank(true);
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

    private void updateGame() {

    }

    private void initPlayerTank(boolean p2Present) {
        p1Tank = new PlayerTank(1);
        p1Tank.setDirection(Direction.UP);
        if (p2Present) {
            p2Tank = new PlayerTank(2);
            p2Tank.setDirection(Direction.UP);
        }
    }

    private String checkTankOutOfBound(Tank tank) {
        int x = tank.getX();
        int y = tank.getY();
        if (x <= 0)
            return "LEFT";
        if (x >= 700 - 20)
            return "RIGHT";
        if (y <= 0)
            return "UP";
        if (y >= 700 - 20)
            return "DOWN";
        return "";
    }

    private Set<String> checkTankCollisions(Tank tank) {
        Set<String> blockedDirections = new HashSet<>();
        for (Environment env : environments) {
            if (!isTankColliding(env, tank).isEmpty()) {
                for (String s : isTankColliding(env, tank)) {
                    blockedDirections.add(s);
                }
            }

        }
        return blockedDirections;
    }

    private boolean checkCollisions(Bullet bullet) {
        for (Environment env : environments) {
            if (isColliding(bullet, env)) {
                bullet.isCollided = true;
                return true;
            }
        }
        if (isTankShot(bullet, p1Tank)) {
            bullet.isCollided = true;
            p1Tank.setHealth(p1Tank.getHealth() - 1);
            if (p2Tank.getHealth() <= 0) {
                System.out.println("Player 2 Win");
                p1TankImageSrc = null;
            }
        }
        if (isTankShot(bullet, p2Tank)) {
            bullet.isCollided = true;
            p2Tank.setHealth(p2Tank.getHealth() - 1);
            if (p2Tank.getHealth() <= 0) {
                System.out.println("Player 1 Win");
                p2TankImageSrc = null;
            }
        }
        return false;
    }

    private Set<String> isTankColliding(Environment env, Tank tank) {
        Set<String> blockedDirections = new HashSet<>();
        boolean up = false;
        boolean down = false;
        boolean left = false;
        boolean right = false;

        int tankX = tank.getX();
        int tankY = tank.getY();

        int envX = env.x;
        int envY = env.y;

        boolean inHorizontalRange = (envY - tankY <= BRICK_HEIGHT && envY - tankY >= 0)
                || (tankY - envY <= BRICK_WIDTH && tankY - envY >= 0);
        boolean inVerticalRange = (envX - tankX <= BRICK_HEIGHT && envX - tankX >= 0)
                || (tankX - envX <= BRICK_WIDTH && tankX - envX >= 0);

        if (envX - tankX <= BRICK_WIDTH && envX - tankX >= 0) {
            if (inHorizontalRange) {
                blockedDirections.add("RIGHT");
            }
        }
        if (tankX - envX <= BRICK_WIDTH && tankX - envX >= 0) {
            if (inHorizontalRange) {
                blockedDirections.add("LEFT");
            }
        }
        if (envY - tankY <= BRICK_WIDTH && envY - tankY >= 0)
            if (inVerticalRange) {
                blockedDirections.add("DOWN");
            }
        if (tankY - envY <= BRICK_WIDTH && tankY - envY >= 0)
            if (inVerticalRange) {
                blockedDirections.add("UP");
            }

        if (envX >= tankX + BRICK_WIDTH) {
            right = true;
            blockedDirections.remove("UP");
            blockedDirections.remove("DOWN");
        }
        if (envY >= tankY + BRICK_HEIGHT) {
            down = true;
            blockedDirections.remove("LEFT");
            blockedDirections.remove("RIGHT");
        }
        if (envX + BRICK_WIDTH <= tankX) {
            left = true;
            blockedDirections.remove("UP");
            blockedDirections.remove("DOWN");
        }
        if (envY + BRICK_HEIGHT <= tankY) {
            up = true;
            blockedDirections.remove("LEFT");
            blockedDirections.remove("RIGHT");
        }

        return blockedDirections;
    }

    private boolean isColliding(Bullet bullet, Environment env) {
        int bulletX = (int) bullet.x;
        int bulletY = (int) bullet.y;

        int envX = env.x;
        int envY = env.y;
        int envWidth = 25;
        int envHeight = 25;

        return bulletX < envX + envWidth &&
                bulletX + 10 > envX &&
                bulletY < envY + envHeight &&
                bulletY + 10 > envY;
    }

    private boolean isTankShot(Bullet bullet, Tank tank) {
        int bulletX = (int) bullet.x;
        int bulletY = (int) bullet.y;

        int tankX = tank.getX();
        int tankY = tank.getY();
        int tankWidth = BRICK_WIDTH - 10;
        int tankHeight = BRICK_HEIGHT - 10;

        return bulletX < tankX + tankWidth &&
                bulletX + 10 > tankX &&
                bulletY < tankY + tankHeight &&
                bulletY + 10 > tankY;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image p1TankImage = new ImageIcon(p1TankImageSrc).getImage();
        Image p2TankImage = new ImageIcon(p2TankImageSrc).getImage();
        Graphics2D g2D = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();

        // Draw vertical lines (columns)
        for (int i = 0; i <= width; i += BRICK_WIDTH) {
            g.drawLine(i, 0, i, height);
        }
        // Draw horizontal lines (rows)
        for (int i = 0; i <= height; i += BRICK_HEIGHT) {
            g.drawLine(0, i, width, i);
        }

        g2D.setColor(p1Tank.getColor());
        g2D.drawImage(p1TankImage, p1Tank.getX(), p1Tank.getY(), BRICK_WIDTH, BRICK_HEIGHT, this);
        g2D.drawImage(p2TankImage, p2Tank.getX(), p2Tank.getY(), BRICK_WIDTH, BRICK_HEIGHT, this);
        for (Environment env : environments) {

            g.setColor(Color.GRAY);
            g.fillRect(env.x, env.y, BRICK_WIDTH, BRICK_HEIGHT); // Placeholder
        }
        if (!p1Tank.getBullets().isEmpty())
            for (Bullet bullet : p1Tank.getBullets()) {
                bullet.draw(g);
            }

        if (!p2Tank.getBullets().isEmpty())
            for (Bullet bullet : p2Tank.getBullets()) {
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
            if (code == KeyEvent.VK_L) {
                shoot = true;
                p1Tank.setBulletCount(3);
                if (p1Tank.getBullets().size() < p1Tank.getBulletCount()) {
                    p1Tank.shoot();
                }
            }

            // p2 keys
            if (code == KeyEvent.VK_UP) {
                p2UpPressed = true;
                p2Tank.setDirection(Direction.UP);
            }
            if (code == KeyEvent.VK_LEFT) {
                p2LeftPressed = true;
                p2Tank.setDirection(Direction.LEFT);
            }
            if (code == KeyEvent.VK_DOWN) {
                p2DownPressed = true;
                p2Tank.setDirection(Direction.DOWN);
            }
            if (code == KeyEvent.VK_RIGHT) {
                p2RightPressed = true;
                p2Tank.setDirection(Direction.RIGHT);
            }
            if (code == KeyEvent.VK_NUMPAD0) {
                shoot = true;
                p2Tank.setBulletCount(3);
                if (p2Tank.getBullets().size() < p2Tank.getBulletCount()) {
                    p2Tank.shoot();
                }
            }
            p1TankImageSrc = ".\\src\\tank" + p1Tank.getDirection() + ".png";
            p2TankImageSrc = ".\\src\\tank" + p2Tank.getDirection() + ".png";
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
            if (code == KeyEvent.VK_L) {
                shoot = false;
            }

            // p2 key

            if (code == KeyEvent.VK_UP) {
                p2UpPressed = false;
            }
            if (code == KeyEvent.VK_LEFT) {
                p2LeftPressed = false;
            }
            if (code == KeyEvent.VK_DOWN) {
                p2DownPressed = false;
            }
            if (code == KeyEvent.VK_RIGHT) {
                p2RightPressed = false;
            }
            if (code == KeyEvent.VK_NUMPAD0) {
                shoot = false;
            }
        }
    }

}