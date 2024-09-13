package tank1990.objects.powerups;

public class Star extends PowerUp{
	public Star(String name) {
		super(name);
	}
	public Star(){

	}
	@Override
	public void activate(){
		System.out.println(this.getName());
	}

}
