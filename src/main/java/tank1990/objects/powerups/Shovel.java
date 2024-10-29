package tank1990.objects.powerups;

import tank1990.main.GamePanel;
import tank1990.objects.environments.BaseWall;
import tank1990.objects.environments.Environment;

import javax.swing.*;
import java.util.List;

public class Shovel extends PowerUp{
	public Shovel(int x, int y) {
		super(x ,y);
		image = new ImageIcon("src/main/resources/images/powerup_shovel.png").getImage();
	}

	@Override
	public void activate() {
		List<Environment> environments = GamePanel.getEnvironments();
		for (Environment env : environments) {
			if (env instanceof BaseWall) {
				((BaseWall) env).transform();
				System.out.println("Transformed a wall into steel");
			}
		}
		System.out.println("[POWER-UP] Rebuild base's walls.");
	}

}
