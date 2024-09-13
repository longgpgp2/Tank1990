package tank1990.objects.environments;

public abstract class Environment {
		private String name = getClass().getSimpleName();
		private boolean crossable;
		private boolean destroyable;
		private boolean bulletThrough;
		private int[][] health;
		private int x;
		private int y;

	public Environment(boolean crossable, boolean destroyable, boolean bulletThrough, int x, int y) {
		this.crossable = crossable;
		this.destroyable = destroyable;
		this.bulletThrough = bulletThrough;
		this.x = x;
		this.y = y;
	}
	public Environment(){

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getCrossable() {
		return crossable;
	}

	public void setCrossable(boolean crossable) {
		this.crossable = crossable;
	}

	public boolean getDestroyable() {
		return destroyable;
	}

	public void setDestroyable(boolean destroyable) {
		this.destroyable = destroyable;
	}

	public int[][] getHealth() {
		return health;
	}

	public void setHealth(int[][] health) {
		this.health = health;
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

	public boolean isCrossable() {
		return crossable;
	}

	public boolean isDestroyable() {
		return destroyable;
	}

	public boolean isBulletThrough() {
		return bulletThrough;
	}

	public void setBulletThrough(boolean bulletThrough) {
		this.bulletThrough = bulletThrough;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
