package tank1990.objects.powerups;

public abstract class PowerUp {
    private String name = this.getClass().getSimpleName();
    private int point = 500;
    public PowerUp(String name) {
        this.name = name;
    }
    public PowerUp(){

    }
    public void activate(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
