package tank1990.manager.animation;

import java.awt.Image;
import javax.swing.ImageIcon;

public class ExplosionAnimation {
    private Image[] frames;
    private int currentFrame;
    private boolean finished;

    public ExplosionAnimation() {
        frames = new Image[5];
        frames[0] = new ImageIcon("src/main/resources/images/big_explosion_1.png").getImage();
        frames[1] = new ImageIcon("src/main/resources/images/big_explosion_2.png").getImage();
        frames[2] = new ImageIcon("src/main/resources/images/big_explosion_3.png").getImage();
        frames[3] = new ImageIcon("src/main/resources/images/big_explosion_4.png").getImage();
        frames[4] = new ImageIcon("src/main/resources/images/big_explosion_5.png").getImage();
        currentFrame = 0;
        finished = false;
    }

    public boolean isFinished() {
        return finished;
    }

    public void startAnimation(Runnable onFinish, ImageUpdateCallback callback) {
        new Thread(() -> {
            try {
                for (currentFrame = 0; currentFrame < frames.length; currentFrame++) {
                    callback.updateImage(frames[currentFrame]); // Cập nhật ảnh
                    Thread.sleep(100); // Chờ 100ms giữa mỗi frame
                }
                finished = true;
                if (onFinish != null) {
                    onFinish.run();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // Interface callback để cập nhật ảnh
    public interface ImageUpdateCallback {
        void updateImage(Image image);
    }
}
