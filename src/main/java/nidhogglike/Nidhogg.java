package nidhogglike;

import gameframework.game.GameConfiguration;
import gameframework.game.GameData;
import gameframework.game.GameLevelDefaultImpl;
import gameframework.gui.GameWindow;

import java.awt.event.KeyEvent;

import nidhogglike.entities.Player;
import nidhogglike.input.Input;
/**
 * @author Team 2
 * 
 * Main class of the game
 * 
 * Create the game window, canvas and entities
 * then start the main loop
 */
public class Nidhogg extends GameLevelDefaultImpl {
	public static int WIDTH = 800;
	public static int HEIGHT = 600;
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
		final Input input = new Input(data.getCanvas());
		this.gameBoard = new NidhoggUniverseViewPort();
		this.gameBoard.setGameData(data);
		
		universe.addGameEntity(new Player(KeyEvent.VK_Z, KeyEvent.VK_Q, KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_A, input, data));
		universe.addGameEntity(new Player(KeyEvent.VK_UP, KeyEvent.VK_LEFT, KeyEvent.VK_DOWN, KeyEvent.VK_RIGHT, KeyEvent.VK_SHIFT, input, data));
	}
	
	
	/**
	 * First method called
	 * 
	 * @param args Command line parameters
	 */
	public static void main(String[] args) {
		GameConfiguration configuration = new GameConfiguration(HEIGHT / SPRITE_SIZE, WIDTH / SPRITE_SIZE, SPRITE_SIZE, 42);
		final GameData gameData = new GameData(configuration);
		GameWindow gameWindow = new GameWindow("Nidhogg", gameData.getCanvas(), gameData);
		
		gameWindow.createGUI();
		new Nidhogg(gameData).start();
	}
}
