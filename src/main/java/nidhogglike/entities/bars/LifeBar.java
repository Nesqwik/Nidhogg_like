package nidhogglike.entities.bars;

import java.awt.Color;

import nidhogglike.entities.Player;


/**
 * The life bar of each player
 * 
 * @author Team 2
 *
 */
public class LifeBar extends Bar {
	
	/**
	 * Constructor life bar for one player
	 * 
	 * @param maxLife the maximum of life for the player
	 * @param player who have the bar
	 */
	public LifeBar(int maxLife, Player player) {
		super(maxLife, player);
	}

	/**
	 * @see nidhogglike.entities.bars.Bar#positionX()
	 */
	@Override
	protected int positionX() {
		return (player.getBoundingBox().width - WIDTH) / 2 + player.getPosition().x;
	}

	/**
	 * @see nidhogglike.entities.bars.Bar#positionY()
	 */
	@Override
	protected int positionY() {
		return player.getPosition().y - 15;
	}

	/**
	 * @see nidhogglike.entities.bars.Bar#lifeWidth()
	 */
	@Override
	protected int lifeWidth() {
		return (int) (((player.getCurrentLife() + 0f) / player.getMaxLife())* WIDTH) - 4;
	}

	/**
	 * @see nidhogglike.entities.bars.Bar#getColor1()
	 */
	@Override
	protected Color getColor1() {
		return new Color(215, 74, 74);
	}

	/**
	 * @see nidhogglike.entities.bars.Bar#getColor2()
	 */
	@Override
	protected Color getColor2() {
		return new Color(208, 47, 47);
	}
}
