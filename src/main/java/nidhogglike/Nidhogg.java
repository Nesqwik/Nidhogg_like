package nidhogglike;

import gameframework.game.GameData;
import gameframework.game.GameLevelDefaultImpl;
import gameframework.gui.GameStatusBarElement;
import gameframework.gui.GameWindow;

import java.awt.Point;
import java.awt.event.KeyEvent;

import nidhogglike.entities.Ground;
import nidhogglike.entities.Player;
import nidhogglike.entities.Sword;
import nidhogglike.entities.Platform;
import nidhogglike.game.NidhoggConfiguration;
import nidhogglike.game.NidhoggGameData;
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
	public static final String PLAYER2_DATA_KEY = "player2";
	public static final String PLAYER1_DATA_KEY = "player1";
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
		
		Player j1 = new Player((NidhoggGameData) data, input, true);
		Player j2 = new Player((NidhoggGameData) data, input, false);
		Sword j1sword = new Sword(data, false);
		Sword j2sword = new Sword(data, true);
		
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
		final NidhoggGameData gameData = new NidhoggGameData(configuration);
		// GameWindow gameWindow = new GameWindow("Nidhogg", gameData.getCanvas(), gameData);
		gameData.setObservableValue(PLAYER1_DATA_KEY, 0);
		gameData.setObservableValue(PLAYER2_DATA_KEY, 0);
		
		GameStatusBarElement<Integer> player1 = new GameStatusBarElement<>("Player 1 : ", gameData.getObservableValue(PLAYER1_DATA_KEY));
		GameStatusBarElement<Integer> player2 = new GameStatusBarElement<>("Player 2 : ", gameData.getObservableValue(PLAYER2_DATA_KEY));
		
		GameWindow gameWindow = new GameWindow("Nidhogg", gameData.getCanvas(), gameData.getConfiguration(), player1, player2);
		gameWindow.createGUI();
		new Nidhogg(gameData).start();
	}
}
