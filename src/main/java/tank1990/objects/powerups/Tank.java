package tank1990.objects.powerups;

import javax.swing.*;

public class Tank extends PowerUp{
	public Tank(int x, int y) {
		super(x ,y);
		image = new ImageIcon("src/main/resources/images/powerup_tank.png").getImage();
	}

	@Override
	public void activate(){
//		System.out.println(this.getName());
	}

}
