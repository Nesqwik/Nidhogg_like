package nidhogglike.game;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class NidhoggConfigurationTest{

	NidhoggConfiguration config;
	
	@Before
	public void init() {
		config = new NidhoggConfiguration(12, 25, 45, 50);
	}
	
	@Test
	public void testNidhoggConfiguration() {
		assertEquals(12, config.getNbRows());
		assertEquals(25, config.getNbColumns());
		assertEquals(45, config.getSpriteSize());
		assertEquals(50, config.getDefaultNbLives());
	}
	
	@Test
	public void testCreateMoveBlockerRulesApplier() {
		assertNotNull(config.createMoveBlockerRulesApplier());
	}

	@Test
	public void testCreateOverlapRulesApplier() {
		assertNotNull(config.createOverlapRulesApplier());
	}

	@Test
	public void testCreateUniverse() {
		assertNotNull(config.createUniverse());
	}

	@Test
	public void testGetAnnouncer() {
		assertNull(config.getAnnouncer());
	}
	
	@Test 
	public void testCreateOverlapProcessor() {
		assertNotNull(config.createOverlapProcessor());
	}
	
	@Test
	public void testCreateMoveBlockerChecker() {
		assertNotNull(config.createMoveBlockerChecker());
	}
	

}
