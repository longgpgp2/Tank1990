package tank1990.objects.animation;

import javax.swing.ImageIcon;

public class Appear {
    private ImageIcon[] frames;
    private int currentFrame;
    private int animationLoops;
    private static final int MAX_ANIMATION_LOOPS = 4;
    private long lastFrameTime;
    private long frameDuration;

    public Appear(long frameDuration) {
        this.frames = loadImages(getImagePaths());
        this.frameDuration = frameDuration;
        this.currentFrame = 0;
        this.animationLoops = 0;
        this.lastFrameTime = System.currentTimeMillis();
    }

    private ImageIcon[] loadImages(String[] paths) {
        ImageIcon[] images = new ImageIcon[paths.length];
        for (int i = 0; i < paths.length; i++) {
            images[i] = new ImageIcon(paths[i]);
        }
        return images;
    }

    public static String[] getImagePaths() {
        return new String[]{
                "src/main/resources/images/appear_4.png",
                "src/main/resources/images/appear_3.png",
                "src/main/resources/images/appear_2.png",
                "src/main/resources/images/appear_1.png"
        };
    }

    public ImageIcon getCurrentFrame() {
        if (animationLoops >= MAX_ANIMATION_LOOPS) {
            return null;
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFrameTime >= frameDuration) {
            lastFrameTime = currentTime;
            currentFrame++;
            if (currentFrame >= frames.length) {
                currentFrame = 0;
                animationLoops++;
            }
        }
        return frames[currentFrame];
    }

    public boolean isAnimationFinished() {
        return animationLoops >= MAX_ANIMATION_LOOPS;
    }
}
