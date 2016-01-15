package nidhogg;

import gameframework.drawing.GameCanvas;
import gameframework.drawing.GameCanvasDefaultImpl;
import gameframework.game.GameConfiguration;
import gameframework.game.GameData;
import gameframework.gui.GameWindow;

public class Nidhogg {
	public static void main(String[] args) {
		GameCanvas gameCanvas = new GameCanvasDefaultImpl();
		GameConfiguration configuration = new GameConfiguration();
		GameData gameData = new GameData(configuration);
		
		GameWindow gameWindow = new GameWindow("Nidhogg", gameCanvas, gameData);
		gameWindow.createGUI();
	}
}
