package nidhogglike.entities;

import java.awt.Color;
import java.awt.Graphics;

import nidhogglike.Nidhogg;

/**
 * Invincible Bar is a bonus 
 * 
 * @author Team 2
 *
 */
public class InvincibleBar extends Bar {
	
	/**
	 * Constructor for the invincible bar
	 * 
	 * @param maxLife the maximum of life for the player
	 * @param player who have the bar
	 */
	public InvincibleBar(int maxLife, Player player) {
		super(maxLife, player);
	}
	
	/**
	 * @see nidhogglike.entities.Bar#draw(java.awt.Graphics)
	 */
	@Override
	public void draw(Graphics g) {
		if (player.stillInvincible()) {
			super.draw(g);
		}
	}

	/**
	 * @see nidhogglike.entities.Bar#positionX()
	 */
	@Override
	protected int positionX() {
		if (Nidhogg.WIDTH/2 < this.player.getInitialPositionX()) {
			return (this.player.getInitialPositionX() + 45);
		} else {
			return (this.player.getInitialPositionX() - 45);
		}
	}

	/**
	 * @see nidhogglike.entities.Bar#positionY()
	 */
	@Override
	protected int positionY() {
		return 40;
	}

	/**
	 * @see nidhogglike.entities.Bar#lifeWidth()
	 */
	@Override
	protected int lifeWidth() {
		return (int)(((player.getInvincibleLife() + 0f) / player.getMaxInvincibleLife())* 50) - 4;
	}

	/**
	 * @see nidhogglike.entities.Bar#getColor1()
	 */
	@Override
	protected Color getColor1() {
		return new Color(240, 236, 51);
	}

	/**
	 * @see nidhogglike.entities.Bar#getColor2()
	 */
	@Override
	protected Color getColor2() {
		return new Color(255, 217, 0);
	}

}
