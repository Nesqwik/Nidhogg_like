package nidhogglike.motion;

import gameframework.motion.GameMovable;
import gameframework.motion.IllegalMoveException;
import gameframework.motion.blocking.MoveBlocker;
import gameframework.motion.blocking.MoveBlockerRulesApplierDefaultImpl;

import java.awt.Rectangle;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Vector;

import nidhogglike.entities.HeadBalloon;
import nidhogglike.entities.Player;
import nidhogglike.entities.Sword;
import nidhogglike.entities.bonus.SurpriseGift;
import nidhogglike.entities.obstacles.Ground;
import nidhogglike.entities.obstacles.Platform;

public class NidhoggBlockerRulesApplier extends MoveBlockerRulesApplierDefaultImpl {
	/**
	 * The last moveBlocker in date to provoke a blocking rule that forbid a movement
	 */
	protected MoveBlocker lastBlockingBlocker = null;

	public NidhoggBlockerRulesApplier() {
		super();
	}

	public void moveBlockerRule(final Player p, final Platform platform)
			throws IllegalMoveException {

		if (!p.getBoundingBox().intersects(platform.getBoundingBox()))
			return;
		else {
			// This rule was done this way to avoid slowdowns provoked by the java exception system.
			final int feetY = p.getPosition().y + p.getBoundingBox().height;
			final Rectangle collision = p.getBoundingBox().intersection(platform.getBoundingBox());
			if (collision.width > collision.height) {
				if (p.getVelocityY() >= 0 && feetY < (platform.getBoundingBox().y + platform.getBoundingBox().height)) {
						p.groundCollision(platform);
				}
			} else {
				if (p.getVelocityY() <= 0 && !p.isJumping()) {
					p.refinePositionAfterLateralCollision(platform);
				}
			}
		}
	}
	public void moveBlockerRule(final HeadBalloon b, final Platform platform)
			throws IllegalMoveException {

		b.handleCollision(platform);
		throw new IllegalMoveException();
	}

	public void moveBlockerRule(final HeadBalloon b, final Ground ground)
			throws IllegalMoveException {
		b.platformCollision(ground);
	}

	public void moveBlockerRule(final Player p, final Ground ground)
			throws IllegalMoveException {
		p.groundCollision(ground);
	}

	public void moveBlockerRule(final Sword s, final Ground ground)
			throws IllegalMoveException {
		s.groundCollision(ground);
	}

	public void moveBlockerRule(final Sword s, final Platform p)
			throws IllegalMoveException {
		if (!s.isHeld()) {
			s.setMoving(false);
			s.groundCollision(p);
		}
	}

	@Override
	public boolean moveValidationProcessing(final GameMovable movable,
			final Vector<MoveBlocker> blockers) {
		for (final MoveBlocker moveBlocker : blockers) {
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
			} catch (final Exception exception) {
				exception.printStackTrace();
				return false;
			}
		}
		return true;
	}

	@Override
	protected void moveBlockerRuleApply(final GameMovable movable, final MoveBlocker blocker)
			throws Exception {
		Method m = null;
		m = (getClass()).getMethod("moveBlockerRule", movable.getClass(),
				blocker.getClass());
		m.invoke(this, movable, blocker);
	}

	public MoveBlocker getLastBlockingBlocker() {
		return lastBlockingBlocker;
	}

	public void moveBlockerRule(final SurpriseGift s, final Ground ground)
			throws IllegalMoveException {
		s.groundCollision(ground);
	}

	public void moveBlockerRule(final SurpriseGift s, final Platform p)
			throws IllegalMoveException {
		s.setMoving(false);
		s.groundCollision(p);
	}
}
