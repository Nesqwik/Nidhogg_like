package nidhogglike.motion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Point;

import nidhogglike.Nidhogg;
import nidhogglike.entities.Player;
import nidhogglike.entities.SurpriseGift;
import nidhogglike.entities.Sword;
import nidhogglike.game.NidhoggAnnouncer;
import nidhogglike.game.NidhoggConfiguration;
import nidhogglike.game.NidhoggGameData;
import nidhogglike.input.Input;
import nidhogglike.particles.ParticleEmitter;

import org.junit.Before;
import org.junit.Test;

public class NidhoggOverlapRulesApplierTest {

	private NidhoggOverlapRulesApplier rulesOverlap;
	private NidhoggGameData data;
	private Sword sword;
	private Player player;
	private Player player2;
	private SurpriseGift surpriseGift;
	private Sword sword2;

	@Before
	public void init() {
		data = new NidhoggGameData(new NidhoggConfiguration(0, 0, 0, 0));
		data.setObservableValue(Nidhogg.PLAYER1_DATA_KEY, 0);
		data.setObservableValue(Nidhogg.PLAYER2_DATA_KEY, 0);
		sword = new Sword(data, true);
		sword2 = new Sword(data, false);
		player = new Player(data, new Input(data.getCanvas()), true);
		player.setParticleEmitter(new ParticleEmitter());
		player2 = new Player(data, new Input(data.getCanvas()), false);
		player2.setParticleEmitter(new ParticleEmitter());
		surpriseGift = new SurpriseGift(data);
		NidhoggAnnouncer announcer = ((NidhoggConfiguration) data.getConfiguration()).getAnnouncer();
		announcer.addPlayer(player);
		announcer.addPlayer(player2);
		rulesOverlap = new NidhoggOverlapRulesApplier(announcer);
	}

	@Test
	public void testOverlapRuleSwordPlayerWhenHolderIsPlayer() {
		player.setSword(sword);
		rulesOverlap.overlapRule(sword, player);
	}

	@Test
	public void testOverlapRuleSwordPlayerWithOtherPlayer() {
		player.setCurrentLife(3);
		player2.setSword(sword);
		rulesOverlap.overlapRule(sword, player);
		assertFalse(player.isKilledBy(player2));
		assertFalse(player.hit());
		assertEquals(2, player.getCurrentLife());
	}
	
	@Test
	public void testOverlapRuleSwordPlayerWhenPlayerWantTakeSword() {
		rulesOverlap.overlapRule(sword, player);
		assertEquals(sword, player.getSword());
	}
	
	@Test
	public void testOverlapRuleSwordWhenPlayerIsInvincible() {
		player.setCurrentLife(3);
		data.getObservableValue(Nidhogg.PLAYER1_DATA_KEY).setValue(2);
		sword2.setHolder(player2);
		player.setMaxInvincibleLife(1000);
		player.invincible();
		assertTrue(player.stillInvincible());
		rulesOverlap.overlapRule(sword, player);
		assertEquals(3, player.getCurrentLife());
		assertEquals(2, (int)data.getObservableValue(Nidhogg.PLAYER1_DATA_KEY).getValue());
	}
	
	@Test
	public void testOverlapRuleSwordPlayerWithBonus() {
		data.getObservableValue(Nidhogg.PLAYER1_DATA_KEY).setValue(2);
		player2.setSword(sword2);
		player2.swordStronger();
		assertEquals(3, player2.getStrongerSword());
		assertNull(player.getSword());
		rulesOverlap.overlapRule(sword2, player);
		assertEquals(3, (int)data.getObservableValue(Nidhogg.PLAYER1_DATA_KEY).getValue());
		assertEquals(2, player2.getStrongerSword());
		assertEquals(3, player.getCurrentLife());
	}
	
	@Test
	public void testOverlapRuleSwordPlayerWithBonusAndTakeHisSword() {
		data.getObservableValue(Nidhogg.PLAYER2_DATA_KEY).setValue(2);
		player2.setCurrentLife(3);
		player2.swordStronger();
		assertEquals(3, player2.getStrongerSword());
		assertNull(sword2.getHolder());
		assertNull(player2.getSword());
		rulesOverlap.overlapRule(sword2, player2);
		assertEquals(2, (int)data.getObservableValue(Nidhogg.PLAYER2_DATA_KEY).getValue());
		assertEquals(3, player2.getStrongerSword());
		assertEquals(3, player.getCurrentLife());
		assertEquals(sword2, player2.getSword());
	}
	
	@Test
	public void testSameDirectionPlayers() {
		player.setSword(sword);
		player2.setCurrentLife(2);
		sword.setMoving(false);
		rulesOverlap.overlapRule(sword, player2);
		assertEquals(2, player2.getCurrentLife());
	}
	
	@Test
	public void testOppositeDirectionPlayers() {
		player.setSword(sword);
		Player player3 = new Player(data, new Input(data.getCanvas()), true);
		player3.setCurrentLife(2);
		player3.setParticleEmitter(new ParticleEmitter());
		sword.setMoving(false);
		rulesOverlap.overlapRule(sword, player3);
		assertEquals(1, player3.getCurrentLife());
	}
	
	@Test
	public void testOverlapRuleSwordPlayerWhenHeCanBeDead() {
		data.getObservableValue(Nidhogg.PLAYER1_DATA_KEY).setValue(2);
		player.setCurrentLife(1);
		player2.setSword(sword2);
		rulesOverlap.overlapRule(sword2, player);
		assertEquals(3, player.getCurrentLife());
		assertEquals(3, (int)data.getObservableValue(Nidhogg.PLAYER1_DATA_KEY).getValue());
	}
	
	@Test
	public void testOverlapRuleSwordSword() {
		player.setPosition(new Point(0,0));
		player2.setPosition(new Point(0,0));
		player.setSword(sword);
		player2.setSword(sword2);
		rulesOverlap.overlapRule(sword, sword2);
		assertEquals(0, player.getPosition().x);
		assertEquals(0, player2.getPosition().x);
	}
	
	@Test
	public void testOverlapRuleSwordSword2() {
		player.setSword(sword);
		assertTrue(sword.isHeld());
		sword2.setMoving(true);
		assertTrue(sword2.isMoving());
		rulesOverlap.overlapRule(sword, sword2);
		assertEquals(-2, (int)sword2.getVelocity_x());
	}
	
	@Test
	public void testOverlapRuleSwordThrowAndTake() {
		player.setSword(sword);
		sword.playerThrow();
		assertNull(sword.getHolder());
		rulesOverlap.overlapRule(sword, player);
		assertEquals(sword, player.getSword());
	}
	
	@Test
	public void testOverlapRuleSwordSword3() {
		player.setSword(sword2);
		assertTrue(sword2.isHeld());
		sword.setMoving(true);
		assertTrue(sword.isMoving());
		rulesOverlap.overlapRule(sword, sword2);
		assertEquals(-2, (int)sword.getVelocity_x());
	}

	@Test
	public void testOverlapRuleSurpriseGiftPlayer() {
		surpriseGift.setGift(0, player);
		rulesOverlap.overlapRule(surpriseGift, player);
		assertTrue(surpriseGift.isOpened());
	}

}
