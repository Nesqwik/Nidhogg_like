package nidhogglike.surprise;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.net.URL;

import nidhogglike.Nidhogg;
import nidhogglike.entities.Player;
import nidhogglike.motion.NidhoggMovable;

import gameframework.drawing.DrawableImage;
import gameframework.drawing.SpriteManager;
import gameframework.drawing.SpriteManagerDefaultImpl;
import gameframework.game.GameData;
import gameframework.game.GameEntity;
import gameframework.motion.blocking.MoveBlocker;
import gameframework.motion.overlapping.Overlappable;


public class SurpriseGift extends NidhoggMovable implements GameEntity, Overlappable {
	protected Gift gift;
	private Player holder;
	protected boolean isMovable;
	protected SpriteManager sprite;
	protected Boolean isGoodGift;
	protected Boolean canDraw;
	private int gravityDelay;
	private float velocity_y;
	private boolean isMoving;
	private static float GRAVITY = 1f;
	private static float VELOCITY_Y_MAX = 5;

	public SurpriseGift(GameData data) {
		this.isMovable = false;
		URL playerImage = this.getClass().getResource("/images/sword.png");
		DrawableImage drawableImage = new DrawableImage(playerImage, data.getCanvas());
		sprite = new SpriteManagerDefaultImpl(drawableImage, 50, 1);
		sprite.setTypes("good", "bad");
		sprite.setType("good");
		this.isGoodGift = true;
		this.velocity_y = 0;
		this.canDraw=false;
		this.getPosition().y = 0;
		this.getPosition().x = Nidhogg.WIDTH / 2;
	}

	protected void update(Gift gift) {
		gift.setTypeSprite(sprite);
	}

	@Override
	public void draw(Graphics g) {
		update(gift);
		if (!gift.isOpened() && canDraw) {
			if (!isOnGround) {
				applyGravity();
			}
			sprite.draw(g, position);
		}

	}

	public void setCanDraw(Boolean maybe) {
		canDraw = maybe;
	}

	public void setGift(Gift surprise) {
		this.gift = surprise;
		this.position.y = 0;
	}

	@Override
	public boolean isMovable() {
		return isMovable;
	}

	@Override
	public Rectangle getBoundingBox() {
		return new Rectangle(50, 10);
	}

	@Override
	public void oneStepMoveAddedBehavior() {
		applyGravity();

	}

	public void appear() {
		this.holder = null;
		setMoving(true);
	}

	public void setMoving(boolean b) {
		this.isMoving = b;
		if (!isMoving) {
			gravityDelay = 0;
		}
	}

	public boolean isMoving() {
		return this.isMoving;
	}

	public void applyGravity() {
		// Apply gravity
		velocity_y += GRAVITY;
		velocity_y = Math.min(velocity_y, VELOCITY_Y_MAX);
		this.getPosition().y += velocity_y;
	}

	public void setHolder(Player player) {
		this.holder = player;
	}

	public Player getHolder() {
		return holder;
	}

	protected boolean isOnGround = false;
	public void groundCollision(MoveBlocker platform) {
		velocity_y = 0;
		isOnGround = true;
		setMoving(false);
	}

	public Gift getGift() {
		return gift;
	}

	public void takingGift(Player player) {
		this.gift.openGift(player);
		this.canDraw = false;
	}
}
