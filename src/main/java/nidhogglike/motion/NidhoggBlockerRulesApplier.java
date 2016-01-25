package nidhogglike.motion;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Vector;

import gameframework.motion.GameMovable;
import gameframework.motion.IllegalMoveException;
import gameframework.motion.blocking.MoveBlocker;
import gameframework.motion.blocking.MoveBlockerRulesApplierDefaultImpl;
import nidhogglike.entities.Player;
import nidhogglike.entities.Wall;

public class NidhoggBlockerRulesApplier extends MoveBlockerRulesApplierDefaultImpl {
	/**
	 * The last moveBlocker in date to provoke a blocking rule that forbid a movement 
	 */
	protected MoveBlocker lastBlockingBlocker = null;
	
	public NidhoggBlockerRulesApplier() {
		super();
	}

	public void moveBlockerRule(Player p, Wall blocker)
			throws IllegalMoveException {
		throw new IllegalMoveException();
	}

	@Override
	public boolean moveValidationProcessing(GameMovable movable,
			Vector<MoveBlocker> blockers) {
		for (MoveBlocker moveBlocker : blockers) {
			try {
				moveBlockerRuleApply(movable, moveBlocker);
			} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException exception) {
				/*
				 * by default the moveBlocker implies the invalidation of the
				 * move (in particular, if no method has been found by
				 * moveBlockerRuleApply)
				 */
				lastBlockingBlocker = moveBlocker;
				return false;
			} catch (Exception exception) {
				exception.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	protected void moveBlockerRuleApply(GameMovable movable, MoveBlocker blocker)
			throws Exception {
		Method m = null;
		m = (getClass()).getMethod("moveBlockerRule", movable.getClass(),
				blocker.getClass());
		m.invoke(this, movable, blocker);
	}
	
	public MoveBlocker getLastBlockingBlocker() {
		return lastBlockingBlocker;
	}
}
