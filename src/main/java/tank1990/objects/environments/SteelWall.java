package tank1990.objects.environments;

public class SteelWall extends Environment{
    int[][] health = {{1, 1, 1, 1}, {1, 1, 1, 1}, {1, 1, 1, 1}, {1, 1, 1, 1}};

    public SteelWall(int x, int y) {
        super(false, false, true, x, y);
        this.setHealth(health);
    }



    @Override
    public String toString() {
        return "Environment{" +
                "name='" + this.getName() + '\'' +
                ", crossable=" + this.getCrossable() +
                ", destroyable=" + this.getDestroyable() +
                ", health=" + this.getHealth() +
                '}';
    }
}

