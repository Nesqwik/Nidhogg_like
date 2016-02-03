package nidhogglike.game;

import gameframework.game.GameConfiguration;
import gameframework.game.GameData;
import gameframework.game.GameUniverse;
import gameframework.motion.blocking.MoveBlockerChecker;
import gameframework.motion.blocking.MoveBlockerRulesApplier;
import gameframework.motion.overlapping.OverlapRulesApplier;
import nidhogglike.motion.NidhoggBlockerChecker;
import nidhogglike.motion.NidhoggBlockerRulesApplier;
import nidhogglike.motion.NidhoggOverlapRulesApplier;

public class NidhoggConfiguration extends GameConfiguration {

	protected NidhoggAnnouncer announcer;

	public NidhoggConfiguration(final int nbRows, final int nbColumns, final int spriteSize, final int nbLives) {
		super(nbRows, nbColumns, spriteSize, nbLives);
	}

	@Override
	public MoveBlockerRulesApplier createMoveBlockerRulesApplier() {
		return new NidhoggBlockerRulesApplier();
	}

	@Override
	public GameUniverse createUniverse(final GameData gameData) {
		return new NidhoggUniverse(gameData);
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
