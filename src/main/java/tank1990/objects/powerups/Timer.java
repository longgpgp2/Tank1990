package tank1990.objects.powerups;

import javax.swing.*;

public class Timer extends PowerUp{
	public Timer(int x, int y) {
		super(x ,y);
		image = new ImageIcon("src/main/resources/images/powerup_timer.png").getImage();
	}

	@Override
	public void activate(){
//		System.out.println(this.getName());
	}

}
