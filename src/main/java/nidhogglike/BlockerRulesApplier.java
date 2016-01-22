package nidhogglike;

import gameframework.motion.IllegalMoveException;
import gameframework.motion.blocking.MoveBlockerRulesApplierDefaultImpl;
import nidhogglike.entities.Player;

public class BlockerRulesApplier extends MoveBlockerRulesApplierDefaultImpl {

	public BlockerRulesApplier() {
		super();
	}

	public void moveBlockerRule(Player p, Wall blocker)
			throws IllegalMoveException {
		throw new IllegalMoveException();
	}

}
