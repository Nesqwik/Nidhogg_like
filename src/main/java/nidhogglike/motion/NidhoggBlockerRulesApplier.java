package nidhogglike.motion;

import gameframework.motion.GameMovable;
import gameframework.motion.IllegalMoveException;
import gameframework.motion.blocking.MoveBlocker;
import gameframework.motion.blocking.MoveBlockerRulesApplierDefaultImpl;

import java.awt.Rectangle;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Vector;

import nidhogglike.entities.Ground;
import nidhogglike.entities.HeadBalloon;
import nidhogglike.entities.Platform;
import nidhogglike.entities.Player;
import nidhogglike.entities.Sword;
import nidhogglike.surprise.SurpriseGift;

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
//			final int delta = platform.getBoundingBox().y - feetY;
//			
//			// if the player is standing on the obstacle
//			if (Math.abs(delta) < 20) {
//				p.groundCollision(platform);
//			}
//			
			Rectangle collision = p.getBoundingBox().intersection(platform.getBoundingBox());
			//&& p.getPosition().y < (platform.getBoundingBox().y + platform.getBoundingBox().width / 3)
			if (collision.width > collision.height) {
				if (p.getVelocityY() >= 0 && feetY < (platform.getBoundingBox().y + platform.getBoundingBox().height)) {
//					if (!p.isJumping())
						p.groundCollision(platform);
				}
			} else {
				if (p.getVelocityY() <= 0 && !p.isJumping()) {
					p.refinePositionAfterLateralCollision(platform);
				} else {
					System.out.println("other");
				}
			}

//			else if (Math.abs(p.getPosition().y - platform.getBoundingBox().y) < 20) {
//				System.out.println("roof");
//				p.roofCollision(platform);
//				throw new IllegalMoveException();
//			} else {
//				System.out.println("lateral");
//				p.refinePositionAfterLateralCollision(platform);
//				throw new IllegalMoveException();
//			}
		}
	}
	public void moveBlockerRule(final HeadBalloon b, final Platform platform)
			throws IllegalMoveException {

		if (Math.abs(b.getVelocityY()) > Math.abs(b.getVelocityX())) {
			b.platformCollision(platform);
			throw new IllegalMoveException();
		} else {
			b.LateralCollision(platform);
			throw new IllegalMoveException();
		}
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
		s.setMoving(false);
		s.groundCollision(ground);
	}

	public void moveBlockerRule(final Sword s, final Platform p)
			throws IllegalMoveException {
		if (!s.isHeld()) {
			final int py = p.getBoundingBox().y + p.getBoundingBox().height;

			if (s.getPosition().y <= py) {
				s.getPosition().y = p.getBoundingBox().y - s.getBoundingBox().height;
			}

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
	public void moveBlockerRule(SurpriseGift s, Ground ground)
			throws IllegalMoveException {
		s.groundCollision(ground);
	}

	public void moveBlockerRule(SurpriseGift s, Platform p)
			throws IllegalMoveException {
		final int py = p.getBoundingBox().y + p.getBoundingBox().height;

		if (s.getPosition().y <= py) {
			s.getPosition().y = p.getBoundingBox().y - s.getBoundingBox().height;
		}

		s.groundCollision(p);
	}
}
