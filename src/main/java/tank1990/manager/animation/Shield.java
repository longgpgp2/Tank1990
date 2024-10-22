package tank1990.manager.animation;

import javax.swing.ImageIcon;

public class Shield {
    private ImageIcon[] frames;
    private int currentFrame;
    private long lastFrameTime;
    private long frameDuration;
    private int animationLoops;
    private static final int MAX_ANIMATION_LOOPS = 4;

    public Shield(long frameDuration) {
        String[] framePaths = {
                "src/main/resources/images/shield_1.png",
                "src/main/resources/images/shield_2.png"
        };

        this.frames = new ImageIcon[framePaths.length];
        this.frameDuration = frameDuration;
        this.currentFrame = 0;
        this.animationLoops = 0;
        this.lastFrameTime = System.currentTimeMillis();

        // Preload images
        for (int i = 0; i < framePaths.length; i++) {
            this.frames[i] = new ImageIcon(framePaths[i]);
        }
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
