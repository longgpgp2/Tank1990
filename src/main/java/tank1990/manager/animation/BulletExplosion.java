package tank1990.manager.animation;

import javax.swing.*;
import java.awt.*;

public class BulletExplosion {
    private int x;
    private int y;
    private int frame = 0;
    private Image[] images = new Image[3];
    private boolean finished = false;
    private final long delay = 10;
    private long lastUpdateTime;

    public BulletExplosion(int x, int y) {
        this.x = x;
        this.y = y;
        images[0] = new ImageIcon("src/main/resources/images/bullet_explosion_1.png").getImage();
        images[1] = new ImageIcon("src/main/resources/images/bullet_explosion_2.png").getImage();
        images[2] = new ImageIcon("src/main/resources/images/bullet_explosion_3.png").getImage();


        lastUpdateTime = System.currentTimeMillis();
    }

    public void update() {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastUpdateTime >= delay) {
            frame++;
            lastUpdateTime = currentTime;
            if (frame >= images.length) {
                finished = true;
            }
        }
    }

    public boolean isFinished() {
        return finished;
    }

    public void render(Graphics g) {
        if (frame < images.length-2) {
            // Vẽ hình ảnh nổ tại vị trí của nó
            System.out.println(frame);
            System.out.println(images.length);
            int imgWidth = images[frame].getWidth(null);
            int imgHeight = images[frame].getHeight(null);
            g.drawImage(images[frame], x - imgWidth / 2, y - imgHeight / 2, null);
        }
    }
}
