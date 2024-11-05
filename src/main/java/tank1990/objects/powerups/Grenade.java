package tank1990.objects.powerups;

import tank1990.manager.GameEntityManager;
import tank1990.manager.spawner.TankSpawner;
import tank1990.objects.tanks.EnemyTank;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Grenade extends PowerUp{
	public Grenade(int x, int y) {
		super(x ,y);
		image = new ImageIcon("src/main/resources/images/powerup_grenade.png").getImage();
	}

	@Override
	public void activate(){
		this.updatePoint();

		// save a list of removed tanks to remove from enemyTanks later
		List<EnemyTank> removedTanks = new ArrayList<>();
		for (EnemyTank enemyTank : TankSpawner.enemyTanks) {
			if (!enemyTank.isAppearing()) {
				removedTanks.add(enemyTank);
			}
		}

		for (EnemyTank tank : removedTanks) {
			tank.health = 0;
			tank.destroy();
			GameEntityManager.remove(tank);
		}
		System.out.println("[POWER-UP] Destroy all enemy tanks");
	}

}
