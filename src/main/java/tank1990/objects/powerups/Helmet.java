package tank1990.objects.powerups;

import javax.swing.*;

public class Helmet extends PowerUp{
	public Helmet(int x, int y) {
		super(x ,y);
		image = new ImageIcon("src/main/resources/images/powerup_helmet.png").getImage();
	}
	@Override
	public void activate(){
//		System.out.println(this.getName());
	}

}
