package nidhogglike.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import gameframework.game.GameEntity;
import gameframework.motion.blocking.MoveBlocker;

/**
 * @author Team 2
 * Class to test collisions before having something more useful
 */
public class Platform implements MoveBlocker, GameEntity {

	protected Rectangle boundingBox;
	
	public Platform(int x, int y, int width, int height) {
		boundingBox = new Rectangle(x, y, width, height);
	}

	@Override
	public Rectangle getBoundingBox() {
		return boundingBox;
	}

	@Override
	public boolean isMovable() {
		return false;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(new Color(150,150,150));
		g.fillRect(boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
	}

}
