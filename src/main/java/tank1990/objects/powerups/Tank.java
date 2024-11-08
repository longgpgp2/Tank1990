package tank1990.objects.powerups;

import tank1990.manager.spawner.TankSpawner;
import tank1990.objects.tanks.PlayerTank;

import javax.swing.*;

/**
 * Tank (PowerUp) là một PowerUp khi kích hoạt sẽ cho PlayerTank thêm 1 mạng.
 * Số lượng mạng tối đa của PlayerTank là 3.
 * 
 */
public class Tank extends PowerUp{
	/**
	 * Constructor
	 * 
	 * @param x hoành độ
	 * @param y tung độ
	 */
	public Tank(int x, int y) {
		super(x ,y);
		image = new ImageIcon("src/main/resources/images/powerup_tank.png").getImage();
	}

	/**
	 * Kích hoạt hiệu ứng của Tank:
	 * - Cộng điểm cho PlayerTank
	 * - Cộng lives cho PlayerTank nếu lives < 3
	 * 
	 */
	@Override
	public void activate(){
		this.updatePoint();

		PlayerTank playerTank = TankSpawner.playerTank;
		if (playerTank.getHealth() < 3) { // giới hạn 3 mạng
			playerTank.setHealth(playerTank.getHealth() + 1);
			System.out.println("[POWER-UP] Increase lives by 1. Current lives: " + playerTank.getHealth());
		} else {
			System.out.println("[POWER-UP] Maximum lives reached. Current lives: " + playerTank.getHealth());
		}
	}

}
