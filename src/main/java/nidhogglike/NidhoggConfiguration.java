package nidhogglike;

import gameframework.game.GameConfiguration;
import gameframework.motion.GameMovable;
import gameframework.motion.IllegalMoveException;
import gameframework.motion.blocking.MoveBlocker;
import gameframework.motion.blocking.MoveBlockerRulesApplier;
import gameframework.motion.blocking.MoveBlockerRulesApplierDefaultImpl;

public class NidhoggConfiguration extends GameConfiguration {

	public NidhoggConfiguration(int nbRows, int nbColumns, int spriteSize, int nbLives) {
		super(nbRows, nbColumns, spriteSize, nbLives);
	}

	@Override
	public MoveBlockerRulesApplier createMoveBlockerRulesApplier() {
		MoveBlockerRulesApplierDefaultImpl applier = new MoveBlockerRulesApplierDefaultImpl() {
			@SuppressWarnings("unused")
			// tools can't see that this method is only called using reflexion
			public void moveBlockerRule(GameMovable movable, MoveBlocker blocker)
					throws IllegalMoveException {
				System.out.println("you should block bro");
			}
		};
		return applier;
	}
}
