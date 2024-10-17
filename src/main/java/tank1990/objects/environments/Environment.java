package tank1990.objects.environments;

import tank1990.main.GamePanel;
import tank1990.objects.common.Entity;
import tank1990.objects.common.constants.GameConstants;

public abstract class Environment extends Entity {
		public String name = getClass().getSimpleName();
		public boolean crossable;
		public boolean destroyable;
		 public boolean bulletThrough;
		 public int[][] health;
		 public int x;
		 public int y;

	public Environment(boolean crossable, boolean destroyable, boolean bulletThrough, int x, int y) {
		super(x, y, GameConstants.ENTITY_WIDTH, GameConstants.ENTITY_HEIGHT);
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
