package tank1990.objects.powerups;

import tank1990.manager.spawner.TankSpawner;
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
		if (playerTank.getStar() < 4) {
			playerTank.setStar(playerTank.getStar() + 1);
			System.out.println("[POWER-UP] Increased power by 1. Current level: " + playerTank.getStar());
		} else {
			System.out.println("[POWER-UP] Maximum power level reached. Current level: " + playerTank.getStar());
		}
	}

}
