package nidhogglike.entities;

import static org.junit.Assert.*;

import java.awt.Point;
import java.awt.Rectangle;

import nidhogglike.entities.obstacles.Platform;
import nidhogglike.game.NidhoggConfiguration;
import nidhogglike.game.NidhoggGameData;
import nidhogglike.input.Input;

import org.junit.Before;
import org.junit.Test;

public class SwordTest {

	Sword swordLeft;
	Sword swordRight;
	NidhoggGameData data;
	Player player;
	
	@Before
	public void init() {
		data = new NidhoggGameData(new NidhoggConfiguration(100,100,100,100));
		swordLeft = new Sword(data, true);
		swordRight = new Sword(data, false);
		player = new Player(data, new Input(data.getCanvas()), true);
	}

	@Test
	public void testIsHeld() {
		assertFalse(swordLeft.isHeld());
		swordLeft.setHolder(player);
		assertTrue(swordLeft.isHeld());
	}

	@Test
	public void testPlayerThrow() {
		swordLeft.setHolder(player);
		assertEquals(player, swordLeft.getHolder());
		swordLeft.playerThrow();
		assertNull(swordLeft.getHolder());
		assertEquals(player, swordLeft.getLastHolder());
		assertTrue(swordLeft.isMoving());
	}

	@Test
	public void testSetHolderAndLastHolderAndHolder() {
		Player player2 = new Player(data, new Input(data.getCanvas()), false);
		swordLeft.setHolder(player);
		assertEquals(player, swordLeft.getHolder());
		assertEquals(player, swordLeft.getLastHolder());
		swordLeft.setHolder(player2);
		assertEquals(player2, swordLeft.getHolder());
		assertEquals(player2, swordLeft.getLastHolder());
	}
	
	@Test
	public void testGetBoundingBox() {
		assertEquals(new Rectangle(50,10), swordLeft.getBoundingBox());
	}

	@Test
	public void testGroundCollision() {
		swordLeft.setPosition(new Point(100,100));
		swordLeft.groundCollision(new Platform(0,0,0,0));
		assertEquals(100, swordLeft.getPosition().x);
		assertEquals(100, swordLeft.getPosition().y);
		assertEquals(0, (int)swordLeft.getVelocity_x());
		assertEquals(0, (int)swordLeft.getVelocity_y());
		assertFalse(swordLeft.isMoving());
	}
	
	@Test
	public void testSetMoving() {
		swordLeft.setMoving(false);
		assertFalse(swordLeft.isMoving());
		assertEquals(0, (int)swordLeft.getVelocity_x());
	}
	
	@Test
	public void testApplyGravity() {
		swordLeft.setPosition(new Point(100,100));
		swordLeft.applyGravity();
		assertEquals(100, swordLeft.getPosition().x);
		assertEquals(101, swordLeft.getPosition().y);
	}
	
	@Test
	public void testOutOfBoundsVerification() {
		swordLeft.getBoundingBox().setBounds(0, 0, 50, 100);
		swordLeft.setPosition(new Point(1000,0));
		swordLeft.outOfBoundsVerification();
		assertEquals(-50, swordLeft.getPosition().x);
	}
	
	@Test
	public void testOutOfBoundsVerificationLittleX() {
		swordLeft.getBoundingBox().setBounds(0, 0, 50, 100);
		swordLeft.setPosition(new Point(-1000,0));
		swordLeft.outOfBoundsVerification();
		assertEquals(0, swordLeft.getPosition().x);
	}
	
	@Test
	public void testOneStepMoveAddedBehaviour() {
		swordLeft.setPosition(new Point(0,0));
		swordLeft.setVelocity_x(5);
		swordLeft.oneStepMoveAddedBehavior();
		assertEquals(-5, swordLeft.getPosition().x);
	}
	
}
