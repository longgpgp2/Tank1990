package tank1990.objects.environments;

import tank1990.common.classes.GameEntity;
import tank1990.common.classes.GameSprite;
import tank1990.common.classes.Vector2D;
import tank1990.common.enums.EntityType;
import tank1990.common.interfaces.DestructibleEntity;
import tank1990.manager.GameEntityManager;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class BrickWall extends Environment implements DestructibleEntity {
	public boolean destroyed = false;
	public int health = 2;

	private Vector2D damagedDirection = new Vector2D(0, 0);
	private BufferedImage maskImage;

	public BrickWall(int x, int y) {
		super(EntityType.BRICK, false, true, true, x, y);
		sprite = new GameSprite("src/main/resources/images/wall_brick.png");
	}

	@Override
	public void draw(Graphics2D graphics2d) {
		if (health > 1) {
			graphics2d.drawImage(sprite.getBufferedImage(), (int) x, (int) y, this);
		} else {
			drawDamagedBrick(graphics2d);
		}
	}

	@Override
	public void update(double deltaTime) {
		if (health <= 0) {
			destroy();
		}
	}

	@Override
	public void destroy() {
		maskImage = null;

		// setCollision(null); // can disable collision box instead
		getCollision().setEnabled(false);
		setSprite(null);
		image=null;
		destroyed = true;
	}

	@Override
	public void hit(int damage) {
		health -= damage;
	}

	public void hitComponent(GameEntity gameComponent) {
		Vector2D direction = gameComponent.getCenter().minus(this.center);

		boolean isHorizontal = Math.abs(direction.x) > Math.abs(direction.y);

		if (isHorizontal) {
			damagedDirection = new Vector2D(-direction.x, 0);
		} else {
			damagedDirection = new Vector2D(0, -direction.y);
		}
	}

	private void drawDamagedBrick(Graphics2D graphics2d) {
		if (maskImage != null) {
			sprite.applyGrayscaleMaskToAlpha(maskImage);
			graphics2d.drawImage(sprite.getBufferedImage(), (int) x, (int) y, this);
			return;
		}

		if (damagedDirection == null) {
			return;
		}

		Vector2D damagedRelativeDirection = damagedDirection.multiply(-1);

		/*
		 * Draw everything with just half width/height and
		 * change position by half of width/height
		 */
		if (damagedRelativeDirection.x > 0) { // right side dmg
			maskImage = GameSprite.createdGrayScaleMask(0, 0, width / 2, height, sprite);
		} else if (damagedRelativeDirection.x < 0) { // left side dmg
			maskImage = GameSprite.createdGrayScaleMask(width / 2, 0, width / 2, height, sprite);
		} else if (damagedRelativeDirection.y > 0) { // bottom side dmg
			maskImage = GameSprite.createdGrayScaleMask(0, 0, width, height / 2, sprite);
		} else if (damagedRelativeDirection.y < 0) { // top side dmg
			maskImage = GameSprite.createdGrayScaleMask(0, height / 2, width, height / 2, sprite);
		}

		damagedDirection = null;
	}
}
