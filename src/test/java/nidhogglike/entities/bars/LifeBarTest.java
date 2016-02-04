package nidhogglike.entities.bars;

import nidhogglike.entities.bars.Bar;
import nidhogglike.entities.bars.LifeBar;

public class LifeBarTest extends BarTest {

	@Override
	protected Bar createBar() {
		return new LifeBar(0, player);
	}


}
