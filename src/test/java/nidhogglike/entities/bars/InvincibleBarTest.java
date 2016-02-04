package nidhogglike.entities.bars;

import nidhogglike.entities.bars.Bar;
import nidhogglike.entities.bars.InvincibleBar;

public class InvincibleBarTest extends BarTest {

	@Override
	protected Bar createBar() {
		return new InvincibleBar(0, player);
	}


}
