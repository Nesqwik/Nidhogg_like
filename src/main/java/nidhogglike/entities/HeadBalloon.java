package nidhogglike.entities;

import gameframework.base.ObjectWithBoundedBox;
import gameframework.game.GameEntity;
import gameframework.motion.overlapping.Overlappable;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import nidhogglike.Nidhogg;
import nidhogglike.entities.obstacles.Platform;
import nidhogglike.game.NidhoggGameData;
import nidhogglike.motion.NidhoggMovable;

public class HeadBalloon extends NidhoggMovable implements Overlappable, GameEntity {

	private static final int INITIAL_TTL = 10 * 600;

	private static final float VELOCITY_MIN = 1f;
	private static final float GRAVITY = 1f;
	private static final float ENERGY_LOSSES = 0.1f;
	private static final float VELOCITY_Y_MAX = 10;
	private static final int BALLOON_SIZE = 10;

	private int current_ttl = INITIAL_TTL;
	private float velocity_x;
	private float velocity_y;
	private float timeBeforeShot = 0;
	private final Color color;
	private final NidhoggGameData data;

	public HeadBalloon(final NidhoggGameData data, final int x, final int y, final Color color) {
		setPosition(new Point(x, y));
		velocity_x = getRandomSpeed(-10, 10);
		velocity_y = getRandomSpeed(-10, 10);

		this.data = data;
		this.color = color;
	}

	private float getRandomSpeed(final float min, final float max) {
		return min + (float)(Math.random() * ((max - min) + 1));
	}

	@Override
	public Rectangle getBoundingBox() {
		return new Rectangle(getPosition().x, getPosition().y, BALLOON_SIZE, BALLOON_SIZE);
	}

	@Override
	public void draw(final Graphics g) {
		g.setColor(color);
		g.fillRect(position.x, position.y, BALLOON_SIZE, BALLOON_SIZE);
		decrementTTL();

	}

	@Override
	public void oneStepMoveAddedBehavior() {

		applyGravity();
		this.getPosition().x += velocity_x;
		timeBeforeShot--;

		if(Math.abs(velocity_x) < VELOCITY_MIN) {
			velocity_x = 0;
		}

		if(Math.abs(velocity_y) < VELOCITY_MIN) {
			velocity_y = 0;
		}


		// When the ball goes out of bounds
		if(this.getPosition().x > Nidhogg.WIDTH) {
			this.getPosition().x = -this.getBoundingBox().width;

		} else if(this.getPosition().x < -this.getBoundingBox().width) {
			this.getPosition().x = Nidhogg.WIDTH;
		}

	}


	public void applyGravity() {
		// Apply gravity
		velocity_y += GRAVITY;
		velocity_y = Math.min(velocity_y, VELOCITY_Y_MAX);
		this.getPosition().y += velocity_y;
	}

	public float getVelocityY() {
		return velocity_y;
	}

	public float getVelocityX() {
		return velocity_x;
	}

	public void platformCollision(final Platform platform) {
		this.getPosition().y -= velocity_y;
		energyLossesY();
		energyLossesX();
		velocity_y = - velocity_y;
	}

	public void energyLossesY() {
		if(velocity_y > 0) {
			velocity_y -= GRAVITY;
		} else {
			velocity_y += GRAVITY;
		}
	}

	public void energyLossesX() {
		if(velocity_x > 0) {
			velocity_x -= ENERGY_LOSSES;
		} else {
			velocity_x += ENERGY_LOSSES;
		}
	}

	public void handleCollision(final ObjectWithBoundedBox boundedObject) {
		// There's 4 possibilities for the new position of the ball, at the left, the right, the top or the bottom of the platform
		final int newX1 = boundedObject.getBoundingBox().x - getBoundingBox().width;
		final int newX2 = boundedObject.getBoundingBox().x + boundedObject.getBoundingBox().width;
		final int newY1 = boundedObject.getBoundingBox().y - getBoundingBox().height;
		final int newY2 = boundedObject.getBoundingBox().y + boundedObject.getBoundingBox().height;

		// We choose the one that is the nearest to the ball's actual position
		final int deltaX1 = (int) Math.abs(this.getPosition().x  - newX1 - velocity_x);
		final int deltaX2 = (int) Math.abs(this.getPosition().x  - newX2  - velocity_x);
		final int deltaY1 = (int) Math.abs(this.getPosition().y  - newY1 - velocity_y);
		final int deltaY2 = (int) Math.abs(this.getPosition().y  - newY2 - velocity_y);

		if (Math.min(deltaY1, deltaY2) < Math.min(deltaX1, deltaX2)) {
			this.getPosition().y = deltaY1 < deltaY2 ? newY1 : newY2;
			energyLossesY();
			velocity_y = - velocity_y;
		} else {
			this.getPosition().x = deltaX1 < deltaX2 ? newX1 : newX2;
			energyLossesX();
			velocity_x = - velocity_x;
		}
	}

	private void decrementTTL() {
		current_ttl--;
		if(current_ttl == 0) {
			destroy();
		}
	}

	public void destroy() {
		data.getUniverse().removeGameEntity(this);
	}

	private boolean canBeShot() {
		return timeBeforeShot <= 0;
	}
	
	protected Player playerShooter = null;

	public void shoot() {
		if(canBeShot()) {
			float speed = (playerShooter.isDucking() ? 0.5f : 1f);
			speed *= (playerShooter.getFakeVelocityX() + 1);
			velocity_x = (playerShooter.isHeadingLeft() ? -speed : speed);
			velocity_y = playerShooter.getFakeVelocityX() == 0 ? 0 :  getRandomSpeed(20, 27);
			timeBeforeShot = 20f;
		}
	}

	public void playerCollision(Player player) {
		if (playerShooter == null) {
			playerShooter = player;
			shoot();
		} else {
			player.hitByBalloon();
			handleCollision(playerShooter);
			playerShooter = null;
		}
	}
}
