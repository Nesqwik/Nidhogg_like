package nidhogglike.entities;

import gameframework.drawing.DrawableImage;
import gameframework.drawing.SpriteManager;
import gameframework.game.GameData;
import gameframework.game.GameEntity;
import gameframework.motion.GameMovableDriverDefaultImpl;
import gameframework.motion.blocking.MoveBlocker;
import gameframework.motion.overlapping.Overlappable;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.net.URL;

import nidhogglike.motion.NidhoggMovable;

public abstract class MovableEntities extends NidhoggMovable implements GameEntity, Overlappable {
	private Player holder;
	protected SpriteManager sprite;
	private static float GRAVITY = 1f;
	private static float VELOCITY_Y_MAX = 5;	
	private float velocity_x;
	private float velocity_y;
	private int gravityDelay;
	private boolean isMoving = true;
	
	public MovableEntities(GameData data, String source, boolean isHeadingLeft){
		super(new GameMovableDriverDefaultImpl());
		this.holder = null;
		URL image = this.getClass().getResource(source);
		DrawableImage drawableImage = new DrawableImage(image, data.getCanvas());
		init(isHeadingLeft, drawableImage);
		velocity_y = 0;
	}

	protected abstract void init(boolean isHeadingLeft, DrawableImage drawableImage) ;
	
	@Override
	public Rectangle getBoundingBox() {
		return new Rectangle(50, 10);
	}

	public abstract void draw(Graphics g);

	public boolean isHeld() {
		return holder != null;
	}

	@Override
	public void oneStepMoveAddedBehavior() {
		if (!isHeld()) {
			this.getPosition().x += velocity_x * movePositionX();
			gravityDelay -= 1;
			
			if (gravityDelay <= 0) {
				applyGravity();
			}
			outOfBounds();
		} else {
			changeType();
		}
	}

	protected abstract void changeType();

	protected abstract void outOfBounds();

	protected abstract int movePositionX();
	

	public void applyGravity() {
		// Apply gravity
		velocity_y += GRAVITY;
		velocity_y = Math.min(velocity_y, VELOCITY_Y_MAX);
		this.getPosition().y += velocity_y;
	}
	
	public void groundCollision(MoveBlocker platform) {
		// Collision with the ground
		this.getPosition().y = platform.getBoundingBox().y - this.getBoundingBox().height;
		velocity_y = 0;
		velocity_x = 0;
		setMoving(false);
	}
	
	public void setMoving(boolean isMoving) {
		this.isMoving = isMoving;
		if (!isMoving) {
			velocity_x = 0;
			gravityDelay = 0;
		}
	}
	
	public boolean isMoving() {
		return this.isMoving;
	}

	public void setHolder(Player player) {
		this.holder = player;
	}
	
	public Player getHolder() {
		return holder;
	}
}

