package nidhogglike.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.net.URL;

import nidhogglike.motion.NidhoggMovable;

import gameframework.drawing.DrawableImage;
import gameframework.drawing.SpriteManager;
import gameframework.drawing.SpriteManagerDefaultImpl;
import gameframework.game.GameData;
import gameframework.game.GameEntity;
import gameframework.motion.blocking.MoveBlocker;
import gameframework.motion.overlapping.Overlappable;

/**
 * @author Team 2
 *
 *         Class representing a surprise gift 
 *         It can be change player's health bar
 */

public class SurpriseGift extends NidhoggMovable implements GameEntity, Overlappable {
	protected static float GRAVITY = 1f;
	protected static float VELOCITY_Y_MAX = 5;
	protected static int DELAYGIFTOPEN = 80;
	protected static int TIME = 4;
	protected static int POINT_LIFE_MODIFICATION = 2;
	
	private SpriteManager sprite;
	@SuppressWarnings("unused")
	private int gravityDelay;
	private Player holder;
	private float velocity_y;
	private boolean isMoving;
	private int timeToLive;
	private boolean isOpen;
	private boolean setTypeResponse;
	private boolean isOnGround;
	private int timeToOpen;
	

	/**
	 * Create an instance of SurpriseGift
	 * 
	 * @param data the game's data
	 */
	public SurpriseGift(GameData data) {
		URL playerImage = this.getClass().getResource("/images/gift.png");
		DrawableImage drawableImage = new DrawableImage(playerImage, data.getCanvas());
		sprite = new SpriteManagerDefaultImpl(drawableImage, 50, 2);
		sprite.setTypes("gift","answer");
		sprite.setType("gift");
		this.velocity_y = 0;
		this.getPosition().y = 0;
		this.isOnGround = false;
		this.timeToLive = TIME;
		isOpen= true;
		this.holder = null;
	}


	/**
	 * Draw the surprise gift
	 * It appears from the top
	 * When it's not open, it disappears after some time
	 * And when it's open, we can see the answer for a moment
	 * 
	 * @see gameframework.game.GameEntity#draw(java.awt.Graphics)
	 */
	@Override
	public void draw(Graphics g) {
		if (!isOpened() && (timeToLive > 0)) {
			if (!isOnGround) {
				applyGravity();
			}
			sprite.draw(g, position);
		}
		
		if (setTypeResponse && isOpened() && (timeToOpen < DELAYGIFTOPEN-1)) {
			sprite.draw(g, position);
			timeToOpen++;
		}
	}

	/**
	 * create a new gift with the coordinate x (in abscissa)
	 * 
	 * @param x the coordinate for x (in abscissa)
	 */
	public void setGift(int x) {
		this.setTypeResponse = false;
		this.timeToOpen = 0;
		this.getPosition().x = x;
		this.position.y = 0;
		this.timeToLive = TIME;
		isOpen = false;
		sprite.setType("gift");
		this.holder = null;
	}

	/**
	 * give a random answer
	 * it's can be a good or bad gift
	 * 
	 * @param player who take the gift
	 */
	private void giveAnswer() {
		setTypeResponse = true;
		sprite.setType("answer");
		if (Math.random() < 0.5) {
			this.sprite.increment();
			this.holder.modificationScore((-1) * POINT_LIFE_MODIFICATION);
		} else {
			this.holder.modificationScore(POINT_LIFE_MODIFICATION);
		}
	}

	/**
	 * @see gameframework.motion.GameMovable#isMovable()
	 */
	@Override
	public boolean isMovable() {
		return true;
	}

	/**
	 * @see gameframework.base.ObjectWithBoundedBox#getBoundingBox()
	 */
	@Override
	public Rectangle getBoundingBox() {
		return new Rectangle(50,50);
	}

	/**
	 * @see gameframework.motion.GameMovable#oneStepMoveAddedBehavior()
	 */
	@Override
	public void oneStepMoveAddedBehavior() {
		if (!this.isOpened()) {
			applyGravity();
		}
	}

	/**
	 * Make possible to appear
	 */
	public void appear() {
		setMoving(true);
	}

	/**
	 * Change the moving
	 * 
	 * @param b true if you want that the SurprisedGift move else false
	 */
	public void setMoving(boolean b) {
		this.isMoving = b;
		if (!isMoving) {
			gravityDelay = 0;
		}
	}

	/**
	 * @return true if it's moving
	 */
	public boolean isMoving() {
		return this.isMoving;
	}

	/**
	 * apply the gravity at the gift
	 */
	public void applyGravity() {
		velocity_y += GRAVITY;
		velocity_y = Math.min(velocity_y, VELOCITY_Y_MAX);
		this.getPosition().y += velocity_y;
	}

	/**
	 * Process the ground collision
	 * 
	 * @param platform of the game
	 */
	public void groundCollision(MoveBlocker platform) {
		this.getPosition().y = platform.getBoundingBox().y - 49;
		velocity_y = 0;
		isOnGround = true;
		setMoving(false);
	}

	/**
	 * Player take the gift and an answer is revealed
	 * 
	 * @param player who take the gift
	 */
	public void takingGift() {
		if (!isOpened()) {
			this.isOpen = true;
			giveAnswer();
		}
	}

	/**
	 * Reduction of the time for the duration of the gift not opened
	 */
	public void reduceTime() {
		this.timeToLive--;
	}

	/**
	 * @return true if the gift is open else false
	 */
	public boolean isOpened() {
		return isOpen;
	}
	
	public void setHolder(Player player) {
		this.holder = player;
		player.isTakingGift(this);
	}
	
	public Player getHolder() {
		return this.holder;
	}

}
