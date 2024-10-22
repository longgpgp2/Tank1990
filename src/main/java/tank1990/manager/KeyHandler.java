package tank1990.manager;

import tank1990.common.classes.Vector2D;
import tank1990.common.enums.Direction;
import tank1990.objects.tanks.PlayerTank;
import tank1990.objects.tanks.Tank;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler {
    public boolean upPressed, downPressed, leftPressed, rightPressed, shoot;
    public PlayerTank tank;

    public KeyHandler(PlayerTank tank){
        this.tank = tank;
    }

    public void keyTyped(KeyEvent e) {

    }

    public void disableHorizontal(){
        if(upPressed||downPressed){
            leftPressed=false;
            rightPressed=false;
        }
    }
    public void disableVertical(){
        if(leftPressed||rightPressed){
            upPressed=false;
            downPressed=false;
        }
    }

    public void updatePosition(){
        if(upPressed)
            tank.setPosition(tank.getPosition().add(new Vector2D(0,-tank.velocity)));
        if(downPressed)
            tank.setPosition(tank.getPosition().add(new Vector2D(0,tank.velocity)));
        if(leftPressed)
            tank.setPosition(tank.getPosition().add(new Vector2D(-tank.velocity,0)));
        if(rightPressed)
            tank.setPosition(tank.getPosition().add(new Vector2D(tank.velocity,0)));
    }

    public void updateImage(){
        ImageIcon ii = null;
        if (tank.spriteNum == 1) {
            ii = new ImageIcon("src/main/resources/images/tank_player1_"+tank.direction.toString().toLowerCase()+"_c0_t1.png");
        }
        if (tank.spriteNum == 2) {
            ii = new ImageIcon("src/main/resources/images/tank_player1_"+tank.direction.toString().toLowerCase()+"_c0_t2.png");
        }
        tank.image = ii.getImage();
    }
    public void updateTankSpriteCounter(){
        tank.spriteCounter++;
        if (tank.spriteCounter > 6) { // animation speed
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
        if (key == KeyEvent.VK_A) {
            leftPressed=true;
            disableVertical();
            tank.direction = Direction.LEFT;
            updateImage();
        } else if (key == KeyEvent.VK_D) {
            rightPressed=true;
            disableVertical();
            tank.direction = Direction.RIGHT;
            updateImage();
        } else if (key == KeyEvent.VK_W) {
            upPressed=true;
            disableHorizontal();
            tank.direction = Direction.UP;
            updateImage();
        } else if (key == KeyEvent.VK_S) {
            downPressed=true;
            disableHorizontal();
            tank.direction = Direction.DOWN;
            updateImage();
        }
        if (key == KeyEvent.VK_SPACE) { // Bắn khi nhấn phím SPACE
            tank.shoot();
        }
        updateTankSpriteCounter();
    }


    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W){
            upPressed=false;
        }
        if (code == KeyEvent.VK_A){
            leftPressed=false;
        }
        if (code == KeyEvent.VK_S){
            downPressed=false;
        }
        if (code == KeyEvent.VK_D){
            rightPressed=false;
        }
    }
}
