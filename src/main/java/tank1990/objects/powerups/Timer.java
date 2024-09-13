package tank1990.objects.powerups;

public class Timer extends PowerUp{
	public Timer(String name) {
		super(name);
	}
	public Timer(){

	}
	@Override
	public void activate(){
		System.out.println(this.getName());
	}

}
