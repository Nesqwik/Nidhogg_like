package nidhogglike.entities;

import gameframework.game.GameEntity;
import gameframework.motion.blocking.MoveBlocker;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Wall implements GameEntity, MoveBlocker {

	@Override
	public Rectangle getBoundingBox() {
		return new Rectangle(200,200,100,100);
	}

	@Override
	public void draw(final Graphics g) {
		g.setColor(new Color(150, 150, 150));
		final Rectangle hitbox = getBoundingBox();
		g.fillRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
	}

	@Override
	public boolean isMovable() {
		// TODO Auto-generated method stub
		return false;
	}

}
