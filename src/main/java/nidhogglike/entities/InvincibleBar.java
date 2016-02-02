package nidhogglike.entities;

import java.awt.Color;
import java.awt.Graphics;

import nidhogglike.Nidhogg;

public class InvincibleBar extends Bar {
	
	public InvincibleBar(int maxLife, Player player) {
		super(maxLife, player);
	}
	
	public void draw(Graphics g) {
		if (player.stillInvincible()) {
			super.draw(g);
		}
	}

	@Override
	protected int positionX() {
		if (Nidhogg.WIDTH/2 < this.player.getInitialPositionX()) {
			return (this.player.getInitialPositionX() + 45);
		} else {
			return (this.player.getInitialPositionX() - 45);
		}
	}

	@Override
	protected int positionY() {
		return 40;
	}

	@Override
	protected int lifeWidth() {
		return (int)(((player.getInvincibleLife() + 0f) / player.getMaxInvincibleLife())* 50) - 4;
	}

	@Override
	protected Color getColor1() {
		return new Color(240, 236, 51);
	}

	@Override
	protected Color getColor2() {
		return new Color(255, 217, 0);
	}

}
