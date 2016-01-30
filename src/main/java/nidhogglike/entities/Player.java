package nidhogglike.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.net.URL;

import gameframework.base.ObjectWithBoundedBox;
import gameframework.drawing.DrawableImage;
import gameframework.drawing.SpriteManager;
import gameframework.drawing.SpriteManagerDefaultImpl;
import gameframework.game.GameEntity;
import gameframework.motion.GameMovableDriverDefaultImpl;
import gameframework.motion.MoveStrategyConfigurableKeyboard;
import gameframework.motion.blocking.MoveBlocker;
import gameframework.motion.overlapping.Overlappable;

import nidhogglike.Nidhogg;
import nidhogglike.game.NidhoggGameData;
import nidhogglike.game.NidhoggUniverse;
import nidhogglike.input.Input;
import nidhogglike.motion.NidhoggMovable;
import nidhogglike.particles.ParticleEmitter;
import nidhogglike.particles.behaviors.DelayedParticle;
import nidhogglike.particles.behaviors.DyingParticle;
import nidhogglike.particles.behaviors.GravityParticle;
import nidhogglike.particles.behaviors.MovingParticle;
import nidhogglike.particles.behaviors.ParticleBehavior;




/**
 * @author Team 2
 *
 *         Class representing a player controlled by the keyboard
 */
public class Player extends NidhoggMovable implements GameEntity, Overlappable {
	protected static final int DUCKING_HEIGHT_OFFSET = 25;
	protected static final int HITBOX_HEIGHT = 50;
	protected static final int DEFAULT_SPEED = 8;
	protected static final int DUCKING_SPEED = 3;
	protected static final int LIFE = 3;
	protected float velocity_y;
	protected float fakeVelocity_x;
	
	private static float ACCELERATION = 0.5f;
	private static float VELOCITY_Y_MAX = 10;
	private static float VELOCITY_X_MAX = 15;

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
	private boolean ducking;
	private boolean headingLeft;
	private int boundingBoxHeight;
	protected String spriteTypePrefix;
	private String observableDataKey;
	private Point respawnPosition;
	private ParticleEmitter particleEmitter;
	private ParticleBehavior dyingParticleBehavior;
	private Color color;
	private SurpriseGift surpriseGift;
	private int maxLife;
	private int currentLife;
	private int invulnerabilityTime;

	public Player(final NidhoggGameData data, final Input input, final boolean isPlayer1) {
		super(new GameMovableDriverDefaultImpl());
		this.maxLife = this.currentLife = LIFE;
		invulnerabilityTime = 0;
		if (isPlayer1) {
			color = new Color(255, 160, 64);
			initPlayer(Nidhogg.PLAYER1_DATA_KEY, new Point(75, 0), KeyEvent.VK_Z, KeyEvent.VK_Q, KeyEvent.VK_S,
					KeyEvent.VK_D, KeyEvent.VK_A, input, data, "/images/player1.png");
			headingLeft = false;
			sprite.setType("headingRight");
		} else {
			color = new Color(145, 63, 160);
			initPlayer(Nidhogg.PLAYER2_DATA_KEY, new Point(Nidhogg.WIDTH - 125, 0), KeyEvent.VK_UP, KeyEvent.VK_LEFT,
					KeyEvent.VK_DOWN, KeyEvent.VK_RIGHT, KeyEvent.VK_SHIFT, input, data, "/images/player2.png");
		}
	}

	protected void initPlayer(final String observableDataKey, final Point respawnPosition, final int keyUp,
			final int keyLeft, final int keyDown, final int keyRight, final int throwKey, final Input input,
			final NidhoggGameData data, final String spritePath) {
		jumping = false;
		incrementStep = 0;
		this.input = input;
		this.data = data;
		final URL playerImage = this.getClass().getResource(spritePath);
		final DrawableImage drawableImage = new DrawableImage(playerImage, data.getCanvas());
		sprite = new SpriteManagerDefaultImpl(drawableImage, 50, 2);
		sprite.setTypes("headingLeft", "headingRight", "duckingLeft", "duckingRight");
		sprite.setType("headingLeft");
		spriteTypePrefix = "heading";
		headingLeft = true;
		ducking = false;
		setupKeys(keyUp, keyLeft, keyDown, keyRight, throwKey);
		this.observableDataKey = observableDataKey;
		this.respawnPosition = respawnPosition;
		boundingBoxHeight = HITBOX_HEIGHT;
		resetPosition();
	}

	protected void resetPosition() {
		this.getPosition().x = respawnPosition.x;
		this.getPosition().y = respawnPosition.y;
	}

	protected void setupKeys(final int keyUp, final int keyLeft, final int keyDown, final int keyRight,
			final int throwKey) {
		final MoveStrategyConfigurableKeyboard strategyKeyboard = new MoveStrategyConfigurableKeyboard(false);
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
		return new Rectangle(40, boundingBoxHeight);
	}

	@Override
	public void oneStepMoveAddedBehavior() {
		if (invulnerabilityTime > 0) {
			--invulnerabilityTime;
		}
		
		if (input.isPressed(jumpKey) && jumpHeight < JUMP_HEIGHT) {
			velocity_y = -10;
			jumping = true;
			++jumpHeight;
		} else if (input.isPressed(throwKey) && isHoldingSword()) {
			// Sword throwing
			sword.playerThrow();
			sword = null;
		}

		if (input.isPressed(duckKey)) {
			if (!ducking)
				duck();
			moveDriver.getSpeedVector(this).setSpeed(DUCKING_SPEED);
		} else {
			if (ducking)
				unduck();
			moveDriver.getSpeedVector(this).setSpeed(DEFAULT_SPEED);
		}

		if (isHeadingLeft())
			sprite.setType(spriteTypePrefix + "Left");
		else
			sprite.setType(spriteTypePrefix + "Right");

		// Apply gravity
		applyGravity();

		// When the player goes out of bounds
		outOfBoundsVerification();

		updateDirection();
		updateAnimation();
	}

	protected void outOfBoundsVerification() {
		if (this.getPosition().x > Nidhogg.WIDTH) {
			this.getPosition().x = -this.getBoundingBox().width;

		} else if (this.getPosition().x < -this.getBoundingBox().width) {
			this.getPosition().x = Nidhogg.WIDTH;
		}
		// sword.outOfBoundsVerification();
	}

	protected void duck() {
		ducking = true;
		getPosition().y += DUCKING_HEIGHT_OFFSET;
		boundingBoxHeight = HITBOX_HEIGHT - DUCKING_HEIGHT_OFFSET;
		spriteTypePrefix = "ducking";
	}

	protected void unduck() {
		ducking = false;
		getPosition().y -= DUCKING_HEIGHT_OFFSET;
		boundingBoxHeight = HITBOX_HEIGHT;
		spriteTypePrefix = "heading";
	}

	protected void applyGravity() {
		velocity_y += GRAVITY;
		velocity_y = Math.min(velocity_y, VELOCITY_Y_MAX);
		this.getPosition().y += velocity_y;
	}

	public void groundCollision(final MoveBlocker platform) {
		// Collision with the ground
		this.getPosition().y = platform.getBoundingBox().y - this.getBoundingBox().height;
		jumping = false;
		jumpHeight = 0;
		velocity_y = 0;
	}

	protected void updateDirection() {
		if (speedVector.getDirection().x != 0) {
			headingLeft = speedVector.getDirection().x < 0;
			fakeVelocity_x += ACCELERATION;
			fakeVelocity_x = Math.min(fakeVelocity_x, VELOCITY_X_MAX);
			sprite.setType(spriteTypePrefix + (headingLeft ? "Left" : "Right"));
		} else {
			fakeVelocity_x = 0;
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
	public void draw(final Graphics g) {
		if (invulnerabilityTime % 4 == 0) {
			sprite.draw(g, position);
		}
	}
	
	/**
	 * Set a surprise gift to the player
	 * 
	 * @param surpriseGift the surprise gift
	 */
	public void setSurpriseGift(SurpriseGift surpriseGift) {
		this.surpriseGift = surpriseGift;
	}

	public boolean isHoldingSword() {
		return sword != null;
	}

	public boolean isDucking() {
		return ducking;
	}

	public void setSword(final Sword sword) {
		sword.setHolder(this);
		this.sword = sword;
	}

	public boolean isHeadingLeft() {
		return headingLeft;
	}
	
	public void emitParticle() {
		// Particles
		particleEmitter.emit(color, this.getPosition().x + this.getBoundingBox().width / 2,
				this.getPosition().y + this.getBoundingBox().height / 2, 20, dyingParticleBehavior);
	}
	
	public boolean hit() {
		if (invulnerabilityTime > 0) {
			return false;
		}
		
		emitParticle();
		
		currentLife -= 1;
		if (currentLife <= 0) {
			die();
			return true;
		} else {
			invulnerabilityTime = 60;
		}
		return false;
	}

	public void die() {
		if (invulnerabilityTime > 0) {
			return;
		}

		// Head
		data.getUniverse().addGameEntity(new HeadBalloon(data, this.getPosition().x, this.getPosition().y, color));

		data.incrementObservableValue(observableDataKey, 1);
		// Respawn
		resetPosition();
		this.currentLife = this.maxLife;

		recoverSwordIfNeeded();

		//For the SurpriseGift
		addGift();
		this.surpriseGift.reduceTime();
	}

	protected void recoverSwordIfNeeded() {
		if (sword == null) {
			final Sword sword = ((NidhoggUniverse) data.getUniverse()).getFreeSword();
			if (sword != null) {
				setSword(sword);
			}
		}
		
		
	}

	/**
	 * Add a new gift in the SurpriseGift, it appears with a random coordinate
	 */
	protected void addGift() {
		int score = this.data.getObservableValue(observableDataKey).getValue();
		if (score % 3 == 2) {
			int alea = 50 + (int)(Math.random()*400);
			this.surpriseGift.setGift(alea, this);
		}

	}

	public void refinePositionAfterLateralCollision(final ObjectWithBoundedBox collisioner) {
		final boolean leftCollision = this.getPosition().x > collisioner.getBoundingBox().x;

		if (leftCollision) {
			getPosition().x = collisioner.getBoundingBox().x + collisioner.getBoundingBox().width + 10;
			if (isHoldingSword() && isHeadingLeft()) {
				getPosition().x += sword.getBoundingBox().width;
			}
		} else {
			getPosition().x = collisioner.getBoundingBox().x - this.getBoundingBox().width - 10;
			if (isHoldingSword() && !isHeadingLeft()) {
				getPosition().x -= sword.getBoundingBox().width;
			}
		}
		applyGravity();
	}

	public void roofCollision(final Platform platform) {
		velocity_y = 0;
		jumpHeight = JUMP_HEIGHT;
		this.getPosition().y += 5;
	}

	public float getVelocityY() {
		return velocity_y;
	}
	
	public float getFakeVelocityX() {
		return fakeVelocity_x;
	}

	public void setParticleEmitter(final ParticleEmitter emitter) {
		this.particleEmitter = emitter;
		dyingParticleBehavior = new MovingParticle(null, 7, -Math.PI * 6 / 10, -Math.PI * 4 / 10);
		dyingParticleBehavior = new DyingParticle(dyingParticleBehavior, 300, false);
		dyingParticleBehavior = new GravityParticle(dyingParticleBehavior, 100, 250);
		dyingParticleBehavior = new DelayedParticle(dyingParticleBehavior, 2);
	}
	

	/**
	 * Modification of the score
	 * 
	 * @param number the number that you want add, it can be negative
	 */
	public void modificationScore(int number) {
		if (number > 0) {
			if (this.currentLife == 3) {
				int score = this.data.getObservableValue(observableDataKey).getValue() - 1;
				this.currentLife = 3;
				if (score >= 0) {
					this.data.getObservableValue(observableDataKey).setValue(score);
					this.currentLife = 2;
				}
			} else {
				this.currentLife++;
			}
		} else {
			if (this.currentLife == 1) {
				int score = this.data.getObservableValue(observableDataKey).getValue() + 1;
				this.data.getObservableValue(observableDataKey).setValue(score);
				this.currentLife = 3;
			} else {
				this.currentLife--;
			}
		}
	}

	/**
	 * The player is taking the Surprise Gift
	 * 
	 * @param s the SurpriseGift
	 */
	public void isTakingGift(SurpriseGift s) {
		s.takingGift(this);
	}

	public boolean isKilledBy(final Player killer) {
		final boolean goOppositeDirection = killer.isHeadingLeft() != this.isHeadingLeft();

		return !goOppositeDirection;
	}

	public Sword getSword() {
		return sword;
	}

	public void pushBackwards() {
		final int moveSpeed = getSpeedVector().getSpeed();
		if (isHeadingLeft()) {
			getPosition().x += moveSpeed;
		} else {
			getPosition().x -= moveSpeed;
		}
		outOfBoundsVerification();
	}

	public boolean isJumping() {
		return jumping;
	}

	public int getMaxLife() {
		return maxLife;
	}

	public void setMaxLife(int maxLife) {
		this.maxLife = maxLife;
	}

	public int getCurrentLife() {
		return currentLife;
	}

	public void setCurrentLife(int currentLife) {
		this.currentLife = currentLife;
	}
	
	
}
