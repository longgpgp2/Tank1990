package tank1990.objects.environments;

import tank1990.common.enums.EntityType;

import javax.swing.*;

public class Trees extends Environment{
    int[][] health = {{1, 1, 1, 1}, {1, 1, 1, 1}, {1, 1, 1, 1}, {1, 1, 1, 1}};

    public Trees(int x, int y) {
        super(EntityType.TREE,false, false, true, x, y);
        super.health= health;
        image = new ImageIcon("src/main/resources/images/trees.png").getImage();
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
