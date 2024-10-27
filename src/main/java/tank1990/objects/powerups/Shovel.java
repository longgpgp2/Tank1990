package tank1990.objects.powerups;

import javax.swing.*;

public class Shovel extends PowerUp{
	public Shovel(int x, int y) {
		super(x ,y);
		image = new ImageIcon("src/main/resources/images/powerup_shovel.png").getImage();
	}

	@Override
	public void activate(){
		System.out.println("[POWER-UP] Rebuild base's walls. This does nothing for now");
	}

}
