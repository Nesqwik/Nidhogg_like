package nidhogglike.entities;

import gameframework.drawing.DrawableImage;
import gameframework.drawing.SpriteManager;
import gameframework.drawing.SpriteManagerDefaultImpl;
import gameframework.game.GameData;
import gameframework.game.GameEntity;
import gameframework.motion.GameMovableDriverDefaultImpl;
import gameframework.motion.MoveStrategyConfigurableKeyboard;
import gameframework.motion.blocking.MoveBlocker;
import gameframework.motion.overlapping.Overlappable;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.net.URL;

import nidhogglike.Nidhogg;
import nidhogglike.game.NidhoggGameData;
import nidhogglike.input.Input;
import nidhogglike.motion.NidhoggMovable;


/**
 * @author Team 2
 *
 * Class representing a player controlled by the keyboard
 */
public class Player extends NidhoggMovable implements GameEntity, Overlappable {
	protected float velocity_y;
	private static float VELOCITY_Y_MAX = 10;
	private static float GRAVITY = 1f;
	private static int JUMP_HEIGHT = 10;
	private boolean jumping;
	private Sword sword;
	private int jumpHeight;
	private Input input;
	private SpriteManager sprite;
	private int incrementStep;
	private GameData data;
	private int jumpKey;
	private int duckKey;
	private int throwKey;
	private boolean headingLeft;
	private String observableDataKey;

	public Player(String observableDataKey, int keyUp, int keyLeft, int keyDown, int keyRight, int throwKey, Input input, GameData data) {
		super(new GameMovableDriverDefaultImpl());
		jumping = false;
		incrementStep = 0;
		this.input = input;
		this.data = data;
		URL playerImage = this.getClass().getResource("/images/player.png");
		DrawableImage drawableImage = new DrawableImage(playerImage, data.getCanvas());
		sprite = new SpriteManagerDefaultImpl(drawableImage, 50, 2);
		sprite.setTypes("headingLeft", "headingRight");
		sprite.setType("headingLeft");
		headingLeft = true;
		setupKeys(keyUp, keyLeft, keyDown, keyRight, throwKey);
		this.observableDataKey = observableDataKey;
	}

	protected void setupKeys(int keyUp, int keyLeft, int keyDown, int keyRight, int throwKey) {
		MoveStrategyConfigurableKeyboard strategyKeyboard = new MoveStrategyConfigurableKeyboard(false);
		strategyKeyboard.addKeyDirection(keyLeft, new Point(-1, 0));
		strategyKeyboard.addKeyDirection(keyRight, new Point(1, 0));
		data.getCanvas().addKeyListener(strategyKeyboard);
		moveDriver.setStrategy(strategyKeyboard);
		this.jumpKey = keyUp;
		this.duckKey = keyDown;
		this.throwKey = throwKey;
	}

	@Override
	public Rectangle getBoundingBox() {
		return new Rectangle(50, 50);
	}

	@Override
	public void oneStepMoveAddedBehavior() {
		if (input.isPressed(jumpKey) && jumpHeight < JUMP_HEIGHT) {
			velocity_y = -10;
			jumping = true;
			++jumpHeight;
		} else if(input.isPressed(throwKey) && isHoldingSword()){
			// Sword throwing
			sword.playerThrow();
			sword = null;
		}

		// Apply gravity
		applyGravity();

		// When the player goes out of bounds
		if(this.getPosition().x > Nidhogg.WIDTH) {
			this.getPosition().x = -this.getBoundingBox().width;

		} else if(this.getPosition().x < -this.getBoundingBox().width) {
			this.getPosition().x = Nidhogg.WIDTH;
		}

		updateDirection();
		updateAnimation();
	}

	protected void applyGravity() {
		velocity_y += GRAVITY;
		velocity_y = Math.min(velocity_y, VELOCITY_Y_MAX);
		this.getPosition().y += velocity_y;
	}

	public void groundCollision(MoveBlocker platform) {
		// Collision with the ground
		this.getPosition().y = platform.getBoundingBox().y - this.getBoundingBox().height;
		jumping = false;
		jumpHeight = 0;
		velocity_y = 0;
	}

	protected void updateDirection() {
		if (speedVector.getDirection().x != 0) {
			headingLeft = speedVector.getDirection().x < 0;

			sprite.setType(headingLeft ? "headingLeft" : "headingRight");
		}
	}

	protected void updateAnimation() {
		if (speedVector.getDirection().x != 0) {
			++incrementStep;
			if (incrementStep >= 10) {
				sprite.increment();
				incrementStep = 0;
			}
		} else {
			sprite.setIncrement(0);
		}
	}

	@Override
	public void draw(Graphics g) {
		sprite.draw(g, position);
	}

	public boolean isHoldingSword() {
		return sword != null;
	}

	public void setSword(Sword sword) {
		sword.setHolder(this);
		this.sword = sword;
	}

	public boolean isHeadingLeft() {
		return headingLeft;
	}

	public void die() {
		((NidhoggGameData)data).incrementObservableValue(observableDataKey, 1);
	}

	public void refinePositionAfterLateralCollision(Platform platform) {
		boolean leftCollision = this.getPosition().x > platform.getBoundingBox().x;
		if (leftCollision) {
			getPosition().x += 1;
		} else {
			getPosition().x -= 1;
		}
		applyGravity();
//		getPosition().x = leftCollision ?
//				platform.getBoundingBox().x + platform.getBoundingBox().width + 9 
//				: platform.getBoundingBox().x - this.getBoundingBox().width - 9; 
	}

	public void roofCollision(Platform platform) {
		velocity_y = 0;
		jumpHeight = JUMP_HEIGHT;
		this.getPosition().y += 5;
	}
	
	public float getVelocityY() {
		return velocity_y;
	}
}
