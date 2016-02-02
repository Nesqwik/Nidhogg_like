package nidhogglike.entities;


public class LifeBarTest extends BarTest {

	@Override
	protected Bar createBar() {
		return new LifeBar(0, player);
	}


}
