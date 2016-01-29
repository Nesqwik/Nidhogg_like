package nidhogglike.game;

import gameframework.game.GameConfiguration;
import gameframework.game.GameUniverse;
import gameframework.motion.blocking.MoveBlockerChecker;
import gameframework.motion.blocking.MoveBlockerCheckerDefaultImpl;
import gameframework.motion.blocking.MoveBlockerRulesApplier;
import gameframework.motion.overlapping.OverlapRulesApplier;
import nidhogglike.motion.NidhoggBlockerChecker;
import nidhogglike.motion.NidhoggBlockerRulesApplier;
import nidhogglike.motion.NidhoggOverlapRulesApplier;

public class NidhoggConfiguration extends GameConfiguration {

	protected NidhoggAnnouncer announcer;

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
		announcer = new NidhoggAnnouncer();
		return new NidhoggOverlapRulesApplier(announcer);
	}

	public NidhoggAnnouncer getAnnouncer() {
		return announcer;
	}
	
	@Override
	public MoveBlockerChecker createMoveBlockerChecker() {
		return new NidhoggBlockerChecker();
	}
}
