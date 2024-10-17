package tank1990.objects.powerups;

import tank1990.common.classes.GameEntity;
import tank1990.common.classes.Vector2D;
import tank1990.common.constants.GameConstants;
import tank1990.common.enums.EntityType;

public abstract class PowerUp extends GameEntity {
    public String name = this.getClass().getSimpleName();
    public int x;
    public int y;
    public int point = 500;

    public PowerUp(int x, int y) {
        super(EntityType.POWER_UP, new Vector2D(x, y), GameConstants.POWERUP_SIZE, GameConstants.POWERUP_SIZE);
    }

    public void activate() {

    }
}
