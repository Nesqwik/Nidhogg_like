package nidhogglike.entities;

import gameframework.game.GameEntity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import nidhogglike.motion.NidhoggMovable;

public class LifeBar extends NidhoggMovable implements GameEntity {
	protected Player player;
	
	private static Color BACKGROUND_COLOR = new Color(60, 60, 60);
	private static Color LIFE_COLOR1 = new Color(215, 74, 74);
	private static Color LIFE_COLOR2 = new Color(208, 47, 47);
	
	private static int WIDTH = 50;
	
	public LifeBar(int maxLife, Player player) {
		this.player = player;
	}
	
	@Override
	public void draw(Graphics g) {
		this.getPosition().x = (player.getBoundingBox().width - WIDTH) / 2 + player.getPosition().x;
		this.getPosition().y =  player.getPosition().y - 15;
		int lifeWidth = (int) (((player.getCurrentLife() + 0f) / player.getMaxLife())* WIDTH) - 4;
		
		g.setColor(BACKGROUND_COLOR);
		g.fillRect(this.getPosition().x, this.getPosition().y, WIDTH, 9);
		
		g.setColor(LIFE_COLOR1);
		g.fillRect(this.getPosition().x + 2, this.getPosition().y + 2, lifeWidth, 2);
		
		g.setColor(LIFE_COLOR2);
		g.fillRect(this.getPosition().x + 2, this.getPosition().y + 4, lifeWidth, 3);
	}

	@Override
	public boolean isMovable() {
		return false;
	}

	@Override
	public Rectangle getBoundingBox() {
		return new Rectangle(0, 0, WIDTH, 5);
	}

	@Override
	public void oneStepMoveAddedBehavior() {
		
	}
}
