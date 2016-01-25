package nidhogglike.game;

import nidhogglike.motion.NidhoggBlockerRulesApplier;
import nidhogglike.motion.NidhoggOverlapRulesApplier;
import gameframework.game.GameConfiguration;
import gameframework.game.GameUniverse;
import gameframework.motion.blocking.MoveBlockerRulesApplier;
import gameframework.motion.overlapping.OverlapRulesApplier;

public class NidhoggConfiguration extends GameConfiguration {

	public NidhoggConfiguration(int nbRows, int nbColumns, int spriteSize, int nbLives) {
		super(nbRows, nbColumns, spriteSize, nbLives);
	}

	@Override
	public MoveBlockerRulesApplier createMoveBlockerRulesApplier() {
		return new NidhoggBlockerRulesApplier();
	}
	
	@Override
	public GameUniverse createUniverse() {
		return new NidhoggUniverse();
	}

	@Override
	public OverlapRulesApplier createOverlapRulesApplier() {
		return new NidhoggOverlapRulesApplier();
	}
}
