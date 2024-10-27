package tank1990.objects.powerups;

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
		PlayerTank playerTank = TankSpawner.playerTank;
		playerTank.setLives(playerTank.getLives() + 1);
		System.out.println("[POWER-UP] Increase lives by 1. Current lives: " + playerTank.getLives());
	}

}
