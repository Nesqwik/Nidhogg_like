package nidhogglike.entities;

import gameframework.drawing.DrawableImage;
import gameframework.drawing.SpriteManager;
import gameframework.drawing.SpriteManagerDefaultImpl;
import gameframework.game.GameEntity;
import gameframework.motion.GameMovableDriverDefaultImpl;
import gameframework.motion.MoveStrategyConfigurableKeyboard;
import gameframework.motion.blocking.MoveBlocker;
import gameframework.motion.overlapping.Overlappable;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import nidhogglike.Nidhogg;
import nidhogglike.entities.bars.InvincibleBar;
import nidhogglike.entities.bonus.BonusSword;
import nidhogglike.entities.bonus.SurpriseGift;
import nidhogglike.entities.obstacles.Platform;
import nidhogglike.game.NidhoggGameData;
import nidhogglike.game.NidhoggUniverse;
import nidhogglike.input.Input;
import nidhogglike.motion.NidhoggMovable;
import nidhogglike.particles.ParticleEmitter;
import nidhogglike.particles.behaviors.DefaultParticleBehavior;
import nidhogglike.particles.behaviors.DelayedParticleBehavior;
import nidhogglike.particles.behaviors.DyingParticleBehavior;
import nidhogglike.particles.behaviors.GravityParticleBehavior;
import nidhogglike.particles.behaviors.MovingParticleBehavior;
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
	private static final int MAXSTRONGERSWORD = 3;
	protected float MAXINVICIBLELIFE;
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
	private int boundingBoxHeight = 0;
	protected String spriteTypePrefix;
	private String observableDataKey;
	private Point respawnPosition;
	private ParticleEmitter particleEmitter;
	private ParticleBehavior dyingParticleBehavior;
	private Color color, headColor;
	private SurpriseGift surpriseGift;
	private int maxLife;
	private int currentLife;
	private int invulnerabilityTime;
	private float invincibleLife;
	private int initialPositionX;
	private final List<BonusSword> bonusSwords;

	private Rectangle boundingBox;

	public Player(final NidhoggGameData data, final Input input, final boolean isPlayer1) {
		super(new GameMovableDriverDefaultImpl());
		this.maxLife = this.currentLife = LIFE;
		this.bonusSwords = new ArrayList<BonusSword>();
		invulnerabilityTime = 0;
		if (isPlayer1) {
			initialPositionX = 75;
			color = new Color(223, 153, 65);
			headColor = new Color(239, 117, 44);
			initPlayer(Nidhogg.PLAYER1_DATA_KEY, new Point(initialPositionX, 0), KeyEvent.VK_Z, KeyEvent.VK_Q, KeyEvent.VK_S,
					KeyEvent.VK_D, KeyEvent.VK_A, input, data, "/images/player1.png");
			headingLeft = false;
			sprite.setType("headingRight");
		} else {
			initialPositionX = Nidhogg.WIDTH - 125;
			color = new Color(145, 63, 160);
			headColor = new Color(75, 39, 135);
			initPlayer(Nidhogg.PLAYER2_DATA_KEY, new Point(initialPositionX, 0), KeyEvent.VK_UP, KeyEvent.VK_LEFT,
					KeyEvent.VK_DOWN, KeyEvent.VK_RIGHT, KeyEvent.VK_SHIFT, input, data, "/images/player2.png");
		}
		boundingBox = new Rectangle(getPosition().x, getPosition().y, 40, HITBOX_HEIGHT);
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
		boundingBox = new Rectangle(getPosition().x, getPosition().y, boundingBox.width, boundingBoxHeight);
		return boundingBox;
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
	public void setSurpriseGift(final SurpriseGift surpriseGift) {
		this.surpriseGift = surpriseGift;
	}

	public boolean isHoldingSword() {
		return sword != null;
	}

	public boolean isDucking() {
		return ducking;
	}

	public void setSword(final Sword sword) {
		if (sword != null) {
			sword.setHolder(this);
		}
		this.sword = sword;
	}

	public boolean isHeadingLeft() {
		return headingLeft;
	}

	public void emitParticle() {
		// Particles
		final Rectangle rect = new Rectangle(this.getPosition().x + this.getBoundingBox().width / 2,
				this.getPosition().y + this.getBoundingBox().height / 2, 7, 7);

		particleEmitter.emit(color, rect, 20, dyingParticleBehavior);
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
		data.getUniverse().addGameEntity(new HeadBalloon(data, this.getPosition().x, this.getPosition().y, headColor));

		data.incrementObservableValue(observableDataKey, 1);
		// Respawn
		resetPosition();
		this.currentLife = this.maxLife;

		recoverSwordIfNeeded();

		//For the SurpriseGift
		if (surpriseGift != null ) {
			addGift();
			this.surpriseGift.reduceTime();
		}
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
		final int score = this.data.getObservableValue(observableDataKey).getValue();
		if (score % 3 == 2) {
			final int alea = 50 + (int)(Math.random()*400);
			this.surpriseGift.setGift(alea, this);
			data.getUniverse().addGameEntity(surpriseGift);
		}

	}

	public void refinePositionAfterLateralCollision(final Platform collisioner) {
		final boolean leftCollision = this.getPosition().x >
		collisioner.getBoundingBox().x + (collisioner.getBoundingBox().width / 2);

		if (leftCollision) {
			getPosition().x = collisioner.getBoundingBox().x + collisioner.getBoundingBox().width;
		} else {
			getPosition().x = collisioner.getBoundingBox().x - this.getBoundingBox().width;
		}
	}

	public float getVelocityY() {
		return velocity_y;
	}

	public float getFakeVelocityX() {
		return fakeVelocity_x;
	}

	public void setParticleEmitter(final ParticleEmitter emitter) {
		this.particleEmitter = emitter;
		dyingParticleBehavior = new DefaultParticleBehavior();
		dyingParticleBehavior = new MovingParticleBehavior(dyingParticleBehavior, 7, -Math.PI * 6 / 10, -Math.PI * 4 / 10);
		dyingParticleBehavior = new DyingParticleBehavior(dyingParticleBehavior, 300, false);
		dyingParticleBehavior = new GravityParticleBehavior(dyingParticleBehavior, 100f / 1000, 250f / 1000);
		dyingParticleBehavior = new DelayedParticleBehavior(dyingParticleBehavior, 2);
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

	public void setMaxLife(final int maxLife) {
		this.maxLife = maxLife;
	}

	public int getCurrentLife() {
		return currentLife;
	}

	public void setCurrentLife(final int currentLife) {
		this.currentLife = currentLife;
	}

	public float getInvincibleLife() {
		return invincibleLife;
	}

	public float getMaxInvincibleLife() {
		return MAXINVICIBLELIFE;
	}

	public void setMaxInvincibleLife(final float max) {
		MAXINVICIBLELIFE = max;
	}

	public void invincible() {
		final InvincibleBar invincibleBar = new InvincibleBar(60, this);
		data.getUniverse().addGameEntity(invincibleBar);
		this.invincibleLife = MAXINVICIBLELIFE;
	}

	public void decrementInvincibleLife() {
		this.invincibleLife--;
	}

	public boolean stillInvincible() {
		return (invincibleLife > 0);
	}

	public int getInitialPositionX() {
		return initialPositionX;
	}

	public void completeCurrentLife() {
		this.currentLife= maxLife;
	}

	public void swordStronger() {
		for (int i=MAXSTRONGERSWORD; i>0; i--) {
			final BonusSword bonus = new BonusSword(data, i-1, this);
			setStrongerSword(bonus);
			data.getUniverse().addGameEntity(bonus);
		}
	}

	public void setStrongerSword(final BonusSword bonus) {
		this.bonusSwords.add(bonus);
	}

	public void removeStrongerSword() {
		this.bonusSwords.get(0).canDraw(false);
		this.bonusSwords.remove(0);
	}

	public int getStrongerSword() {
		return bonusSwords.size();
	}

	public void removeAllStrongerSword() {
		for (int i = bonusSwords.size(); i>0; i--) {
			this.bonusSwords.get(i-1).canDraw(false);
			this.bonusSwords.remove(i-1);
		}
	}

	public void pushInDirection(boolean toTheLeft) {
		final int moveSpeed = (int) (getSpeedVector().getSpeed() * 1.5);
		if (toTheLeft) {
			getPosition().x -= moveSpeed;
		} else {
			getPosition().x += moveSpeed;
		}
		outOfBoundsVerification();
	}

	public void hitByBalloon() {
		if (sword != null) {
			sword.drop();
		}
	}

}
