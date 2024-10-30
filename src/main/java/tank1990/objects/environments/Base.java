package tank1990.objects.environments;

import tank1990.common.classes.GameEntity;
import tank1990.common.classes.Vector2D;
import tank1990.common.enums.EntityType;

import javax.swing.*;

public class Base extends Environment {

    public Base(int x, int y) {
        super(EntityType.BASE,false, true, false, x, y);
        health= 1;
        image = new ImageIcon("src/main/resources/images/base.png").getImage();
    }
}
