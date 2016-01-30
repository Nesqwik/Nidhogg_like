package nidhogglike.motion;

import nidhogglike.entities.HeadBalloon;
import nidhogglike.entities.Player;
import nidhogglike.entities.Sword;
import nidhogglike.game.NidhoggAnnouncer;
import nidhogglike.surprise.SurpriseGift;
import gameframework.motion.overlapping.OverlapRulesApplierDefaultImpl;

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
			if (sword.getLastHolder() == player)
				announcer.registerSuicide(player);
			else
				announcer.registerKill(sword.getLastHolder());
		} else if (sword.getHolder() != null) {
			mustHit = player.isKilledBy(sword.getHolder());
			
			announcer.registerKill(sword.getHolder());
		} else if (!player.isHoldingSword()) {
			player.setSword(sword);
		}
		
		boolean mustKill = false;
		if (mustHit) {
			if (!sword.isHeld()) {
				player.emitParticle();
				player.die();
				mustKill = true;
			} else {
				mustKill = player.hit();
			}
		}

		if (mustKill) {
			announcer.registerDeath(player);
		}
	}

	public void overlapRule(final Sword s1, final Sword s2) {
		if (s1.isHeld() && s2.isHeld()) {
			s2.getHolder().pushBackwards();
			s1.getHolder().pushBackwards();
		} else if(s1.isHeld() && s2.isMoving()) {
			s2.setVelocity_x(-2);
		} else if(s2.isHeld() && s1.isMoving()) {
			s1.setVelocity_x(-2);
		}
	}
	
	public void overlapRule(SurpriseGift s, Player player) {
		if (s.getHolder() != player) {
			player.isTakingGift(s);
		}
	}
	
	public void overlapRule(final HeadBalloon baloon, final Player player) {
		baloon.isShotBy(player);
	}
	
	
}
