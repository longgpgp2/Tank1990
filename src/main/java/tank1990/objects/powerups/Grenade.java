package tank1990.objects.powerups;

import tank1990.manager.GameEntityManager;
import tank1990.manager.spawner.TankSpawner;
import tank1990.objects.tanks.EnemyTank;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Grenade là một PowerUp khi kích hoạt sẽ tiêu diệt tất cả EnemyTank 
 * đã xuất hiện trên sân.
 * 
 */
public class Grenade extends PowerUp {
	/**
	 * Constructor
	 * 
	 * @param x hoành độ
	 * @param y tung độ
	 */
	public Grenade(int x, int y) {
		super(x, y);
		image = new ImageIcon("src/main/resources/images/powerup_grenade.png").getImage();
	}

	/**
	 * Kích hoạt hiệu ứng của Grenade:
	 * - Cộng điểm cho PlayerTank
	 * - Giảm health của tất cả EnemyTank đã xuất hiện về 0
	 * - Gỡ các EnemyTank đó khỏi các List gameEntities và List enemyTanks
	 * 
	 */
	@Override
	public void activate() {
		this.updatePoint();

		/* lưu lại một List các EnemyTank cần xóa tránh tương tác với
			List enemyTanks đang được dùng trong vòng lặp */  
		List<EnemyTank> removedTanks = new ArrayList<>();
		for (EnemyTank enemyTank : TankSpawner.enemyTanks) {
			if (!enemyTank.isAppearing()) { // bỏ qua các EnemyTank đang xuất hiện
				removedTanks.add(enemyTank);
			}
		}

		// thực hiện xóa bỏ các EnemyTank
		for (EnemyTank tank : removedTanks) {
			tank.health = 0;
			tank.destroy();
			GameEntityManager.remove(tank);
		}
		System.out.println("[POWER-UP] Destroy all enemy tanks");
	}

}
