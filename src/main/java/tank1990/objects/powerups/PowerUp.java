package tank1990.objects.powerups;

import tank1990.common.classes.CollisionBox;
import tank1990.common.classes.GameEntity;
import tank1990.common.classes.Vector2D;
import tank1990.common.constants.GameConstants;
import tank1990.common.enums.EntityType;
import tank1990.main.GameInfoPanel;
import tank1990.manager.spawner.TankSpawner;
import tank1990.objects.tanks.PlayerTank;

/**
 * PowerUp là một vật thể xuất hiện ngẫu nhiên trong game sau khi PlayerTank hạ gục một EnemyTank.
 * PlayerTank có thể chạm vào PowerUp để kích hoạt hiệu ứng của PowerUp đó
 * 
 */
public abstract class PowerUp extends GameEntity {
    public String name = this.getClass().getSimpleName();
    public int x;
    public int y;
    public int point = 500;

    /**
     * Constructor
     * 
     * @param x hoành độ
     * @param y tung độ
     */
    public PowerUp(int x, int y) {
        super(EntityType.POWER_UP, new Vector2D(x, y), GameConstants.POWERUP_SIZE, GameConstants.POWERUP_SIZE);
        setCollision(new CollisionBox(this, new Vector2D(0, 0), GameConstants.POWERUP_SIZE, GameConstants.POWERUP_SIZE));
    }

    /**
     * Kích hoạt hiệu ứng của PowerUp
     * 
     */
    public abstract void activate();

    /**
     * Cập nhật điểm cho PlayerTank
     * 
     */
    protected void updatePoint() {
        PlayerTank playerTank = TankSpawner.playerTank;
        playerTank.setPoint(playerTank.getPoint() + point);
        GameInfoPanel.getInstance().updatePoint();
    }
}
