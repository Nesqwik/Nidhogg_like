package nidhogglike.motion;

import gameframework.motion.overlapping.OverlapRulesApplierDefaultImpl;
import nidhogglike.entities.HeadBalloon;
import nidhogglike.entities.Player;
import nidhogglike.entities.SurpriseGift;
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
		boolean mustHit = false;

		if (sword.isMoving()) {
			mustHit = true;
		} else if (sword.getHolder() != null) {
			mustHit = player.isKilledBy(sword.getHolder());
		} else if (!player.isHoldingSword()) {
			player.setSword(sword);
		}
		
		if (player.stillInvincible()) {
			mustHit = false;
		}
		
		boolean mustKill = false;

		if ((sword.getHolder() != null) && (sword.getHolder().getStrongerSword() > 0) && (player != sword.getHolder())) {
			mustKill = true;
			sword.getHolder().removeStrongerSword();
			player.setCurrentLife(3);
		} else if (mustHit) {
			if (!sword.isHeld()) {
				mustKill = true;
			} else {
				mustKill = player.hit();
			}
		}
		
		if (mustKill) {
			player.emitParticle();
			player.die();
			announcer.handleKill(player, sword);
		} else {
			player.pushInDirection(sword.getPosition().x > player.getPosition().x);
		}
	}

	public void overlapRule(final Sword s1, final Sword s2) {
		if (s1.isHeld() && s2.isHeld()) {
			s2.getHolder().pushBackwards();
			s1.getHolder().pushBackwards();
		} else if (s1.isHeld() && s2.isMoving()) {
			s2.setVelocity_x(-2);
		} else if (s2.isHeld() && s1.isMoving()) {
			s1.setVelocity_x(-2);
		}
	}

	public void overlapRule(SurpriseGift s, Player player) {
		s.takingGift(player);
	}

	public void overlapRule(final HeadBalloon baloon, final Player player) {
		baloon.isShotBy(player);
	}

}
