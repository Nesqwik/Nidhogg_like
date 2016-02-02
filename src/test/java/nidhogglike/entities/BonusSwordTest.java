package nidhogglike.entities;

import static org.junit.Assert.*;

import java.awt.Rectangle;

import nidhogglike.game.NidhoggConfiguration;
import nidhogglike.game.NidhoggGameData;
import nidhogglike.input.Input;

import org.junit.Before;
import org.junit.Test;

public class BonusSwordTest {

	BonusSword sword;
	NidhoggGameData data;
	Player player;
	
	@Before
	public void init() {
		data = new NidhoggGameData(new NidhoggConfiguration(0, 0, 0, 0));
		player = new Player(data, new Input(data.getCanvas()), true);
		sword = new BonusSword(data, 2, player);
	}

	@Test
	public void testGetBoundingBox() {
		assertEquals(new Rectangle(40,40), sword.getBoundingBox());
	}

}
