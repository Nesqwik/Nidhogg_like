package nidhogglike.entities;

import gameframework.drawing.DrawableImage;
import gameframework.drawing.SpriteManager;
import gameframework.drawing.SpriteManagerDefaultImpl;
import gameframework.game.GameData;
import gameframework.game.GameEntity;
import gameframework.motion.GameMovable;
import gameframework.motion.GameMovableDriverDefaultImpl;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.net.URL;

import nidhogglike.Nidhogg;

public class Sword extends GameMovable implements GameEntity{
	private Player holder;
	private SpriteManager sprite;
	private static float GRAVITY = 0.2f;
	private static float VELOCITY_Y_MAX = 5;
	private static final int GROUND_Y = 340;
	private static float velocity_x = 10;
	private float velocity_y;
	private boolean isHeadingLeft;
	
	public Sword(GameData data){
		super(new GameMovableDriverDefaultImpl());
		URL playerImage = this.getClass().getResource("/images/sword.png");
		DrawableImage drawableImage = new DrawableImage(playerImage, data.getCanvas());
		sprite = new SpriteManagerDefaultImpl(drawableImage, 50, 1);
		sprite.setTypes("left", "right");
		sprite.setType("left");
		isHeadingLeft = true;
		velocity_y = 0;
	}
	
	@Override
	public Rectangle getBoundingBox() {
		return new Rectangle(50, 10);
	}

	@Override
	public void draw(Graphics g) {
		if (isHeld()) {
			position.x = holder.getPosition().x;
			if (isHeadingLeft) {
				position.x -= holder.getBoundingBox().width - 6;
			} else {
				position.x += holder.getBoundingBox().width - 19;
			}
			position.y = holder.getPosition().y + 11;
		}
		sprite.draw(g, position);
		
	}

	private boolean isHeld() {
		return holder != null;
	}

	@Override
	public void oneStepMoveAddedBehavior() {
		if (!isHeld()) {
			this.getPosition().x += velocity_x;
			// Apply gravity
			velocity_y += GRAVITY;
			velocity_y = Math.min(velocity_y, VELOCITY_Y_MAX);
			this.getPosition().y += velocity_y;
			
			// Collision with the ground
			if (this.getPosition().y > GROUND_Y) {
				this.getPosition().y = GROUND_Y;
				velocity_y = 0;
				velocity_x = 0;
			}
			
			// When the player goes out of bounds
			if(this.getPosition().x > Nidhogg.WIDTH) {
				this.getPosition().x = -this.getBoundingBox().width;
				
			} else if(this.getPosition().x < -this.getBoundingBox().width) {
				this.getPosition().x = Nidhogg.WIDTH;
			}
		} else {
			isHeadingLeft = holder.isHeadingLeft();
			
			sprite.setType(isHeadingLeft ? "left" : "right");
		}
	}

	public void playerThrow() {
		this.holder = null;
	}

	public void setHolder(Player player) {
		this.holder = player;
	}
}
