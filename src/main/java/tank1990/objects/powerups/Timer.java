package tank1990.objects.powerups;

import tank1990.manager.spawner.TankSpawner;
import tank1990.objects.tanks.EnemyTank;
import tank1990.objects.tanks.Tank;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Timer extends PowerUp{
	public Timer(int x, int y) {
		super(x ,y);
		image = new ImageIcon("src/main/resources/images/powerup_timer.png").getImage();
	}

	@Override
	public void activate(){
		for (EnemyTank tank : TankSpawner.enemyTanks) {
			tank.setMovementSpeed(0);
			tank.setBulletSpeed(0);
		}
		new javax.swing.Timer(5000, e -> {
			for (EnemyTank tank : TankSpawner.enemyTanks) {
				tank.resetSpeed();
			}
		}).start();
		System.out.println("[POWER-UP] Freeze all enemy tanks");
	}

}
