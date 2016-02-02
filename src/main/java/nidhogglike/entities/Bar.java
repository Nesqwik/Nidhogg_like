package nidhogglike.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import gameframework.game.GameEntity;
import nidhogglike.motion.NidhoggMovable;

public abstract class Bar extends NidhoggMovable implements GameEntity {

	protected Player player;
	protected static int WIDTH = 50;
	
	protected static Color BACKGROUND_COLOR = new Color(60, 60, 60);

	public Bar(int maxLife, Player player) {
		this.player = player;
	}
	
	@Override
	public Rectangle getBoundingBox() {
		return new Rectangle(0, 0, WIDTH, 5);
	}

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

	protected abstract Color getColor1();

	protected abstract Color getColor2();

	protected abstract int positionX();

	protected abstract int positionY();

	protected abstract int lifeWidth();

	@Override
	public void oneStepMoveAddedBehavior() {
	
	}
	
	@Override
	public boolean isMovable() {
		return false;
	}

}
