package tank1990.objects.powerups;

public class Helmet extends PowerUp{
	public Helmet(String name) {
		super(name);
	}
	public Helmet(){

	}
	@Override
	public void activate(){
		System.out.println(this.getName());
	}

}
