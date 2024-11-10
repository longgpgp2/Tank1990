package tank1990.objects.environments;

import java.awt.Graphics2D;
import java.awt.Image;

import tank1990.common.classes.GameSprite;
import tank1990.common.enums.EntityType;
import tank1990.objects.animation.ExplosionAnimation;

public class Base extends Environment {
    public boolean isDestroy = false;

    public Base(int x, int y) {
        super(EntityType.BASE, false, true, false, x, y);
        health = 1;
        sprite = new GameSprite("src/main/resources/images/base.png");
    }

    @Override
    public void destroy() {
        ExplosionAnimation explosion = new ExplosionAnimation();
        explosion.startAnimation(() -> {
            isDestroy = true;
        }, new ExplosionAnimation.ImageUpdateCallback() {
            @Override
            public void updateImage(Image newImage) {
                image = newImage;
            }
        });

        // Xóa CollisionBox
        if (collisionBox != null) {
            collisionBox = null;
        }
    }

    public boolean isDestroy() {
        return isDestroy;
    }

    public void hit(int damage) {
        health -= damage;
        if (health <= 0) {
            destroy();
        }
    }

    public void draw(Graphics2D graphics2d) {
        graphics2d.drawImage(sprite.getBufferedImage(), (int) x, (int) y, this);
    }
}
