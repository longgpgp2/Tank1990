package tank1990.objects.powerups;

import tank1990.manager.spawner.TankSpawner;
import tank1990.objects.tanks.EnemyTank;

import javax.swing.*;

public class Timer extends PowerUp{
	public Timer(int x, int y) {
		super(x ,y);
		image = new ImageIcon("src/main/resources/images/powerup_timer.png").getImage();
	}

	@Override
	public void activate(){
		this.updatePoint();

		for (EnemyTank tank : TankSpawner.enemyTanks) {
			tank.freeze();
		}
	}

}
