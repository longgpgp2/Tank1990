package tank1990.objects.powerups;

import javax.swing.*;

public class Grenade extends PowerUp{
	public Grenade(int x, int y) {
		super(x ,y);
		image = new ImageIcon("src/main/resources/images/powerup_grenade.png").getImage();
	}

	@Override
	public void activate(){
//		System.out.println(this.getName());
	}

}
