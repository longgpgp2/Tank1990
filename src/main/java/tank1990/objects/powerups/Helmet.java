package tank1990.objects.powerups;

import tank1990.manager.spawner.TankSpawner;
import tank1990.objects.tanks.PlayerTank;

import javax.swing.*;

public class Helmet extends PowerUp{
	public Helmet(int x, int y) {
		super(x ,y);
		image = new ImageIcon("src/main/resources/images/powerup_helmet.png").getImage();
	}
	@Override
	public void activate(){
		this.updatePoint();

		TankSpawner.playerTank.startShield(50);
		System.out.println("[POWER-UP] Shield is activated");
	}

}
