package nidhogglike.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.net.URL;

import gameframework.assets.Sound;
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
	private boolean ducking;
	private boolean headingLeft;
	private int boundingBoxHeight;
	protected String spriteTypePrefix;
	private String observableDataKey;
	private Point respawnPosition;
	private ParticleEmitter particleEmitter;
	private ParticleBehavior dyingParticleBehavior;
	private Color color;

	private final boolean isPlayer1;

	public Player(final NidhoggGameData data, final Input input, final boolean isPlayer1) {
		super(new GameMovableDriverDefaultImpl());
		this.isPlayer1 = isPlayer1;
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

	protected void initPlayer(String observableDataKey, Point respawnPosition, int keyUp, int keyLeft, int keyDown,
			int keyRight, int throwKey, Input input, NidhoggGameData data, String spritePath) {
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
		return new Rectangle(isHeadingLeft() ? 50 : 40, boundingBoxHeight);
	}

	@Override
	public void oneStepMoveAddedBehavior() {
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
		if (this.getPosition().x > Nidhogg.WIDTH) {
			this.getPosition().x = -this.getBoundingBox().width;

		} else if (this.getPosition().x < -this.getBoundingBox().width) {
			this.getPosition().x = Nidhogg.WIDTH;
		}

		updateDirection();
		updateAnimation();
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

			sprite.setType(spriteTypePrefix + (headingLeft ? "Left" : "Right"));
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
		sprite.draw(g, position);
	}

	public boolean isHoldingSword() {
		return sword != null;
	}

	public boolean isDucking() {
		return ducking;
	}

	public void setSword(Sword sword) {
		sword.setHolder(this);
		this.sword = sword;
	}

	public boolean isHeadingLeft() {
		return headingLeft;
	}

	public void die() {
		// Particles
		particleEmitter.emit(color, this.getPosition().x + this.getBoundingBox().width / 2,
				this.getPosition().y + this.getBoundingBox().height / 2, 20, dyingParticleBehavior);

		// Head
		data.getUniverse().addGameEntity(new HeadBalloon(this.getPosition().x, this.getPosition().y, color));

		data.incrementObservableValue(observableDataKey, 1);
		announceKills(data.getObservableValue(observableDataKey).getValue());
		// Respawn
		resetPosition();

		recoverSwordIfNeeded();
	}

	public void announceKills(int deaths) {
		Sound s = null;
		try {
			switch (deaths) {
			case 1:
				s = new Sound("/sounds/announcer/firstblood.wav");
				break;
			case 3:
				s = new Sound("/sounds/announcer/multikill.wav");
				break;
			case 5:
				s = new Sound("/sounds/announcer/megakill.wav");
				break;
			case 10:
				s = new Sound("/sounds/announcer/ludicrouskill.wav");
				break;
			case 15:
				s = new Sound("/sounds/announcer/monsterkill.wav");
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (s != null)
			s.play();
	}

	protected void recoverSwordIfNeeded() {
		if (sword == null) {
			final Sword sword = ((NidhoggUniverse) data.getUniverse()).getFreeSword();
			if (sword != null) {
				setSword(sword);
			}
		}
	}

	public void refinePositionAfterLateralCollision(final Platform platform) {
		final boolean leftCollision = this.getPosition().x > platform.getBoundingBox().x;
		if (leftCollision) {
			getPosition().x += 1;
		} else {
			getPosition().x -= 1;
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

	public void setParticleEmitter(final ParticleEmitter emitter) {
		this.particleEmitter = emitter;
		dyingParticleBehavior = new MovingParticle(null, 7, -Math.PI * 6 / 10, -Math.PI * 4 / 10);
		dyingParticleBehavior = new DyingParticle(dyingParticleBehavior, 300, false);
		dyingParticleBehavior = new GravityParticle(dyingParticleBehavior, 100, 250);
		dyingParticleBehavior = new DelayedParticle(dyingParticleBehavior, 2);
	}
}
