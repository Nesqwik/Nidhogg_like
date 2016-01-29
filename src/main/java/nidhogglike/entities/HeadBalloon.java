package nidhogglike.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import gameframework.game.GameEntity;
import gameframework.motion.overlapping.Overlappable;
import nidhogglike.Nidhogg;
import nidhogglike.game.NidhoggGameData;
import nidhogglike.motion.NidhoggMovable;

public class HeadBalloon extends NidhoggMovable implements Overlappable, GameEntity {

	private static final int INITIAL_TTL = 10 * 60;

	private static final float VELOCITY_MIN = 1f;
	private static final float GRAVITY = 1f;
	private static final float ENERGY_LOSSES = 0.1f;
	private static final float VELOCITY_Y_MAX = 10;
	private static final int BALLOON_SIZE = 10;
	
	private int current_ttl = INITIAL_TTL;
	private float velocity_x;
	private float velocity_y;
	private float timeBeforeShot = 0;
	private Color color;
	private final NidhoggGameData data;
	
	public HeadBalloon(final NidhoggGameData data, int x, int y, Color color) {
		setPosition(new Point(x, y));
		velocity_x = getRandomSpeed(-10, 10);
		velocity_y = getRandomSpeed(-10, 10);
		
		this.data = data;
		this.color = color;
	}
	
	private float getRandomSpeed(float min, float max) {
		return min + (float)(Math.random() * ((max - min) + 1));
	}
	
	@Override
	public Rectangle getBoundingBox() {
		return new Rectangle(BALLOON_SIZE, BALLOON_SIZE);
	}

	@Override
	public void draw(Graphics g) {
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

	public void platformCollision(Platform platform) {
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

	public void LateralCollision(Platform platform) {
		this.getPosition().x -= velocity_x;
		energyLossesX();
		velocity_x = - velocity_x;
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
	
	public void isShotBy(Player player) {
		if(canBeShot()) {
			float speed = (player.isDucking() ? 0.5f : 1f);
			speed *= (player.getFakeVelocityX() + 1);
			velocity_x = (player.isHeadingLeft() ? -speed : speed);
			velocity_y = getRandomSpeed(20, 27);
			timeBeforeShot = 90f;
		}
	}
}
