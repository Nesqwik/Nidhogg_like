package nidhogglike.motion;

import nidhogglike.entities.Player;
import nidhogglike.entities.Sword;
import nidhogglike.surprise.SurpriseGift;
import gameframework.motion.overlapping.OverlapRulesApplierDefaultImpl;

public class NidhoggOverlapRulesApplier extends OverlapRulesApplierDefaultImpl {

	public void overlapRule(Sword sword, Player player) {
		// This is not an error, we want to check that the sword is colliding with another player
		if (sword.getHolder() != player) {
			if(sword.isMoving() || sword.getHolder() != null) {
				player.die();
				
			} else if(!player.isHoldingSword()) {
				player.setSword(sword);
			}
		}
	}
	
	public void overlapRule(SurpriseGift s, Player player) {
		if (s.getHolder() != player) {
			player.isTakingGift(s);
		}
	}
}
