package nidhogglike.game;

import nidhogglike.motion.NidhoggBlockerRulesApplier;
import gameframework.game.GameConfiguration;
import gameframework.game.GameUniverse;
import gameframework.motion.blocking.MoveBlockerRulesApplier;

public class NidhoggConfiguration extends GameConfiguration {

	public NidhoggConfiguration(int nbRows, int nbColumns, int spriteSize, int nbLives) {
		super(nbRows, nbColumns, spriteSize, nbLives);
	}

	@Override
	public MoveBlockerRulesApplier createMoveBlockerRulesApplier() {
		return new NidhoggBlockerRulesApplier();
	}
	
	public GameUniverse createUniverse() {
		return new NidhoggUniverse();
	}
}
