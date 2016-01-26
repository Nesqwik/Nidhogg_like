package nidhogglike;

import java.util.Arrays;

import gameframework.game.GameData;
import gameframework.game.GameLevelDefaultImpl;
import gameframework.gui.GameStatusBarElement;
import gameframework.gui.GameWindow;
import nidhogglike.entities.Ground;
import nidhogglike.entities.Player;
import nidhogglike.entities.Sword;
import nidhogglike.entities.Platform;
import nidhogglike.game.NidhoggConfiguration;
import nidhogglike.game.NidhoggGameData;
import nidhogglike.input.Input;
import nidhogglike.particles.ParticleEmitter;
import nidhogglike.particles.behaviors.DelayedParticle;
import nidhogglike.particles.behaviors.DyingParticle;
import nidhogglike.particles.behaviors.GravityParticle;
import nidhogglike.particles.behaviors.MovingParticle;
import nidhogglike.particles.behaviors.ParticleBehavior;

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
	public static int WIDTH;
	public static int HEIGHT;
	public static int SPRITE_SIZE = 16;
	public static int FPS = 60;

	protected static int SMALL_WIDTH = 640;
	protected static int SMALL_HEIGHT = 480;

	protected static int BIG_WIDTH = 1024;
	protected static int BIG_HEIGHT = 768;
	
	protected static boolean bigScreen;

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
		ParticleEmitter emitter = new ParticleEmitter();
		
		Player j1 = new Player((NidhoggGameData) data, input, true);
		Player j2 = new Player((NidhoggGameData) data, input, false);
		j1.setParticleEmitter(emitter);
		j2.setParticleEmitter(emitter);
		Sword j1sword = new Sword(data, false);
		Sword j2sword = new Sword(data, true);

		j1.setSword(j1sword);
		j2.setSword(j2sword);

		universe.addGameEntity(j1);
		universe.addGameEntity(j2);
		universe.addGameEntity(j1sword);
		universe.addGameEntity(j2sword);
		universe.addGameEntity(new Ground());
		
		if (bigScreen) {
			addBigPlatforms();
		} else {
			addSmallPlatforms();
		}
		
		
		universe.addGameEntity(emitter);
	}

	private void addSmallPlatforms() {
		universe.addGameEntity(new Platform(50, 250, 100, 10));
		universe.addGameEntity(new Platform(WIDTH - 150, 250, 100, 10));
		universe.addGameEntity(new Platform(WIDTH / 2 - 50, 200, 100, 10));
		universe.addGameEntity(new Platform(WIDTH / 2 - 20, 280, 40, 50));
	}

	private void addBigPlatforms() {
		//TODO : Ajouter des plateformes
		universe.addGameEntity(new Platform(50, 450, 200, 10));
		universe.addGameEntity(new Platform(WIDTH - 250, 450, 200, 10));
		
		universe.addGameEntity(new Platform(WIDTH / 2 - 10, 510, 40, 40));
		
		universe.addGameEntity(new Platform(WIDTH / 2 - 150, 350, 300, 10));
	}

	/**
	 * First method called
	 * 
	 * @param args Command line parameters
	 */
	public static void main(String[] args) {
		bigScreen = Arrays.asList(args).contains("-b");
		Nidhogg.WIDTH = bigScreen ? BIG_WIDTH : SMALL_WIDTH;
		Nidhogg.HEIGHT = bigScreen ? BIG_HEIGHT : SMALL_HEIGHT;

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
