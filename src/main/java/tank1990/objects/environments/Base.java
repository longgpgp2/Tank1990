package tank1990.objects.environments;

import tank1990.common.classes.GameEntity;
import tank1990.common.classes.Vector2D;
import tank1990.common.enums.EntityType;
import tank1990.objects.animation.ExplosionAnimation;

import javax.swing.*;
import java.awt.*;

public class Base extends Environment {
    private boolean isDestroy = false;
    public Base(int x, int y) {
        super(EntityType.BASE,false, true, false, x, y);
        health= 1;
        image = new ImageIcon("src/main/resources/images/base.png").getImage();
    }
    @Override
    public void destroy() {
        System.out.println("Gà");

        // animation nổ
        ExplosionAnimation explosion = new ExplosionAnimation();
        explosion.startAnimation(() -> {
            image = new ImageIcon("src/main/resources/haha.jpg").getImage();
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
}
