package nidhogglike.entities;

import gameframework.game.GameEntity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import nidhogglike.motion.NidhoggMovable;

public class LifeBar extends NidhoggMovable implements GameEntity {
	protected Player player;
	
	private static int WIDTH = 50;
	
	public LifeBar(int maxLife, Player player) {
		this.player = player;
	}
	
	@Override
	public void draw(Graphics g) {
		this.getPosition().x = (player.getBoundingBox().width - WIDTH) / 2 + player.getPosition().x;
		this.getPosition().y =  player.getPosition().y - 15;
		
		g.setColor(Color.BLACK);
		g.fillRect(this.getPosition().x, this.getPosition().y, WIDTH, 9);
		
		g.setColor(Color.RED);
		g.fillRect(this.getPosition().x + 2, this.getPosition().y + 2, (int) (((player.getCurrentLife() + 0f) / player.getMaxLife())* WIDTH) - 4, 5);
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
