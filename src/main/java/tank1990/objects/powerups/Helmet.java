package tank1990.objects.powerups;

import tank1990.manager.spawner.TankSpawner;

import javax.swing.*;

/**
 * Helmet là một PowerUp khi kích hoạt sẽ tạo một chiếc khiên bao quanh PlayerTank.
 * PlayerTank khi có khiên sẽ không nhận sát thương
 * 
 */
public class Helmet extends PowerUp{
	/**
	 * Constructor
	 * 
	 * @param x hoành độ
	 * @param y tung độ
	 */
	public Helmet(int x, int y) {
		super(x ,y);
		image = new ImageIcon("src/main/resources/images/powerup_helmet.png").getImage();
	}

	/**
	 * Kích hoạt hiệu ứng của Helmet:
	 * - Cộng điểm cho PlayerTank
	 * - Kích hoạt khiên, tồn tại trong 10s (tương ứng với 25 lần lặp hiệu ứng)
	 * 
	 */
	@Override
	public void activate(){
		this.updatePoint();

		TankSpawner.playerTank.startShield(25);
		System.out.println("[POWER-UP] Shield is activated");
	}

}
