package nidhogglike.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import gameframework.game.GameEntity;
import gameframework.motion.overlapping.Overlappable;
import nidhogglike.Nidhogg;
import nidhogglike.motion.NidhoggMovable;

public class HeadBalloon extends NidhoggMovable implements Overlappable, GameEntity {

	private float velocity_x;
	private float velocity_y;
	private static final float VELOCITY_MIN = 1f;
	private static final float GRAVITY = 1f;
	private static final float ENERGY_LOSSES = 0.1f;
	private static float VELOCITY_Y_MAX = 10;
	private final int BALLOON_SIZE = 10;
	private Color color;
	
	public HeadBalloon(int x, int y, Color color) {
		setPosition(new Point(x, y));
		velocity_x = 5f;
		
		this.color = color;
	} 
	
	@Override
	public Rectangle getBoundingBox() {
		return new Rectangle(BALLOON_SIZE, BALLOON_SIZE);
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillRect(position.x, position.y, BALLOON_SIZE, BALLOON_SIZE);
	}

	@Override
	public void oneStepMoveAddedBehavior() {
		applyGravity();
		this.getPosition().x += velocity_x;
		
		if(Math.abs(velocity_x) < VELOCITY_MIN) {
			velocity_x = 0;
		}
		
		if(Math.abs(velocity_y) < VELOCITY_MIN) {
			velocity_y = 0;
		}
		
		
		// When the player goes out of bounds
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
		if(velocity_y > 0) {
			this.getPosition().y += -velocity_y;
			velocity_y -= GRAVITY;
		} else {
			this.getPosition().y += velocity_y;
			velocity_y += GRAVITY;
		}
		velocity_y = - velocity_y;
	}

	public void LateralCollision(Platform platform) {
		if(velocity_x > 0) {
			this.getPosition().x += -velocity_x;
			velocity_x -= ENERGY_LOSSES;
		} else {
			this.getPosition().x += velocity_x;
			velocity_x += ENERGY_LOSSES;
		}
		velocity_x = - velocity_x;
	}

	public void isShootedBy(Player player) {
		velocity_y = -25;
		velocity_x = (player.isHeadingLeft() ? -8 : 8);
	}
}
