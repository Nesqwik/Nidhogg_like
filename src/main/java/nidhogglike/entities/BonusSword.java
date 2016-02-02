package nidhogglike.entities;

import java.awt.Graphics;
import java.awt.Rectangle;

import gameframework.drawing.DrawableImage;
import gameframework.drawing.SpriteManagerDefaultImpl;
import gameframework.game.GameData;
import gameframework.game.GameEntity;
import gameframework.motion.GameMovableDriverDefaultImpl;
import nidhogglike.Nidhogg;
import nidhogglike.motion.NidhoggMovable;

public class BonusSword extends NidhoggMovable implements GameEntity {

	private SpriteManagerDefaultImpl sprite;
	private int shift;
	private Player player;
	private boolean canDraw;

	public BonusSword(GameData data, int shift, Player player) {
		super(new GameMovableDriverDefaultImpl());
		this.shift = shift;
		this.player = player;
		this.canDraw = true;
		final DrawableImage drawableImage = new DrawableImage("/images/bonusSword.png", data.getCanvas());
		sprite = new SpriteManagerDefaultImpl(drawableImage, 20, 1);
		sprite.setTypes("player1", "player2");
		sprite.setType("player1");
	}
	
	@Override
	public Rectangle getBoundingBox() {
		return new Rectangle(40,40);
	}

	@Override
	public void draw(Graphics g) {
		if (this.canDraw) {
			if (Nidhogg.WIDTH/2 < this.player.getInitialPositionX()) {
				position.x = this.player.getInitialPositionX() + shift * 20 + 45;
				sprite.setType("player2");
			} else {
				position.x = this.player.getInitialPositionX() + shift * 20 - 45;
			}
			position.y = 5;
			this.sprite.draw(g, position);
		} 
	}

	@Override
	public void oneStepMoveAddedBehavior() {}

	public void canDraw(boolean b) {
		this.canDraw = b;
	}

}
