package nidhogglike.entities;

import gameframework.game.GameEntity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import nidhogglike.Nidhogg;
import nidhogglike.motion.NidhoggMovable;

public class InvincibleBar extends NidhoggMovable implements GameEntity {

	private Color BACKGROUND_COLOR = new Color(60, 60, 60);
	private Color INVINCIBLE_COLOR = new Color(240, 236, 51);
	private Color INVINCIBLE_COLOR2 = new Color(255, 217, 0);
	private Player player;
	
	public InvincibleBar(int maxLife, Player player) {
		this.player = player;
	}
	
	public void draw(Graphics g) {
		if (player.stillInvincible()) {
			if (Nidhogg.WIDTH/2 < this.player.getInitialPositionX()) {
				position.x = this.player.getInitialPositionX() + 45;
			} else {
				position.x = this.player.getInitialPositionX() - 45;
			}
			position.y =  40;
			int lifeWidth = (int) (((player.getInvincibleLife() + 0f) / player.getMaxInvincibleLife())* 50) - 4;
			
			g.setColor(BACKGROUND_COLOR);
			g.fillRect(this.getPosition().x, this.getPosition().y, 50, 9);
			
			g.setColor(INVINCIBLE_COLOR);
			g.fillRect(this.getPosition().x + 2, this.getPosition().y + 2, lifeWidth, 2);
			
			g.setColor(INVINCIBLE_COLOR2);
			g.fillRect(this.getPosition().x + 2, this.getPosition().y + 4, lifeWidth, 3);
		}
	}

	@Override
	public boolean isMovable() {
		return false;
	}

	@Override
	public Rectangle getBoundingBox() {
		return new Rectangle(0, 0, 50, 5);
	}

	@Override
	public void oneStepMoveAddedBehavior() {
		
	}
}
