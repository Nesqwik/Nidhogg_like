package nidhogg;

import gameframework.game.GameEntity;
import gameframework.motion.GameMovable;
import gameframework.motion.GameMovableDriver;
import gameframework.motion.GameMovableDriverDefaultImpl;
import gameframework.motion.MoveStrategyKeyboard;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Player extends GameMovable implements GameEntity{
	static GameMovableDriver gameDriver = new GameMovableDriverDefaultImpl();
	
	public Player(MoveStrategyKeyboard strategyKeyBoard){
		super(gameDriver);
		gameDriver.setStrategy(strategyKeyBoard);
	}
	
	@Override
	public Rectangle getBoundingBox() {
		return new Rectangle(50, 50);
	}

	@Override
	public void oneStepMoveAddedBehavior() {
		// TODO
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(new Color(50, 200, 40));
		g.fillRect(this.getPosition().x, this.getPosition().y, 10, 10);
		
	}

}
