package tank1990.objects.powerups;

import tank1990.manager.spawner.TankSpawner;
import tank1990.objects.tanks.Bullet;
import tank1990.objects.tanks.PlayerTank;

import javax.swing.*;

public class Star extends PowerUp{
	public Star(int x, int y) {
		super(x ,y);
		image = new ImageIcon("src/main/resources/images/powerup_star.png").getImage();
	}
	@Override
	public void activate(){
		this.updatePoint();

		PlayerTank playerTank = TankSpawner.playerTank;
		if (playerTank.getStar() < 4) { // cap at level 4
			playerTank.setStar(playerTank.getStar() + 1);
			System.out.println("[POWER-UP] Increased power level by 1. Current level: " + playerTank.getStar());
		} else {
			System.out.println("[POWER-UP] Maximum power level reached. Current level: " + playerTank.getStar());
			return;
		}

		switch (playerTank.getStar()) {
			case 2:
				playerTank.setBulletSpeed(playerTank.getBulletSpeed() * 2);
				System.out.println("[POWER-UP] Doubled bullet speed");
				break;
			case 3:
				playerTank.setBulletCount(playerTank.getBulletCount() + 1);
				playerTank.getBullets().clear();
				playerTank.initalizeBullets();
				System.out.println("[POWER-UP] Doubled bullet speed");
				break;
			case 4:
				for (Bullet bullet : playerTank.getBullets()) {
					bullet.setDamage(2);
				}
				break;
			default:
				break;
		}
	}

}
