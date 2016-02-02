package nidhogglike.entities;

import java.awt.Color;


public class LifeBar extends Bar {
	
	public LifeBar(int maxLife, Player player) {
		super(maxLife, player);
	}

	@Override
	protected int positionX() {
		return (player.getBoundingBox().width - WIDTH) / 2 + player.getPosition().x;
	}

	@Override
	protected int positionY() {
		return player.getPosition().y - 15;
	}

	@Override
	protected int lifeWidth() {
		return (int) (((player.getCurrentLife() + 0f) / player.getMaxLife())* WIDTH) - 4;
	}

	@Override
	protected Color getColor1() {
		return new Color(215, 74, 74);
	}

	@Override
	protected Color getColor2() {
		return new Color(208, 47, 47);
	}
}
