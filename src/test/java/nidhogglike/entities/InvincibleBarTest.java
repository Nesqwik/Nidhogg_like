package nidhogglike.entities;

import static org.junit.Assert.*;

import java.awt.Rectangle;

import nidhogglike.game.NidhoggConfiguration;
import nidhogglike.game.NidhoggGameData;
import nidhogglike.input.Input;

import org.junit.Before;
import org.junit.Test;

public class InvincibleBarTest {

	
	private InvincibleBar invincibleBar;
	private Player player;
	private NidhoggGameData data;

	@Before
	public void init() {
		data = new NidhoggGameData(new NidhoggConfiguration(0, 0, 0, 0));
		player = new Player(data, new Input(data.getCanvas()), true);
		invincibleBar = new InvincibleBar(0, player);
	}

	@Test
	public void testIsMovable() {
		assertFalse(invincibleBar.isMovable());
	}

	@Test
	public void testGetBoundingBox() {
		assertEquals(new Rectangle(0,0,50,5), invincibleBar.getBoundingBox());
	}

}
