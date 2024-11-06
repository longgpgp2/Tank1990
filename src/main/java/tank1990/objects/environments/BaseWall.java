package tank1990.objects.environments;

import tank1990.common.classes.GameSprite;
import tank1990.manager.GameEntityManager;
import tank1990.manager.spawner.TankSpawner;

import javax.swing.*;

/**
 * BaseWall is a special type of BrickWall that can act as a pseudo-SteelWall and be repaired.
 *
 */
public class BaseWall extends BrickWall {
    public BaseWall(int x, int y) {
        super(x, y);
    }

    /**
     * Temporary transform from BrickWall to SteelWall, then back to BrickWall
     *
     */
    public void transform() {
        // add back to the game if destroyed
        if (destroyed) {
            GameEntityManager.add(this);
        }

        // restore its health
        this.health = 2;

        // temporarily turn itself into SteelWall
        this.destroyable = false;
        getCollision().setEnabled(true);
        setSprite(new GameSprite("src/main/resources/images/wall_steel.png"));

        // turn itself back into BrickWall
        Timer timer = new Timer(10000, e -> {
            setSprite(new GameSprite("src/main/resources/images/wall_brick.png"));
            this.destroyable = true;
        });
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Apply damage depending on the form of BaseWall
     *
     * @param damage The amount of taken damage
     */
    @Override
    public void hit(int damage) {
        // consider both BrickWall and SteelWall cases
        if (destroyable) {
            health -= damage;
        }
    }
}
