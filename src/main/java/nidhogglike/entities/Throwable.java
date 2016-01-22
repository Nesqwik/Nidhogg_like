package nidhogglike.entities;

import gameframework.drawing.DrawableImage;
import gameframework.drawing.SpriteManager;
import gameframework.drawing.SpriteManagerDefaultImpl;
import gameframework.game.GameData;
import gameframework.game.GameEntity;
import gameframework.motion.GameMovable;
import gameframework.motion.GameMovableDriver;
import gameframework.motion.GameMovableDriverDefaultImpl;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.net.URL;

import nidhogglike.Nidhogg;

public class Throwable extends GameMovable implements GameEntity{
	static GameMovableDriver gameDriver = new GameMovableDriverDefaultImpl();
	private Player source;
	private SpriteManager sprite;
	private static float GRAVITY = 0.2f;
	private static float VELOCITY_Y_MAX = 5;
	private static final int GROUND_Y = 415;
	private static float velocity_x = 10;
	// 1 : rigth, -1 : left
	private int direction;
	private GameData data;
	private float velocity_y;
	
	public Throwable (Player p, GameData data, int x_position, int y_position, int direction){
		super(gameDriver);
		this.data = data;
		this.direction = direction;
		this.getPosition().x = x_position;
		this.getPosition().y = y_position;
		URL playerImage = this.getClass().getResource("/images/sword.png");
		DrawableImage drawableImage = new DrawableImage(playerImage, data.getCanvas());
		sprite = new SpriteManagerDefaultImpl(drawableImage, 50, 1);
		source = p;
		velocity_y = 0;
	}
	
	@Override
	public Rectangle getBoundingBox() {
		return new Rectangle(50, 10);
	}

	@Override
	public void draw(Graphics g) {
		sprite.draw(g, position);
		
	}

	@Override
	public void oneStepMoveAddedBehavior() {
		this.getPosition().x += velocity_x*direction;
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
		// TODO : Nicolas, regarde ça pour éviter les if ! <3<3<3
		if(this.getPosition().x > Nidhogg.WIDTH) {
			this.getPosition().x = -this.getBoundingBox().width;
			
		} else if(this.getPosition().x < -this.getBoundingBox().width) {
			this.getPosition().x = Nidhogg.WIDTH;
		}
	}
}
