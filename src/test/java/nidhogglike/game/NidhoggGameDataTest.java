package nidhogglike.game;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class NidhoggGameDataTest {

	NidhoggGameData data;
	
	@Before
	public void init() {
		data = new NidhoggGameData(new NidhoggConfiguration(0, 0, 0, 0));
	}

	@Test
	public void testSetObservableValue() {
		assertEquals(0, data.observableValues.size());
		data.setObservableValue("key", 2);
		assertEquals(1, data.observableValues.size());
		assertEquals(2, (int)data.getObservableValue("key").getValue());
	}
	
	@Test
	public void testSetObservableValueWhenExisting() {
		data.setObservableValue("key", 2);
		data.setObservableValue("key", 4);
		assertEquals(4, (int)data.getObservableValue("key").getValue());
	}

	@Test
	public void testIncrementObservableValue() {
		data.setObservableValue("key", 2);
		data.incrementObservableValue("key", 6);
		assertEquals(8, (int)data.getObservableValue("key").getValue());
	}

}
