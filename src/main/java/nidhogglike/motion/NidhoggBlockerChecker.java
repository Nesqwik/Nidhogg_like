package nidhogglike.motion;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

import gameframework.motion.GameMovable;
import gameframework.motion.IntersectTools;
import gameframework.motion.SpeedVector;
import gameframework.motion.blocking.MoveBlocker;
import gameframework.motion.blocking.MoveBlockerCheckerDefaultImpl;
import gameframework.motion.blocking.MoveBlockerRulesApplier;
import gameframework.motion.blocking.MoveBlockerRulesApplierDefaultImpl;
import nidhogglike.entities.Platform;

public class NidhoggBlockerChecker extends MoveBlockerCheckerDefaultImpl {
	private ConcurrentLinkedQueue<MoveBlocker> moveBlockers;
	private MoveBlockerRulesApplier moveBlockerRuleApplier;

	public NidhoggBlockerChecker() {
		moveBlockers = new ConcurrentLinkedQueue<MoveBlocker>();
		this.moveBlockerRuleApplier = new MoveBlockerRulesApplierDefaultImpl();
	}

	@Override
	public void addMoveBlocker(MoveBlocker p) {
		moveBlockers.add(p);
	}

	@Override
	public void removeMoveBlocker(MoveBlocker p) {
		moveBlockers.remove(p);
	}

	@Override
	public void setMoveBlockerRules(MoveBlockerRulesApplier moveBlockerRules) {
		this.moveBlockerRuleApplier = moveBlockerRules;
	}

	@Override
	public boolean moveValidation(GameMovable m, SpeedVector mov) {
		Shape intersectShape = IntersectTools.getIntersectShape(m, mov);
		Vector<MoveBlocker> moveBlockersInIntersection = new Vector<MoveBlocker>();

		for (MoveBlocker moveBlocker : moveBlockers) {
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
