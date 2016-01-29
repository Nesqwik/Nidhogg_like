package nidhogg.entities;

import java.awt.Graphics;

import gameframework.game.GameEntity;

public class LivingEntity implements GameEntity {

	@Override
	public void draw(Graphics g) {
		g.drawRect(50, 50, 50, 50);
	}

	@Override
	public boolean isMovable() {
		return true;
	}

}
