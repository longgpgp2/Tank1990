package tank1990.objects.environments;

import tank1990.common.enums.EntityType;

public class Ice extends Environment{
    int[][] health = {{2, 2, 2, 2}, {2, 2, 2, 2}, {2, 2, 2, 2}, {2, 2, 2, 2}};

	public Ice(int x, int y) {
        super(EntityType.RIVER,false, false, true, x, y);
        super.health= (health);
    }



//    @Override
//    public String toString() {
//        return "Environment{" +
//                "name='" + this.getName() + '\'' +
//                ", crossable=" + this.getCrossable() +
//                ", destroyable=" + this.getDestroyable() +
//                ", health=" + this.getHealth() +
//                '}';
//    }
}

