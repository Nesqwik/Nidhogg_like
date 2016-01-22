package nidhogglike;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import gameframework.game.GameEntity;
import gameframework.motion.blocking.MoveBlocker;

public class Wall implements MoveBlocker, GameEntity {

	public Wall() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Rectangle getBoundingBox() {
		return new Rectangle(350, 350, 100, 100);
	}

	@Override
	public boolean isMovable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(new Color(150,150,150));
		g.fillRect(350, 350, 100, 100);
	}

}
