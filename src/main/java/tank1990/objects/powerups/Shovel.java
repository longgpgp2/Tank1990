package tank1990.objects.powerups;

import tank1990.common.classes.GameEntity;
import tank1990.common.enums.EntityType;
import tank1990.main.GamePanel;
import tank1990.manager.GameEntityManager;
import tank1990.objects.environments.BaseWall;
import tank1990.objects.environments.Environment;

import javax.swing.*;
import java.util.List;

/**
 * Shovel là một PowerUp khi kích hoạt sẽ biến tất cả BaseWall tạm thành SteelWall tạm thời.
 * Sau một khoản thời gian sẽ biến đổi trở lại thành BrickWall và hồi phục mọi tổn thất.
 * 
 */
public class Shovel extends PowerUp{
	/**
	 * Constructor
	 * 
	 * @param x hoành độ
	 * @param y tung độ
	 */
	public Shovel(int x, int y) {
		super(x ,y);
		image = new ImageIcon("src/main/resources/images/powerup_shovel.png").getImage();
	}

	/**
	 * Kích hoạt hiệu ứng của Shovel:
	 * - Cộng điểm cho PlayerTank
	 * - Biến đổi các BaseWall
	 * 
	 */
	@Override
	public void activate() {
		this.updatePoint();

		List<GameEntity> environments = List.of(GameEntityManager.getGameEntity(EntityType.BASE_WALL));
		for (GameEntity env : environments) {
			if (env instanceof BaseWall) {
				((BaseWall) env).transform();
			}
		}
		System.out.println("[POWER-UP] Base's walls have been rebuilt");
	}

}
