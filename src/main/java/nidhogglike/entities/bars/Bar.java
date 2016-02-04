package nidhogglike.entities.bars;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import gameframework.game.GameEntity;
import nidhogglike.entities.Player;
import nidhogglike.motion.NidhoggMovable;

/**
 * Draw a bar
 * 
 * @author Team 2
 *
 */
public abstract class Bar extends NidhoggMovable implements GameEntity {

	/**
	 * the player who have the bar
	 */
	protected Player player;
	/**
	 * the width if the bar
	 */
	protected static int WIDTH = 50;
	
	/**
	 * the background color
	 */
	protected static Color BACKGROUND_COLOR = new Color(60, 60, 60);

	/**
	 * Constructor for a bar
	 * 
	 * @param maxLife the maximum life for the player
	 * @param player for the bar
	 */
	public Bar(int maxLife, Player player) {
		this.player = player;
	}
	
	/**
	 * @see gameframework.base.ObjectWithBoundedBox#getBoundingBox()
	 */
	@Override
	public Rectangle getBoundingBox() {
		return new Rectangle(0, 0, WIDTH, 5);
	}

	/**
	 * @see gameframework.game.GameEntity#draw(java.awt.Graphics)
	 */
	@Override
	public void draw(Graphics g) {
		this.getPosition().x = positionX();
		this.getPosition().y =  positionY();
		int lifeWidth = lifeWidth();
		
		g.setColor(BACKGROUND_COLOR);
		g.fillRect(this.getPosition().x, this.getPosition().y, WIDTH, 9);
		
		g.setColor(getColor1());
		g.fillRect(this.getPosition().x + 2, this.getPosition().y + 2, lifeWidth, 2);
		
		g.setColor(getColor2());
		g.fillRect(this.getPosition().x + 2, this.getPosition().y + 4, lifeWidth, 3);
	}

	/**
	 * @return the first color for the bar
	 */
	protected abstract Color getColor1();

	/**
	 * @return the second color for the bar
	 */
	protected abstract Color getColor2();

	/**
	 * @return the x position to draw the bar
	 */
	protected abstract int positionX();

	/**
	 * @return the y position to draw the bar
	 */
	protected abstract int positionY();

	/**
	 * @return the width of the life
	 */
	protected abstract int lifeWidth();

	/**
	 * do nothing
	 * @see gameframework.motion.GameMovable#oneStepMoveAddedBehavior()
	 */
	@Override
	public void oneStepMoveAddedBehavior() {
	
	}
	
	/**
	 * @return false
	 * 
	 * @see gameframework.motion.GameMovable#isMovable()
	 */
	@Override
	public boolean isMovable() {
		return false;
	}

}
