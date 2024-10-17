package tank1990.objects.environments;

import tank1990.common.classes.GameEntity;
import tank1990.common.classes.Vector2D;
import tank1990.common.constants.GameConstants;
import tank1990.common.enums.EntityType;

public abstract class Environment extends GameEntity {
	public String name = getClass().getSimpleName();
	public boolean crossable;
	public boolean destroyable;
	public boolean bulletThrough;
	public int[][] health;
	public int x;
	public int y;

	public Environment(EntityType type, boolean crossable, boolean destroyable, boolean bulletThrough, int x, int y) {
		super(type, new Vector2D(x, y), GameConstants.ENTITY_WIDTH, GameConstants.ENTITY_HEIGHT);
		this.crossable = crossable;
		this.destroyable = destroyable;
		this.bulletThrough = bulletThrough;
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "Environment{" +
				"name='" + name + '\'' +
				", crossable=" + crossable +
				", destroyable=" + destroyable +
				", health=" + health +
				'}';
	}

}
