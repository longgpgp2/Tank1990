package tank1990.objects.environments;

import tank1990.common.classes.GameSprite;
import tank1990.common.enums.EntityType;

import java.awt.Graphics2D;

import javax.swing.*;

public class Trees extends Environment {
    public Trees(int x, int y) {
        super(EntityType.TREE, true, false, true, x, y);
        health = 1;
        sprite = new GameSprite("src/main/resources/images/trees.png");
    }

    public void draw(Graphics2D graphics2d) {
        graphics2d.drawImage(sprite.getBufferedImage(), (int) x, (int) y, this);
    }
}
