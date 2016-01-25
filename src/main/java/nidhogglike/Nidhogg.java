package nidhogglike;

import gameframework.game.GameData;
import gameframework.game.GameLevelDefaultImpl;
import gameframework.gui.GameWindow;

import java.awt.event.KeyEvent;

import nidhogglike.entities.Ground;
import nidhogglike.entities.Player;
import nidhogglike.entities.Sword;
import nidhogglike.entities.Platform;
import nidhogglike.game.NidhoggConfiguration;
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
		final Input input = new Input(data.getCanvas());
		this.gameBoard = new NidhoggUniverseViewPort();
		this.gameBoard.setGameData(data);
		
		Player j1 = new Player(KeyEvent.VK_Z, KeyEvent.VK_Q, KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_A, input, data);
		Player j2 = new Player(KeyEvent.VK_UP, KeyEvent.VK_LEFT, KeyEvent.VK_DOWN, KeyEvent.VK_RIGHT, KeyEvent.VK_SHIFT, input, data);
		Sword j1sword = new Sword(data);
		Sword j2sword = new Sword(data);
		
		j1.setSword(j1sword);
		j2.setSword(j2sword);
		
		universe.addGameEntity(j1);
		universe.addGameEntity(j2);
		universe.addGameEntity(j1sword);
		universe.addGameEntity(j2sword);
		universe.addGameEntity(new Ground());
		universe.addGameEntity(new Platform(200, 250, 100, 10));
		universe.addGameEntity(new Platform(400, 250, 100, 10));
		universe.addGameEntity(new Platform(500, 320, 100, 50));
		
		universe.addGameEntity(new Platform(350, 150, 100, 10));


	}
	
	/**
	 * First method called
	 * 
	 * @param args Command line parameters
	 */
	public static void main(String[] args) {
		NidhoggConfiguration configuration = new NidhoggConfiguration(HEIGHT / SPRITE_SIZE, WIDTH / SPRITE_SIZE, SPRITE_SIZE, 42);
		final GameData gameData = new GameData(configuration);
		GameWindow gameWindow = new GameWindow("Nidhogg", gameData.getCanvas(), gameData);
		
		gameWindow.createGUI();
		new Nidhogg(gameData).start();
	}
}
