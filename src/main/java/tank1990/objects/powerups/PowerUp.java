package tank1990.objects.powerups;

import tank1990.main.GamePanel;
import tank1990.objects.common.Entity;

public abstract class PowerUp extends Entity {
    public String name = this.getClass().getSimpleName();
    public int x;
    public int y;
    public int point = 500;
    public PowerUp(int x, int y) {
        super(x, y, GamePanel.BRICK_WIDTH, GamePanel.BRICK_HEIGHT);
        this.name = name;
    }
    public void activate(){

    }
}
