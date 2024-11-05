package tank1990.objects.powerups;

import tank1990.main.GameFrame;
import tank1990.manager.spawner.TankSpawner;
import tank1990.objects.tanks.PlayerTank;

import javax.swing.*;

public class Tank extends PowerUp{

	public Tank(int x, int y) {
		super(x ,y);
		image = new ImageIcon("src/main/resources/images/powerup_tank.png").getImage();
	}

	@Override
	public void activate(){
		this.updatePoint();

		PlayerTank playerTank = TankSpawner.playerTank;
		if (playerTank.getLives() < 9) {
			playerTank.setLives(playerTank.getLives() + 1);
			System.out.println("[POWER-UP] Increase lives by 1. Current lives: " + playerTank.getLives());
		} else {
			System.out.println("[POWER-UP] Maximum lives reached. Current lives: " + playerTank.getLives());
		}
	}

}
