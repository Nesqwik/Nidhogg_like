package nidhogglike.entities;

import static org.junit.Assert.*;

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
	public void testIsMovable() {
		assertTrue(gift.isMovable());
	}

	@Test
	public void testTakingGiftWithSimpleLifeBar() {
		gift.setGift(0, player);
		player.setCurrentLife(2);
		gift.takingGift(player);
		assertNotEquals(2, player.getCurrentLife());
		if (gift.isGood()) {
			assertEquals(3, player.getCurrentLife());
		} else {
			assertEquals(1, player.getCurrentLife());
		}
	}
	
	@Test
	public void testTakingGiftWithSimpleCompleteLifeBarAndZeroScore() {
		testLifeAndScoreAfterTakingGift(0, 3);
	}
	
	@Test
	public void testTakingGiftWithSimpleCompleteLifeBarAndPositiveScore() {
		testLifeAndScoreAfterTakingGift(2, 3);
	}
	
	
	@Test
	public void testTakingGiftWithSimpleEmptyLifeBarAndPositiveScore() {
		testLifeAndScoreAfterTakingGift(2, 1);
	}
	
	@Test
	public void testTakingGiftWithSimpleEmptyLifeBarAndZeroScore() {
		testLifeAndScoreAfterTakingGift(0, 1);
	}

	protected void testLifeAndScoreAfterTakingGift(int scoreValue, int currentLife) {
		data.getObservableValue(PLAYER1_DATA_KEY).setValue(scoreValue);
		player.setCurrentLife(currentLife);
		gift.setGift(0, player);
		gift.takingGift(player);
		int score = data.getObservableValue(PLAYER1_DATA_KEY).getValue();
		testGift(scoreValue, score, currentLife, player);
	}

	protected void testGift(int scoreBefore, int score, int lifeBarBefore, Player player) {
		if (gift.isGood()) {
			if (lifeBarBefore < 3) {
				assertEquals(lifeBarBefore + 1, player.getCurrentLife());
				assertEquals(scoreBefore, score);
			} else {
				if (scoreBefore > 0) {
					assertEquals(lifeBarBefore - 2, player.getCurrentLife());
					assertEquals(scoreBefore -1, score);
				} else {
					assertEquals(scoreBefore, score);
					assertEquals(lifeBarBefore, player.getCurrentLife());
				}
			}
		} else {
			if (lifeBarBefore > 1) {
				assertEquals(lifeBarBefore - 1, player.getCurrentLife());
				assertEquals(scoreBefore, score);
			} else {
				assertEquals(lifeBarBefore +2, player.getCurrentLife());
				assertEquals(scoreBefore+1, score);
			}
		}
	}

	@Test
	public void testIsOpened() {
		gift.setGift(0, player);
		assertFalse(gift.isOpened());
		gift.takingGift(player);
		assertTrue(gift.isOpened());
	}

}
