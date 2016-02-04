package nidhogglike.motion;

import gameframework.motion.GameMovable;
import gameframework.motion.SpeedVector;
import gameframework.motion.blocking.MoveBlocker;
import gameframework.motion.blocking.MoveBlockerCheckerDefaultImpl;
import gameframework.motion.blocking.MoveBlockerRulesApplier;
import gameframework.motion.blocking.MoveBlockerRulesApplierDefaultImpl;

import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NidhoggBlockerChecker extends MoveBlockerCheckerDefaultImpl {
	/**
	 * A queue containing all the moveBlockers to check for when verifying if a
	 * GameMovable can move
	 */
	protected ConcurrentLinkedQueue<MoveBlocker> moveBlockers;

	/**
	 * The rule applier used when checking if a GameMovable can move
	 */
	protected MoveBlockerRulesApplier moveBlockerRuleApplier;

	public NidhoggBlockerChecker() {
		moveBlockers = new ConcurrentLinkedQueue<MoveBlocker>();
		this.moveBlockerRuleApplier = new MoveBlockerRulesApplierDefaultImpl();
	}

	/**
	 * @see gameframework.motion.blocking.MoveBlockerChecker#addMoveBlocker(gameframework.motion.blocking.MoveBlocker)
	 */
	@Override
	public void addMoveBlocker(final MoveBlocker p) {
		moveBlockers.add(p);
	}

	/**
	 * @see gameframework.motion.blocking.MoveBlockerChecker#removeMoveBlocker(gameframework.motion.blocking.MoveBlocker)
	 */
	@Override
	public void removeMoveBlocker(final MoveBlocker p) {
		moveBlockers.remove(p);
	}

	/**
	 * @see gameframework.motion.blocking.MoveBlockerChecker#setMoveBlockerRules(gameframework.motion.blocking.MoveBlockerRulesApplier)
	 */
	@Override
	public void setMoveBlockerRules(final MoveBlockerRulesApplier moveBlockerRules) {
		this.moveBlockerRuleApplier = moveBlockerRules;
	}

	/**
	 * @see gameframework.motion.blocking.MoveBlockerChecker#moveValidation(gameframework.motion.GameMovable, gameframework.motion.SpeedVector)
	 */
	@Override
	public boolean moveValidation(final GameMovable m, final SpeedVector mov) {
		final Vector<MoveBlocker> moveBlockersInIntersection = new Vector<MoveBlocker>();

		for (final MoveBlocker moveBlocker : moveBlockers) {
			if (moveBlocker.getBoundingBox().intersects(m.getBoundingBox())) {
				moveBlockersInIntersection.add(moveBlocker);
			}
		}

		if (!moveBlockersInIntersection.isEmpty()) {
			return moveBlockerRuleApplier.moveValidationProcessing(m,
					moveBlockersInIntersection);
		}

		return true;
	}
}
