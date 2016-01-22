package nidhogglike;

import gameframework.game.GameConfiguration;
import gameframework.motion.blocking.MoveBlockerRulesApplier;

public class NidhoggConfiguration extends GameConfiguration {

	public NidhoggConfiguration(int nbRows, int nbColumns, int spriteSize, int nbLives) {
		super(nbRows, nbColumns, spriteSize, nbLives);
	}

	@Override
	public MoveBlockerRulesApplier createMoveBlockerRulesApplier() {
		return new BlockerRulesApplier();
	}
}
