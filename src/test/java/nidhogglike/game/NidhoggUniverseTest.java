package nidhogglike.game;

import static org.junit.Assert.*;

import nidhogglike.entities.Player;
import nidhogglike.entities.Sword;
import nidhogglike.input.Input;

import org.junit.Before;
import org.junit.Test;

public class NidhoggUniverseTest {

	NidhoggUniverse universe;
	NidhoggGameData data;
	Sword sword;
	
	@Before
	public void init() {
		universe = new NidhoggUniverse();
		NidhoggConfiguration config = new NidhoggConfiguration(0, 0, 0, 0);
		data = new NidhoggGameData(config);
		sword = new Sword(data, true);
		universe.setGameData(data);
	}
	
	@Test
	public void testAddGameEntity() {
		assertEquals(0, universe.swords.size());
		universe.addGameEntity(sword);
		assertEquals(1, universe.swords.size());
	}

	@Test
	public void testGetFreeSwordWhenExists() {
		universe.addGameEntity(sword);
		assertEquals(sword, universe.getFreeSword());
	}
	
	@Test
	public void testGetFreeSwordWhenNotExists() {
		universe.addGameEntity(sword);
		sword.setHolder(new Player(data, new Input(data.getCanvas()), true));
		assertNull(universe.getFreeSword());
	}

}
