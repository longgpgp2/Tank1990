package tank1990.objects.environments;

import tank1990.common.enums.EntityType;

import javax.swing.*;

public class SteelWall extends Environment{
    public SteelWall(int x, int y) {
        super(EntityType.STEEL,false, false, true, x, y);
        health= 1;
        image = new ImageIcon("src/main/resources/images/wall_steel.png").getImage();
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

