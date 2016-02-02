package nidhogglike.entities;


public class InvincibleBarTest extends BarTest {

	@Override
	protected Bar createBar() {
		return new InvincibleBar(0, player);
	}


}
