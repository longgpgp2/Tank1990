package tank1990.objects.environments;

import tank1990.common.enums.EntityType;

import javax.swing.*;

public class Trees extends Environment {
    public Trees(int x, int y) {
        super(EntityType.TREE, true, false, true, x, y);
        health = 1;
        image = new ImageIcon("src/main/resources/images/trees.png").getImage();
    }
}
