package tank1990.objects.environments;

import tank1990.common.classes.GameSprite;
import tank1990.common.enums.EntityType;

import java.awt.Graphics2D;

public class SteelWall extends Environment {
    public SteelWall(int x, int y) {
        super(EntityType.STEEL, false, false, true, x, y);

        health = 1;
        sprite = new GameSprite("src/main/resources/images/wall_steel.png");
    }

    public void draw(Graphics2D graphics2d) {
        graphics2d.drawImage(sprite.getBufferedImage(), (int) x, (int) y, this);
    }
}
