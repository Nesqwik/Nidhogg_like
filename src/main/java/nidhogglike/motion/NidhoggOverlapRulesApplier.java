package nidhogglike.motion;

import nidhogglike.entities.Player;
import nidhogglike.entities.Sword;
import gameframework.motion.overlapping.OverlapRulesApplierDefaultImpl;

public class NidhoggOverlapRulesApplier extends OverlapRulesApplierDefaultImpl {

	public void overlapRule(Sword sword, Player player) {
		System.out.println("toto");
		// This is not an error, we intend to verify if the holder of the sword if the same instance that the player
		if (sword.getHolder() != player) {
			if(sword.isMoving()) {
				player.die();
			} else if(!player.isHoldingSword()) {
				player.setSword(sword);
			}
		}
	}
}
