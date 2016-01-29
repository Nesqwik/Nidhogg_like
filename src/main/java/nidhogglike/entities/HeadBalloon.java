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

	private static final int INITIAL_TTL = 20;
	private int current_ttl = INITIAL_TTL;
	private float velocity_x;
	private float velocity_y;
	private static final float VELOCITY_MIN = 1f;
	private static final float GRAVITY = 1f;
	private static final float ENERGY_LOSSES = 0.1f;
	private static float VELOCITY_Y_MAX = 10;
	private final int BALLOON_SIZE = 10;
	private Color color;
	private final NidhoggGameData data;
	
	public HeadBalloon(final NidhoggGameData data, int x, int y, Color color) {
		setPosition(new Point(x, y));
		velocity_x = 5f;
		this.data = data;
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
		System.out.println(this.getPosition().x + "-" + velocity_x);
		this.getPosition().x -= velocity_x;
		System.out.println("= " +this.getPosition().x);
		energyLossesX();
		velocity_x = - velocity_x;
		decrementTTL();
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

	public void isShotBy(Player player) {
		decrementTTL();
		velocity_y = 25;
		float yspeed = (player.isDucking() ? 4 : 8);
		velocity_x = (player.isHeadingLeft() ? -yspeed : yspeed);
	}
}
