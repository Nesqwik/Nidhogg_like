package nidhogglike.entities;

import static org.junit.Assert.*;

import java.awt.Point;


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
	public void testOneStepMoveAddedBehaviorWithPositionOfTheLeftSword() {
		oneStepMove(swordLeft, (-1));
	}
	
	@Test
	public void testOneStepMoveAddedBehaviorWithPositionOfTheRightSword() {
		oneStepMove(swordRight, 1);
	}

	protected void oneStepMove(Sword sword, int sign) {
		sword.setPosition(new Point(100,100));
		sword.oneStepMoveAddedBehavior();
		assertEquals((int) (100 + sign * sword.getVelocity_x()), sword.getPosition().x);
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

}
