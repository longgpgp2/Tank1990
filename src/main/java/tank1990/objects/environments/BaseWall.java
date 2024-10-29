package tank1990.objects.environments;

import tank1990.common.classes.GameSprite;

import javax.swing.*;

/**
 * BaseWall is a special type of BrickWall that acts as a pseudo SteelWall and be repaired.
 *
 */
public class BaseWall extends BrickWall {
    public BaseWall(int x, int y) {
        super(x, y);
    }

    // TODO: This only works with walls that is not removed the map
    public void transform() {
        // restore its health
        this.health = 2;
        // temporarily turn itself into a pseudo SteelWall
        setSprite(new GameSprite("src/main/resources/images/wall_steel.png"));
        this.destroyable = false;
        // turn itself back into BrickWall
        new Timer(10000, e -> {
            setSprite(new GameSprite("src/main/resources/images/wall_brick.png"));
            this.destroyable = true;
        }).start();
    }
}
