package nidhogglike.entities;

import gameframework.drawing.DrawableImage;
import gameframework.drawing.GameCanvas;
import gameframework.drawing.SpriteManager;
import gameframework.drawing.SpriteManagerDefaultImpl;
import gameframework.game.GameEntity;
import gameframework.motion.GameMovable;
import gameframework.motion.GameMovableDriver;
import gameframework.motion.GameMovableDriverDefaultImpl;
import gameframework.motion.MoveStrategyKeyboard;
import gameframework.motion.blocking.MoveBlocker;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.net.URL;

import nidhogglike.Nidhogg;
import nidhogglike.input.Input;


/**
 * @author Team 2
 *
 * Class representing a player controlled by the keyboard
 */
public class Player extends GameMovable implements GameEntity{
	private static final int GROUND_Y = 368;
	static GameMovableDriver gameDriver = new GameMovableDriverDefaultImpl();
	protected float velocity_y;
	private static float GRAVITY = 1f;
	private static float VELOCITY_Y_MAX = 10;
	private static int JUMP_HEIGHT = 10;
	private boolean jumping;
	private int jumpHeight;
	private Input input;
	private SpriteManager sprite;
	private int incrementStep;
	
	public Player(MoveStrategyKeyboard strategyKeyBoard, Input input, GameCanvas canvas){
		super(gameDriver);
		gameDriver.setStrategy(strategyKeyBoard);
		jumping = false;
		incrementStep = 0;
		this.input = input;
		URL playerImage = this.getClass().getResource("/images/player.png");
		DrawableImage drawableImage = new DrawableImage(playerImage, canvas);
		sprite = new SpriteManagerDefaultImpl(drawableImage, 50, 2);
	}
	
	@Override
	public Rectangle getBoundingBox() {
		return new Rectangle(50, 50);
	}

	@Override
	public void oneStepMoveAddedBehavior() {
		if (input.isPressed(KeyEvent.VK_SPACE) && jumpHeight < JUMP_HEIGHT) {
			velocity_y = -10;
			jumping = true;
			++jumpHeight;
		}
		
		// Apply gravity
		velocity_y += GRAVITY;
		velocity_y = Math.min(velocity_y, VELOCITY_Y_MAX);
		this.getPosition().y += velocity_y;

		// Collision with the ground
		if (this.getPosition().y > GROUND_Y) {
			this.getPosition().y = GROUND_Y;
			jumping = false;
			jumpHeight = 0;
			velocity_y = 0;
		}
		
		// When the player goes out of bounds
		// TODO : Nicolas, regarde ça pour éviter les if ! <3<3<3
		if(this.getPosition().x > Nidhogg.WIDTH) {
			this.getPosition().x = -this.getBoundingBox().width;
			
		} else if(this.getPosition().x < -this.getBoundingBox().width) {
			this.getPosition().x = Nidhogg.WIDTH;
		}
		
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
	public void draw(Graphics g) {
		sprite.draw(g, position);
	}

	@Override
	public void onMoveFailure(MoveBlocker lastBlockingBlocker) {
		velocity_y = 0;
		this.getPosition().y += 20;
	}
}
