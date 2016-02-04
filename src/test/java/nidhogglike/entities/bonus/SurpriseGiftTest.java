package nidhogglike.entities.bonus;

import static org.junit.Assert.*;

import java.awt.Point;
import java.awt.Rectangle;

import nidhogglike.entities.Player;
import nidhogglike.entities.bonus.SurpriseGift;
import nidhogglike.entities.obstacles.Platform;
import nidhogglike.game.NidhoggConfiguration;
import nidhogglike.game.NidhoggGameData;
import nidhogglike.input.Input;

import org.junit.Before;
import org.junit.Test;

public class SurpriseGiftTest {

	private static final String PLAYER1_DATA_KEY = "player1";
	SurpriseGift gift;
	Player player;
	NidhoggGameData data;
	
	@Before
	public void init() {
		data = new NidhoggGameData(new NidhoggConfiguration(0,0,0,0));
		data.setObservableValue(PLAYER1_DATA_KEY, 0);
		gift = new SurpriseGift(data);
		player = new Player(data, new Input(data.getCanvas()), true);
		
	}
	
	@Test
	public void testSetGift() {
		gift.setGift(2, player);
		assertFalse(gift.isOpened());
		assertEquals(2, gift.getPosition().x);
		assertEquals(0, gift.getPosition().y);
		assertTrue(gift.isMoving());
	}
	
	@Test
	public void testBonusSwordGift() {
		gift.bonusSwordGift(player);
		assertEquals(3, player.getStrongerSword());
	}
	
	@Test
	public void testPointGift() {
		gift.pointGift(player);
		assertEquals(3, player.getCurrentLife());
	}
	
	@Test
	public void testInvincibleGift() {
		gift.invincibleGift(player);
		assertTrue(player.stillInvincible());
	}

	@Test
	public void testIsMovable() {
		assertTrue(gift.isMovable());
	}

	@Test
	public void testIsOpened() {
		gift.setGift(0, player);
		assertFalse(gift.isOpened());
		gift.takingGift(player);
		assertTrue(gift.isOpened());
	}
	
	@Test
	public void testGroundCollision() {
		gift.groundCollision(new Platform(0,0,0,0));
		assertEquals(-49, gift.getPosition().y);
		assertTrue(gift.isOnGround());
		assertFalse(gift.isMoving());
	}
	
	@Test
	public void testGetBoundingBox() {
		assertEquals(new Rectangle(50,50), gift.getBoundingBox());
	}
	
	@Test
	public void testAppear() {
		gift.appear();
		assertTrue(gift.isMoving());
	}
	
	@Test
	public void testApplyGravity() {		
		gift.setPosition(new Point(100,100));
		gift.applyGravity();
		assertEquals(100, gift.getPosition().x);
		assertEquals(101, gift.getPosition().y);
	}
	
	@Test
	public void testTakingGift() {
		gift.setGift(100, player);
		gift.takingGift(player);
		assertTrue(gift.isOpened());
	}
	
	@Test
	public void testTaking2GiftSword() {
		gift.bonusSwordGift(player);
		assertEquals(3, player.getStrongerSword());
		gift.bonusSwordGift(player);
		assertEquals(3, player.getStrongerSword());
	}
	
	@Test
	public void testGiftOpenWithoutGravity() {
		gift.setGift(100, player);
		gift.takingGift(player);
		gift.setPosition(new Point(100,100));
		gift.oneStepMoveAddedBehavior();
		assertEquals(100, gift.getPosition().y);
	}
	
	@Test
	public void testGiftOpenWithGravity() {
		gift.setGift(100, player);
		gift.setPosition(new Point(100,100));
		gift.oneStepMoveAddedBehavior();
		assertEquals(101, gift.getPosition().y);
	}
	
	@Test
	public void testGiftAppears() {
		player.setCurrentLife(2);
		gift.invincibleGift(player);
		gift.pointGift(player);
		gift.takingGift(player);
		assertEquals(3, player.getCurrentLife());
	}
	
	@Test
	public void testGiftAppearsPointAndInvincible() {
		player.setCurrentLife(2);
		gift.pointGift(player);
		gift.invincibleGift(player);
		gift.takingGift(player);
		assertEquals(3, player.getCurrentLife());
	}

}
