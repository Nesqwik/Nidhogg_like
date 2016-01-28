package nidhogglike.motion;

import gameframework.motion.overlapping.OverlapRulesApplierDefaultImpl;
import nidhogglike.entities.HeadBalloon;
import nidhogglike.entities.Player;
import nidhogglike.entities.Sword;
import nidhogglike.game.NidhoggAnnouncer;

public class NidhoggOverlapRulesApplier extends OverlapRulesApplierDefaultImpl {

	protected NidhoggAnnouncer announcer;

	public NidhoggOverlapRulesApplier(NidhoggAnnouncer announcer) {
		this.announcer = announcer;
	}

	public void overlapRule(final Sword sword, final Player player) {
		if (sword.getHolder() == player) {
			return;
		}
		boolean mustKill = false;

		if (sword.isMoving()) {
			mustKill = true;
			if (sword.getLastHolder() == player)
				announcer.registerSuicide(player);
			else
				announcer.registerKill(sword.getLastHolder());
		} else if (sword.getHolder() != null) {
			mustKill = player.isKilledBy(sword.getHolder());
			announcer.registerKill(sword.getHolder());
		} else if (!player.isHoldingSword()) {
			player.setSword(sword);
		}

		if (mustKill) {
			announcer.registerDeath(player);
			player.die();
		}
	}

	public void overlapRule(final Sword s1, final Sword s2) {
		if (s1.isHeld() && s2.isHeld()) {
			if (!s1.getHolder().isJumping()) {
				s1.getHolder().pushBackwards();
			}
			if (!s2.getHolder().isJumping()) {
				s2.getHolder().pushBackwards();
			}
		}
	}

	public void overlapRule(final HeadBalloon baloon, final Player player) {
		baloon.isShootedBy(player);
	}
}
