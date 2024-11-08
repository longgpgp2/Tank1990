package tank1990.manager;

import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

import tank1990.common.classes.Vector2D;
import tank1990.common.enums.Direction;
import tank1990.main.GameObject;
import tank1990.manager.spawner.TankSpawner;
import tank1990.objects.tanks.PlayerTank;

public class KeyHandler {
    public boolean upPressed, downPressed, leftPressed, rightPressed, shoot;
    public PlayerTank tank;

    public KeyHandler(PlayerTank tank) {
        this.tank = tank;
    }

    public void keyTyped(KeyEvent e) {

    }

    public void disableHorizontal() {
        if (upPressed || downPressed) {
            leftPressed = false;
            rightPressed = false;
        }
    }

    public void setPress(int key) {
        if (key == KeyEvent.VK_A) {
            leftPressed = true;
            disableVertical();
            rightPressed = false;
            tank.setDirection(Direction.LEFT);
            updateImage();
        } else if (key == KeyEvent.VK_D) {
            rightPressed = true;
            disableVertical();
            leftPressed = false;
            tank.setDirection(Direction.RIGHT);
            updateImage();
        } else if (key == KeyEvent.VK_W) {
            upPressed = true;
            disableHorizontal();
            downPressed = false;
            tank.setDirection(Direction.UP);
            updateImage();
        } else if (key == KeyEvent.VK_S) {
            downPressed = true;
            disableHorizontal();
            upPressed = false;
            tank.setDirection(Direction.DOWN);
            updateImage();
        }else if (key == KeyEvent.VK_0) { // FOR TESTING
            tank.setHealth(0);
        } else if (key == KeyEvent.VK_9) { // FOR TESTING
            GameObject.getInstance().nextLevel();
        }
    }

    public void disableVertical() {
        if (leftPressed || rightPressed) {
            upPressed = false;
            downPressed = false;
        }
    }

    public void updateVelocity() {
        if (upPressed) {
            tank.setVelocity(new Vector2D(0, -tank.speed));
        }
        if (downPressed) {
            tank.setVelocity(new Vector2D(0, tank.speed));
        }
        if (leftPressed) {
            tank.setVelocity(new Vector2D(-tank.speed, 0));
        }
        if (rightPressed) {
            tank.setVelocity(new Vector2D(tank.speed, 0));
        }
        if (!upPressed && !downPressed && !leftPressed && !rightPressed) {
            tank.setVelocity(new Vector2D(0, 0));
        }
        updateImage();
    }

    public void updateImage() {
        if (tank.isAppearing()) {
            return;
        }
        ImageIcon ii = null;
        if (tank.spriteNum == 1) {
            ii = new ImageIcon(
                    "src/main/resources/images/tank_player1_" + tank.getDirection().toString().toLowerCase()
                            + "_c0_t1.png");
        }
        if (tank.spriteNum == 2) {
            ii = new ImageIcon(
                    "src/main/resources/images/tank_player1_" + tank.getDirection().toString().toLowerCase()
                            + "_c0_t2.png");
        }
        tank.image = ii.getImage();
    }

    public void updateTankSpriteCounter() {
        tank.spriteCounter++;
        if (tank.spriteCounter > 2) { // animation speed
            if (tank.spriteNum == 1) {
                tank.spriteNum = 2;
            } else if (tank.spriteNum == 2) {
                tank.spriteNum = 1;
            }
            tank.spriteCounter = 0;
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if(key==KeyEvent.VK_1) {
            TankSpawner.removeEnemies();
            TankSpawner.disableEnemySpawner();
        }
        if(key==KeyEvent.VK_2) {
            TankSpawner.removePlayer();
        }
        if(key==KeyEvent.VK_3) {
            TankSpawner.spawnPlayer();
        }
        if(key==KeyEvent.VK_4) {
            TankSpawner.enableEnemySpawner(1);
        }
        if(!tank.enabled) return;
        setPress(key);
        if (key == KeyEvent.VK_SPACE) {
            tank.shoot();
        }
        updateVelocity();
        updateTankSpriteCounter();
        updateImage();
    }

    public void keyReleased(KeyEvent e) {
        if(!tank.enabled){
            upPressed=false;
            downPressed=false;
            leftPressed=false;
            rightPressed=false;
            return;}

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
    }
}
