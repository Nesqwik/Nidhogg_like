package nidhogg;

import gameframework.drawing.GameUniverseViewPortDefaultImpl;
import gameframework.game.GameConfiguration;
import gameframework.game.GameData;
import gameframework.game.GameLevel;
import gameframework.game.GameLevelDefaultImpl;
import gameframework.gui.GameWindow;
import gameframework.motion.MoveStrategyKeyboard;
import gameframework.motion.MoveStrategyKeyboard8Dir;

public class Nidhogg {

	public static void main(String[] args) {
		GameConfiguration configuration = new GameConfiguration();
		final GameData gameData = new GameData(configuration);
		GameWindow gameWindow = new GameWindow("Nidhogg", gameData.getCanvas(), gameData);
		
		final MoveStrategyKeyboard strategyKeyBoard = new MoveStrategyKeyboard8Dir(false);
		gameData.getCanvas().addKeyListener(strategyKeyBoard);
		
		gameWindow.createGUI();
		
		GameLevel level = new GameLevelDefaultImpl(gameData, 50) {
			
			@Override
			protected void init() {
				this.gameBoard = new GameUniverseViewPortDefaultImpl();
				this.gameBoard.setGameData(gameData);
				universe.addGameEntity(new Player(strategyKeyBoard));
				System.out.println("dfgd");
				gameData.getCanvas().setSize(640, 480);
			}
		};
		
		
		level.start();
		
	}
}
