package tank1990.objects.environments;

import tank1990.common.enums.EntityType;

public class SteelWall extends Environment{
    int[][] health = {{1, 1, 1, 1}, {1, 1, 1, 1}, {1, 1, 1, 1}, {1, 1, 1, 1}};

    public SteelWall(int x, int y) {
        super(EntityType.STEEL,false, false, true, x, y);
        super.health= health;
    }


//
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

