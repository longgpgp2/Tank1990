package tank1990.objects.powerups;

public class Grenade extends PowerUp{
	public Grenade(String name) {
		super(name);
	}
	public Grenade(){

	}
	@Override
	public void activate(){
		System.out.println(this.getName());
	}

}
