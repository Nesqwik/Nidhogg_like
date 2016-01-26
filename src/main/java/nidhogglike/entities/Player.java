package nidhogglike.entities;

import gameframework.drawing.DrawableImage;
import gameframework.drawing.SpriteManager;
import gameframework.drawing.SpriteManagerDefaultImpl;
import gameframework.game.GameEntity;
import gameframework.motion.GameMovableDriverDefaultImpl;
import gameframework.motion.MoveStrategyConfigurableKeyboard;
import gameframework.motion.blocking.MoveBlocker;
import gameframework.motion.overlapping.Overlappable;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.net.URL;

import nidhogglike.Nidhogg;
import nidhogglike.game.NidhoggGameData;
import nidhogglike.game.NidhoggUniverse;
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
	private NidhoggGameData data;
	private int jumpKey;
	private int duckKey;
	private int throwKey;
	private boolean headingLeft;
	private String observableDataKey;
	private Point respawnPosition;
	
	public Player(NidhoggGameData data, Input input, boolean isPlayer1) {
		super(new GameMovableDriverDefaultImpl());

		if (isPlayer1) {
			initPlayer(Nidhogg.PLAYER1_DATA_KEY, new Point(75, 0), KeyEvent.VK_Z, KeyEvent.VK_Q, KeyEvent.VK_S,
					KeyEvent.VK_D, KeyEvent.VK_A, input, data, "/images/player1.png");
			headingLeft = false;
			sprite.setType("headingRight");
		} else {
			initPlayer(Nidhogg.PLAYER2_DATA_KEY, new Point(Nidhogg.WIDTH - 125, 0), KeyEvent.VK_UP, KeyEvent.VK_LEFT,
					KeyEvent.VK_DOWN, KeyEvent.VK_RIGHT, KeyEvent.VK_SHIFT, input, data, "/images/player2.png");
		}
	}
	
	protected void initPlayer(String observableDataKey,Point respawnPosition, int keyUp, int keyLeft, int keyDown, int keyRight, int throwKey, 
			Input input, NidhoggGameData data, String spritePath) {
		jumping = false;
		incrementStep = 0;
		this.input = input;
		this.data = data;
		URL playerImage = this.getClass().getResource(spritePath);
		DrawableImage drawableImage = new DrawableImage(playerImage, data.getCanvas());
		sprite = new SpriteManagerDefaultImpl(drawableImage, 50, 2);
		sprite.setTypes("headingLeft", "headingRight");
		sprite.setType("headingLeft");
		headingLeft = true;
		setupKeys(keyUp, keyLeft, keyDown, keyRight, throwKey);
		this.observableDataKey = observableDataKey;
		this.respawnPosition = respawnPosition;
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
		return new Rectangle(isHeadingLeft() ? 50 : 40, 50);
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
		data.incrementObservableValue(observableDataKey, 1);
		// Respawn
		this.getPosition().x = respawnPosition.x;
		this.getPosition().y = respawnPosition.y;
		
		Sword sword = ((NidhoggUniverse) data.getUniverse()).getFreeSword();
		if (sword != null)
			setSword(sword);
	}

	public void refinePositionAfterLateralCollision(Platform platform) {
		boolean leftCollision = this.getPosition().x > platform.getBoundingBox().x;
		if (leftCollision) {
			getPosition().x += 1;
		} else {
			getPosition().x -= 1;
		}
		applyGravity();
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
