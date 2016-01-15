package nidhogg;

import gameframework.drawing.GameCanvas;
import gameframework.drawing.GameCanvasDefaultImpl;
import gameframework.drawing.GameUniverseViewPort;
import gameframework.drawing.GameUniverseViewPortDefaultImpl;
import gameframework.game.GameConfiguration;
import gameframework.game.GameData;
import gameframework.game.GameLevel;
import gameframework.game.GameLevelDefaultImpl;
import gameframework.gui.GameWindow;

public class Nidhogg {
	public static void main(String[] args) {
		GameConfiguration configuration = new GameConfiguration();
		final GameData gameData = new GameData(configuration);
		GameWindow gameWindow = new GameWindow("Nidhogg", gameData.getCanvas(), gameData);
		gameWindow.createGUI();
		
		GameLevel level = new GameLevelDefaultImpl(gameData, 100) {
			
			@Override
			protected void init() {
				this.gameBoard = new GameUniverseViewPortDefaultImpl();
				this.gameBoard.setGameData(gameData);
				System.out.println("dfgd");
				gameData.getCanvas().setSize(640, 480);
			}
		};
		
		
		
		
		
		level.start();
		
	}
}
