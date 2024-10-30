package tank1990.objects.powerups;

import javax.swing.*;

public class Star extends PowerUp{
	public Star(int x, int y) {
		super(x ,y);
		image = new ImageIcon("src/main/resources/images/powerup_star.png").getImage();
	}
	@Override
	public void activate(){
//		System.out.println(this.getName());
	}

}
