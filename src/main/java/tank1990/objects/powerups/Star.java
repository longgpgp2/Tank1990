package tank1990.objects.powerups;

import tank1990.manager.spawner.TankSpawner;
import tank1990.objects.tanks.Bullet;
import tank1990.objects.tanks.PlayerTank;

import javax.swing.*;

/**
 * Star là một PowerUp khi kích hoạt sẽ cường hóa các Bullet cho PlayerTank
 * Có 4 cấp độ:
 * - Lv 1: cấp độ gốc
 * - Lv 2: tốc độ đạn tăng gấp đôi
 * - Lv 3: số lượng đạn tăng thêm 1
 * - Lv 4: sát thương đạn tăng thêm 1
 * 
 */
public class Star extends PowerUp{
	/**
	 * Constructor
	 * 
	 * @param x hoành độ
	 * @param y tung độ
	 */
	public Star(int x, int y) {
		super(x ,y);
		image = new ImageIcon("src/main/resources/images/powerup_star.png").getImage();
	}

	/**
	 * Kích hoạt hiệu ứng của Star:
	 * - Cộng điểm cho PlayerTank
	 * - Cộng star cho PlayerTank nếu star < 4
	 * - Áp dụng cường hóa 
	 * 
	 */
	@Override
	public void activate(){
		this.updatePoint();

		PlayerTank playerTank = TankSpawner.playerTank;
		if (playerTank.getStar() < 4) { // giới hạn ở cấp độ 4
			playerTank.setStar(playerTank.getStar() + 1);
			System.out.println("[POWER-UP] Increased power level by 1. Current level: " + playerTank.getStar());
		} else {
			System.out.println("[POWER-UP] Maximum power level reached. Current level: " + playerTank.getStar());
			return;
		}

		// cường hóa các Bullet cho PlayerTank
		switch (playerTank.getStar()) {
			case 2:
				playerTank.setBulletSpeed(playerTank.getBulletSpeed() * 2);
				System.out.println("[POWER-UP] Increased bullet speed");
				break;
			case 3:
				playerTank.setBulletCount(playerTank.getBulletCount() + 1);
				playerTank.getBullets().clear();
				playerTank.initalizeBullets();
				System.out.println("[POWER-UP] Increased number of bullets");
				break;
			case 4:
				for (Bullet bullet : playerTank.getBullets()) {
					bullet.setDamage(2);
				}
				System.out.println("[POWER-UP] Increased bullet damage");
				break;
			default:
				break;
		}
	}

}
