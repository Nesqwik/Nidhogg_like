package nidhogglike.entities;

import gameframework.drawing.DrawableImage;
import gameframework.drawing.SpriteManager;
import gameframework.drawing.SpriteManagerDefaultImpl;
import gameframework.game.GameData;
import gameframework.game.GameEntity;
import gameframework.motion.GameMovableDriverDefaultImpl;
import gameframework.motion.blocking.MoveBlocker;
import gameframework.motion.overlapping.Overlappable;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.net.URL;

import nidhogglike.Nidhogg;
import nidhogglike.motion.NidhoggMovable;

public class Sword extends NidhoggMovable implements GameEntity, Overlappable {
	private Player holder;
	private final SpriteManager sprite;
	private static float GRAVITY = 1f;
	private static float VELOCITY_Y_MAX = 5;
	private static float SPEED_X = 18;
	private static int GRAVITY_DELAY = 20;  // Used to add delay before applying gravity when the sword is thrown

	private float velocity_x;
	private float velocity_y;
	private boolean isHeadingLeft;
	private int gravityDelay;
	private boolean isMoving = false;

	private Player lastHolder = null;

	public Sword(final GameData data, final boolean isHeadingLeft){
		super(new GameMovableDriverDefaultImpl());
		final URL playerImage = this.getClass().getResource("/images/sword.png");
		final DrawableImage drawableImage = new DrawableImage(playerImage, data.getCanvas());
		sprite = new SpriteManagerDefaultImpl(drawableImage, 50, 1);
		sprite.setTypes("left", "right");
		sprite.setType("left");
		this.isHeadingLeft = isHeadingLeft;
		velocity_y = 0;
	}

	@Override
	public Rectangle getBoundingBox() {
		return new Rectangle(50, 10);
	}

	@Override
	public void draw(final Graphics g) {
		if (isHeld()) {
			position.x = holder.getPosition().x;
			if (isHeadingLeft) {
				position.x -= holder.getBoundingBox().width + 4;
			} else {
				position.x += holder.getBoundingBox().width - 10;
			}
			position.y = holder.getPosition().y + 11;
		}
		sprite.draw(g, position);

	}

	public boolean isHeld() {
		return holder != null;
	}

	@Override
	public void oneStepMoveAddedBehavior() {
		if (!isHeld()) {
			this.getPosition().x += velocity_x * (isHeadingLeft ? -1 : 1);
			gravityDelay -= 1;

			if (gravityDelay <= 0) {
				applyGravity();
			}

			// When the sword goes out of bounds
			outOfBoundsVerification();
		} else {
			isHeadingLeft = holder.isHeadingLeft();

			sprite.setType(isHeadingLeft ? "left" : "right");
		}
	}

	protected void outOfBoundsVerification() {
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

	public void groundCollision(final MoveBlocker platform) {
		// Collision with the ground
		this.getPosition().y = platform.getBoundingBox().y - this.getBoundingBox().height;
		velocity_y = 0;
		velocity_x = 0;
		setMoving(false);
	}

	public void setMoving(final boolean isMoving) {
		this.isMoving = isMoving;
		if (!isMoving) {
			velocity_x = 0;
			gravityDelay = 0;
		}
	}

	public boolean isMoving() {
		return this.isMoving;
	}

	public void playerThrow() {
		velocity_x = SPEED_X;
		gravityDelay = GRAVITY_DELAY;
		this.holder = null;
		setMoving(true);
	}

	public void setHolder(final Player player) {
		this.holder = player;
		lastHolder = player;
		setMoving(true);
	}

	public Player getLastHolder() {
		return lastHolder;
	}
	public Player getHolder() {
		return holder;
	}
}
