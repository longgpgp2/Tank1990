package tank1990.objects.animation;

import javax.swing.*;
import java.awt.*;

public class BulletExplosion {
    private int x;
    private int y;
    private int frame = 0;
    private Image[] images = new Image[3];
    private boolean finished = false;
    private final long delay = 70; // Thay đổi delay để chạy animation mượt hơn
    private long lastUpdateTime;

    public BulletExplosion(int x, int y) {
        this.x = x;
        this.y = y;
        images[0] = new ImageIcon("src/main/resources/images/bullet_explosion_1.png").getImage();
        images[1] = new ImageIcon("src/main/resources/images/bullet_explosion_2.png").getImage();
        images[2] = new ImageIcon("src/main/resources/images/bullet_explosion_3.png").getImage();

        lastUpdateTime = System.currentTimeMillis();
    }

    public void update(Runnable onFinish, BulletExplosionImageCallback callback) {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastUpdateTime >= delay) {
            frame++;
            lastUpdateTime = currentTime;
            if (frame >= images.length) {
                finished = true;
                if (onFinish != null) {
                    onFinish.run(); // Gọi callback khi animation kết thúc
                }
            } else {
                callback.updateImage(images[frame]); // Cập nhật ảnh của bullet
            }
        }
    }

    public boolean isFinished() {
        return finished;
    }

    public interface BulletExplosionImageCallback {
        void updateImage(Image image);
    }

    public void render(Graphics g) {
        if (frame < images.length) {
            int imgWidth = images[frame].getWidth(null);
            int imgHeight = images[frame].getHeight(null);
            g.drawImage(images[frame], x - imgWidth / 2, y - imgHeight / 2, null);
        }
    }
}
