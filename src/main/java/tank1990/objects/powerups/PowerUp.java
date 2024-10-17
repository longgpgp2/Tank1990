package tank1990.objects.powerups;

import tank1990.main.GamePanel;
import tank1990.objects.common.Entity;
import tank1990.objects.common.constants.GameConstants;

public abstract class PowerUp extends Entity {
    public String name = this.getClass().getSimpleName();
    public int x;
    public int y;
    public int point = 500;
    public PowerUp(int x, int y) {
        super(x, y, GameConstants.POWERUP_SIZE, GameConstants.POWERUP_SIZE);
    }
    public void activate(){

    }
}
