package nidhogglike.surprise;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.net.URL;
import java.util.LinkedList;

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
	private static int GRAVITY_DELAY = 20;
	
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
	}
	
	protected void update(Gift gift) {
		gift.setTypeSprite(sprite);
	}

	@Override
	public void draw(Graphics g) {
		update(gift);
		if (!gift.isOpened() && canDraw) {
			position.x = gift.getPositionX();
			position.y = 200;
			sprite.draw(g, position);
		}
		
	}
	
	public void setCanDraw(Boolean maybe) {
		canDraw = maybe;
	}

	public void setGift(Gift surprise) {
		this.gift = surprise;
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
		this.getPosition().x += 200;
		gravityDelay -= 1;
		
		if (gravityDelay <= 0) {
			applyGravity();
		}

	}
	
	public void appear() {
		gravityDelay = GRAVITY_DELAY;
		this.holder = null;
		setMoving(true);
	}

	private void setMoving(boolean b) {
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
	
	public void groundCollision(MoveBlocker platform) {
		// Collision with the ground
		this.getPosition().y = platform.getBoundingBox().y - this.getBoundingBox().height;
		velocity_y = 0;
	}
	
	public Gift getGift() {
		return gift;
	}
}
