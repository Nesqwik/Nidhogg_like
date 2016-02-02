package nidhogglike.entities;

import static org.junit.Assert.*;

import java.awt.Rectangle;

import nidhogglike.game.NidhoggConfiguration;
import nidhogglike.game.NidhoggGameData;
import nidhogglike.input.Input;

import org.junit.Before;
import org.junit.Test;

public abstract class BarTest {

	protected Player player;
	protected NidhoggGameData data;

	@Before
	public void init() {
		data = new NidhoggGameData(new NidhoggConfiguration(0, 0, 0, 0));
		player = new Player(data, new Input(data.getCanvas()), true);
	}

	@Test
	public void testIsMovable() {
		assertFalse(createBar().isMovable());
	}

	@Test
	public void testGetBoundingBox() {
		assertEquals(new Rectangle(0,0,50,5), createBar().getBoundingBox());
	}

	protected abstract Bar createBar();

}
