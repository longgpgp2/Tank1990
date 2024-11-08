package tank1990.objects.powerups;

import tank1990.manager.spawner.TankSpawner;
import tank1990.objects.tanks.EnemyTank;

import javax.swing.*;

/**
 * Timer là một PowerUp khi kích hoạt sẽ đóng băng tất cả các EnemyTank đã xuất hiện, 
 * khiến chúng không thể hành động trong một thời gian.
 * 
 */
public class Timer extends PowerUp{
	/**
	 * Constructor
	 * 
	 * @param x hoành độ
	 * @param y tung độ
	 */
	public Timer(int x, int y) {
		super(x ,y);
		image = new ImageIcon("src/main/resources/images/powerup_timer.png").getImage();
	}

	/**
	 * Kích hoạt hiệu ứng của Timer:
	 * - Cộng điểm cho PlayerTank
	 * - Đóng băng tất cả các EnemyTank đã xuất hiện
	 * 
	 */
	@Override
	public void activate(){
		this.updatePoint();

		for (EnemyTank tank : TankSpawner.enemyTanks) {
			if (!tank.isAppearing()) { // bỏ qua các EnemyTank đang xuất hiện
				tank.freeze();
			}
		}
	}

}
