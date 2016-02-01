package nidhogglike.entities;

import static org.junit.Assert.*;

import gameframework.motion.SpeedVector;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import nidhogglike.Nidhogg;
import nidhogglike.game.NidhoggConfiguration;
import nidhogglike.game.NidhoggGameData;
import nidhogglike.input.Input;
import nidhogglike.particles.ParticleEmitter;

import org.junit.Before;
import org.junit.Test;

public class PlayerTest {
	
	private static final String PLAYER1_DATA_KEY = "player1";
	private static final String PLAYER2_DATA_KEY = "player2";
	Player player1;
	Player player2;
	NidhoggGameData data;
	Input input;
	
	@Before
	public void init() {
		data = new NidhoggGameData(new NidhoggConfiguration(0, 0, 0, 0));
		data.setObservableValue(PLAYER1_DATA_KEY, 0);
		data.setObservableValue(PLAYER2_DATA_KEY, 0);
		input = new Input(data.getCanvas());
		player1 = new Player(data, input, true);
		player2 = new Player(data, input, false);
	}

	@Test
	public void testOneStepMoveJump() throws AWTException {
		//jump
		player1.updateDirection();
		assertEquals(0, (int)player1.getVelocityY());
		player1.setPosition(new Point(100,100));
		input.keys[KeyEvent.VK_Z]=true;
		player1.oneStepMoveAddedBehavior();
		//100-10+1(1 represent the gravity)
		assertEquals(91, player1.getPosition().y);
	}
	
	@Test
	public void testOneStepMoveThrowSword() throws AWTException {
		//throw sword
		player1.setSword(new Sword(data, true));
		input.keys[KeyEvent.VK_A]=true;
		player1.oneStepMoveAddedBehavior();
		assertNull(this.player1.getSword());
		//When he hasn't the sword
		player1.oneStepMoveAddedBehavior();
		assertNull(this.player1.getSword());
	}
	
	@Test
	public void testOneStepMoveDuck() throws AWTException {
		//dunk
		input.keys[KeyEvent.VK_S]=true;
		player1.oneStepMoveAddedBehavior();
		//TODO
	}

	@Test
	public void testResetPosition() {
		testPosition(player1, 75, 0);
		testPosition(player2, Nidhogg.WIDTH - 125, 0);
	}

	protected void testPosition(Player player, int x, int y) {
		player.resetPosition();
		assertEquals(x, player.getPosition().x);
		assertEquals(y, player.getPosition().y);
	}

	@Test
	public void testGetBoundingBox() {
		assertEquals(new Rectangle(40, player1.getBoundingBox().height), player1.getBoundingBox());
	}

	@Test
	public void testOutOfBoundsVerificationWithPositiveX() {
		player1.setPosition(new Point(player1.getBoundingBox().width+10, 100));
		player1.outOfBoundsVerification();
		assertEquals(-player1.getBoundingBox().width, player1.getPosition().x);
	}
	
	@Test
	public void testOutOfBoundsVerificationWithXMoreLittleThatTheWidth() {
		player1.setPosition(new Point(-player1.getBoundingBox().width-10,100));
		player1.outOfBoundsVerification();
		assertEquals(Nidhogg.WIDTH, player1.getPosition().x);
	}

	@Test
	public void testDuck() {
		player1.setPosition(new Point(100,100));
		player1.duck();
		assertTrue(player1.isDucking());
		assertEquals(100, player1.getPosition().x);
		assertEquals(125, player1.getPosition().y);
		assertEquals(25, player1.getBoundingBox().height);
	}

	@Test
	public void testUnduck() {
		player1.setPosition(new Point(100,100));
		player1.unduck();
		assertFalse(player1.isDucking());
		assertEquals(100, player1.getPosition().x);
		assertEquals(75, player1.getPosition().y);
		assertEquals(50, player1.getBoundingBox().height);
	}

	@Test
	public void testApplyGravity() {
		player1.setPosition(new Point(50,50));
		assertEquals(0, (int)player1.getVelocityY());
		player1.applyGravity();
		assertEquals(1, (int)player1.getVelocityY());
	}

	@Test
	public void testGroundCollision() {
		player1.groundCollision(new Platform(10,10,10,10));
		assertFalse(player1.isJumping());
		assertEquals(0, (int)player1.getVelocityY());		
	}

	@Test
	public void testUpdateDirection() {
		player1.setSpeedVector(new SpeedVector(new Point(-1,0), 5));
		player1.updateDirection();
		assertTrue(player1.isHeadingLeft());
		assertEquals((int)0.5 , (int)player1.getFakeVelocityX());
	}

	@Test
	public void testUpdateAnimation() {
		//TODO
	}

	@Test
	public void testIsHoldingSword() {
		assertFalse(player1.isHoldingSword());
		player1.setSword(new Sword(data, true));
		assertTrue(player1.isHoldingSword());
	}

	@Test
	public void testIsHeadingLeft() {
		assertFalse(player1.isHeadingLeft());
		assertTrue(player2.isHeadingLeft());
	}

	@Test
	public void testEmitParticle() {
		//TODO
	}

	@Test
	public void testHit() {
		player1.setSurpriseGift(new SurpriseGift(data));
		player1.setParticleEmitter(new ParticleEmitter());
		assertFalse(player1.hit());
		for (int i=0; i<60; i++)
			player1.oneStepMoveAddedBehavior();
		player1.setCurrentLife(0);
		assertTrue(player1.hit());
	}

	@Test
	public void testDie() {
		//TODO
	}

	@Test
	public void testRecoverSwordIfNeeded() {
		player1.recoverSwordIfNeeded();
		assertNull(player1.getSword());
		data.getUniverse().addGameEntity(new Sword(data, true));
		player1.recoverSwordIfNeeded();
		assertNotNull(player1.getSword());
	}

	@Test
	public void testAddGift() {
		SurpriseGift surpriseGift = new SurpriseGift(data);
		player1.setSurpriseGift(surpriseGift);
		player1.addGift();
		//Gift not add because the player's score is 0
		assertEquals(0, (int)this.data.getObservableValue(PLAYER1_DATA_KEY).getValue());
		assertTrue(surpriseGift.isOpened());
		//With this score, the gift is added
		data.getObservableValue(PLAYER1_DATA_KEY).setValue(2);
		player1.addGift();
		assertFalse(surpriseGift.isGood());
		
	}

	@Test
	public void testRefinePositionAfterLateralCollision() {
		//TODO
	}

	@Test
	public void testRoofCollision() {
		player1.setPosition(new Point(100,100));
		player1.roofCollision(null);
		assertEquals(0, (int)player1.getVelocityY());
		assertEquals(105, player1.getPosition().y);
	}

	@Test
	public void testModificationScoreAtBegining() {
		testLifeAndScore(-1, 0, 3, 0, 2, player1);
		testLifeAndScore(1, 0, 3, 0 ,3 , player2);
	}
	
	@Test
	public void testModificationScore() {
		testLifeAndScore(-1, 0, 1, 1, 3, player1);
		testLifeAndScore(1, 0, 1, 0, 2, player2);
	}
	
	@Test
	public void testModificationScore2() {
		testLifeAndScore(-1, 5, 1, 6, 3, player1);
		testLifeAndScore(1, 5, 1, 5, 2, player2);
	}
	
	@Test
	public void testModificationScore3() {
		testLifeAndScore(-1, 3, 2, 3, 1, player1);
		testLifeAndScore(1, 3, 2,3,3, player2);
	}
	
	protected void testLifeAndScore(int number, int scoreValue, int currentLife, int expectedScore, int expectedCurrentLife, Player player) {
		data.getObservableValue(PLAYER1_DATA_KEY).setValue(scoreValue);
		player.setCurrentLife(currentLife);
		player.modificationScore(number);
		int score = data.getObservableValue(PLAYER1_DATA_KEY).getValue();
		assertEquals(expectedScore, score);
		assertEquals(expectedCurrentLife, player.getCurrentLife());
	}

	@Test
	public void testIsKilledBy() {
		assertFalse(player1.isKilledBy(player2));
	}

	@Test
	public void testPushBackwards() {
		//No Out Of Bound Verification
		player1.setPosition(new Point(-2,100));
		player1.setSpeedVector(new SpeedVector(new Point(-1,0), 5));
		player1.pushBackwards();
		assertFalse(player1.isHeadingLeft());
		assertEquals(-7, player1.getPosition().x);
	}


}
