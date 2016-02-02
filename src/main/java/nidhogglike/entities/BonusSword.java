package nidhogglike.entities;

import java.awt.Graphics;
import java.awt.Rectangle;

import gameframework.drawing.DrawableImage;
import gameframework.drawing.SpriteManagerDefaultImpl;
import gameframework.game.GameData;
import gameframework.game.GameEntity;
import gameframework.motion.GameMovableDriverDefaultImpl;
import nidhogglike.Nidhogg;
import nidhogglike.motion.NidhoggMovable;

/**
 * With this bonus, you can kill a player with only one shot
 * 
 * @author Team 2
 *
 */
public class BonusSword extends NidhoggMovable implements GameEntity {

	/**
	 * to draw the image
	 */
	private SpriteManagerDefaultImpl sprite;
	/**
	 * the shift between two images
	 */
	private int shift;
	/**
	 * the player who take the bonus
	 */
	private Player player;
	/**
	 * it's true if it can be draw
	 */
	private boolean canDraw;

	/**
	 * Constructor BonusSword
	 * 
	 * @param data of the game
	 * @param shift between two swords
	 * @param player who take this one
	 */
	public BonusSword(GameData data, int shift, Player player) {
		super(new GameMovableDriverDefaultImpl());
		this.shift = shift;
		this.player = player;
		this.canDraw = true;
		final DrawableImage drawableImage = new DrawableImage("/images/bonusSword.png", data.getCanvas());
		sprite = new SpriteManagerDefaultImpl(drawableImage, 20, 1);
		sprite.setTypes("player1", "player2");
		sprite.setType("player1");
	}
	
	/**
	 * @see gameframework.base.ObjectWithBoundedBox#getBoundingBox()
	 */
	@Override
	public Rectangle getBoundingBox() {
		return new Rectangle(40,40);
	}

	/**
	 * @see gameframework.game.GameEntity#draw(java.awt.Graphics)
	 */
	@Override
	public void draw(Graphics g) {
		if (this.canDraw) {
			if (Nidhogg.WIDTH/2 < this.player.getInitialPositionX()) {
				position.x = this.player.getInitialPositionX() + shift * 20 + 45;
				sprite.setType("player2");
			} else {
				position.x = this.player.getInitialPositionX() + shift * 20 - 45;
			}
			position.y = 5;
			this.sprite.draw(g, position);
		} 
	}

	/**
	 * @see gameframework.motion.GameMovable#oneStepMoveAddedBehavior()
	 */
	@Override
	public void oneStepMoveAddedBehavior() {}

	/**
	 * 
	 * @param maybe is true if you want draw, else is false
	 */
	public void canDraw(boolean maybe) {
		this.canDraw = maybe;
	}

}
