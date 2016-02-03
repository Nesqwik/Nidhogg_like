package nidhogglike.entities;

import gameframework.drawing.DrawableImage;
import gameframework.drawing.SpriteManager;
import gameframework.drawing.SpriteManagerDefaultImpl;
import gameframework.game.GameData;
import gameframework.game.GameEntity;
import gameframework.motion.blocking.MoveBlocker;
import gameframework.motion.overlapping.Overlappable;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.net.URL;

import nidhogglike.motion.NidhoggMovable;

/**
 * @author Team 2
 *
 *         Class representing a surprise gift
 *         It can be change player's health bar
 */

public class SurpriseGift extends NidhoggMovable implements GameEntity, Overlappable {
	protected static float GRAVITY = 1f;
	protected static float VELOCITY_Y_MAX = 5;
	protected int DELAYGIFTOPEN = 80;
	protected static int TIME = 2;
	protected static int POINT_LIFE_MODIFICATION = 2;
	protected float MAXINVICIBLELIFE = 1000;

	private final SpriteManager sprite;
	private float velocity_y;
	private boolean isMoving;
	private int timeToLive;
	private boolean isOpen;
	private boolean setTypeResponse;
	private boolean isOnGround;
	private int timeToOpen;
	private boolean giftInvincible;
	private Player holder;
	private boolean beforeIsInvincible;


	/**
	 * Create an instance of SurpriseGift
	 *
	 * @param data the game's data
	 */
	public SurpriseGift(final GameData data) {
		final URL playerImage = this.getClass().getResource("/images/gift.png");
		final DrawableImage drawableImage = new DrawableImage(playerImage, data.getCanvas());
		sprite = new SpriteManagerDefaultImpl(drawableImage, 50, 2);
		sprite.setTypes("gift","answer","answer2");
		sprite.setType("gift");
		this.velocity_y = 0;
		this.getPosition().y = 0;
		this.isOnGround = false;
		this.timeToLive = TIME;
		isOpen= true;
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
	public void draw(final Graphics g) {
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
		if (giftInvincible) {
			this.holder.decrementInvincibleLife();
		}

		if (timeToOpen-1 == DELAYGIFTOPEN) {

		}
	}


	/**
	 * create a new gift with the coordinate x (in abscissa)
	 *
	 * @param x the coordinate for x (in abscissa)
	 */
	public void setGift(final int x, final Player player) {
		if ((this.holder == null) || (!this.holder.stillInvincible())) {
			this.holder = null;
			this.giftInvincible = false;
			this.setTypeResponse = false;
			this.timeToOpen = 0;
			this.getPosition().x = x;
			this.position.y = 0;
			this.timeToLive = TIME;
			isOpen = false;
			sprite.setType("gift");
			DELAYGIFTOPEN+=20;
			appear();
		}
	}

	/**
	 * give a random answer
	 * it's can be a good or bad gift
	 *
	 * @param player who take the gift
	 */
	protected void giveAnswer(final Player player) {
		setTypeResponse = true;

		final int alea = (int)(Math.random() * 3);
		switch (alea) {
		case 1:
			invincibleGift(player);
			break;
		case 2:
			pointGift(player);
			break;
		default :
			bonusSwordGift(player);
			break;
		}
	}


	/**
	 * create a gift with bonus answer
	 *
	 * @param player who take the gift
	 */
	protected void bonusSwordGift(final Player player) {
		sprite.setType("answer2");
		if (player.getStrongerSword() > 0)
			player.removeAllStrongerSword();
		player.swordStronger();
	}


	/**
	 * create gift to increment current bar
	 *
	 * @param player who take the gift
	 */
	protected void pointGift(final Player player) {
		sprite.setType("answer");
		beforeIsInvincible = false;
		this.sprite.setIncrement(0);
		player.completeCurrentLife();
	}


	/**
	 * create gift to make the player invincible
	 *
	 * @param player who take the gift
	 */
	protected void invincibleGift(final Player player) {
		sprite.setType("answer");
		if (!beforeIsInvincible)
			this.sprite.increment();
		beforeIsInvincible = true;
		giftInvincible = true;
		player.setMaxInvincibleLife(MAXINVICIBLELIFE);
		player.invincible();
		MAXINVICIBLELIFE += 300;
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
		return new Rectangle(getPosition().x, getPosition().y, 50, 50);
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
	public void setMoving(final boolean b) {
		this.isMoving = b;
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
	public void groundCollision(final MoveBlocker platform) {
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
	public void takingGift(final Player player) {
		if (!isOpened()) {
			this.holder = player;
			this.isOpen = true;
			giveAnswer(player);
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

	/**
	 * @return true if the gift is on ground else false
	 */
	public boolean isOnGround() {
		return isOnGround;
	}

}
