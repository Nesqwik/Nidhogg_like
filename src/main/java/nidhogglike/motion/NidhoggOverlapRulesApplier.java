package nidhogglike.motion;

import gameframework.motion.overlapping.OverlapRulesApplierDefaultImpl;
import nidhogglike.entities.Player;
import nidhogglike.entities.Sword;

public class NidhoggOverlapRulesApplier extends OverlapRulesApplierDefaultImpl {

	public void overlapRule(final Sword sword, final Player player) {
		if (sword.getHolder() == player) {
			return;
		}
		boolean mustKill = false;

		if(sword.isMoving()) {
			mustKill = true;
		} else if (sword.getHolder() != null) {
			mustKill = player.isKilledBy(sword.getHolder());
		} else if (!player.isHoldingSword()) {
			player.setSword(sword);
		}

		if (mustKill) {
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
}
