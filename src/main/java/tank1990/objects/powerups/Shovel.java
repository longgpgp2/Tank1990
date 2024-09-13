package tank1990.objects.powerups;

public class Shovel extends PowerUp{
	public Shovel(String name) {
		super(name);
	}
	public Shovel(){

	}
	@Override
	public void activate(){
		System.out.println(this.getName());
	}

}
