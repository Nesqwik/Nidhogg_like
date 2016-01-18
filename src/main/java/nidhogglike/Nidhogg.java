package nidhogglike;

import gameframework.drawing.GameUniverseViewPortDefaultImpl;
import gameframework.game.GameConfiguration;
import gameframework.game.GameData;
import gameframework.game.GameLevelDefaultImpl;
import gameframework.gui.GameWindow;
import gameframework.motion.MoveStrategyKeyboard;
import gameframework.motion.MoveStrategyKeyboard8Dir;
/**
 * @author Team 2
 * 
 * Main class of the game
 * 
 * Create the game window, canvas and entities
 * then start the main loop
 */
public class Nidhogg extends GameLevelDefaultImpl {
	public static int WIDTH = 640;
	public static int HEIGHT = 480;
	public static int SPRITE_SIZE = 16;
	public static int FPS = 60;
	
	public Nidhogg(GameData gameData) {
		super(gameData, (int) (1000f / FPS));
	}

	/** (non-Javadoc)
	 * @see gameframework.game.GameLevelDefaultImpl#init()
	 */
	@Override
	protected void init() {
		final MoveStrategyKeyboard strategyKeyBoard = new MoveStrategyKeyboard8Dir(false);
		data.getCanvas().addKeyListener(strategyKeyBoard);
		this.gameBoard = new GameUniverseViewPortDefaultImpl();
		this.gameBoard.setGameData(data);
		
		universe.addGameEntity(new Player(strategyKeyBoard));
	}
	
	
	public static void main(String[] args) {
		GameConfiguration configuration = new GameConfiguration(HEIGHT / SPRITE_SIZE, WIDTH / SPRITE_SIZE, SPRITE_SIZE, 42);
		final GameData gameData = new GameData(configuration);
		GameWindow gameWindow = new GameWindow("Nidhogg", gameData.getCanvas(), gameData);
		
		gameWindow.createGUI();
		new Nidhogg(gameData).start();
	}
}
