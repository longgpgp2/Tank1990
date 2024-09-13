package tank1990.objects.powerups;

public class Tank extends PowerUp{
	public Tank(String name) {
		super(name);
	}
	public Tank(){

	}
	@Override
	public void activate(){
		System.out.println(this.getName());
	}

}
