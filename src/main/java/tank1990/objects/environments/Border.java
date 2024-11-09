package tank1990.objects.environments;

import tank1990.common.classes.GameSprite;
import tank1990.common.enums.EntityType;

public class Border extends SteelWall{
    public Border(int x, int y){
        super(x, y);
        type = EntityType.BORDER;
        sprite = new GameSprite("src/main/resources/images/border.png");
    }
}
