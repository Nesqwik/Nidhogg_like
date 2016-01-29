package nidhogglike.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.net.URL;

import nidhogglike.Nidhogg;
import nidhogglike.motion.NidhoggMovable;
import nidhogglike.surprise.Gift;

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
	protected SpriteManager sprite;
	protected Boolean canDraw;
	private int gravityDelay;
	private float velocity_y;
	private boolean isMoving;
	private static float GRAVITY = 1f;
	private static float VELOCITY_Y_MAX = 5;
	private static int TIME = 4;
	private int timeToLive;

	public SurpriseGift(GameData data) {
		URL playerImage = this.getClass().getResource("/images/sword.png");
		DrawableImage drawableImage = new DrawableImage(playerImage, data.getCanvas());
		sprite = new SpriteManagerDefaultImpl(drawableImage, 50, 1);
		sprite.setTypes("good", "bad");
		sprite.setType("good");
		this.velocity_y = 0;
		this.canDraw=false;
		this.getPosition().y = 0;
		this.getPosition().x = Nidhogg.WIDTH / 2;
		this.pointsToAdd = 10;
		this.timeToLive = TIME;
	}


	@Override
	public void draw(Graphics g) {
		if (canDraw && (timeToLive > 0)) {
			if (!isOnGround) {
				applyGravity();
			}
			sprite.draw(g, position);
		}

	}

	public void setCanDraw(Boolean maybe) {
		canDraw = maybe;
	}

	public void setGift(int x) {
		this.getPosition().x = x;
		this.position.y = 0;
		this.canDraw = true;
		this.timeToLive = TIME;
	}

	@Override
	public boolean isMovable() {
		return true;
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
		this.getPosition().y = platform.getBoundingBox().y - 5;
	}

	public Gift getGift() {
		return gift;
	}

	public void takingGift(Player player) {
		if (canDraw)
			openGift(player);
		this.canDraw = false;
		this.gift = null;
	}
	
	protected int pointsToAdd;
	
	public void openGift(Player p) {
		p.increaseScore(pointsToAdd);
	}


	public void reduceTime() {
		this.timeToLive--;
	}

}
