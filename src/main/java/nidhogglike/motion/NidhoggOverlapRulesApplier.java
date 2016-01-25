package nidhogglike.motion;

import nidhogglike.entities.Player;
import nidhogglike.entities.Sword;
import gameframework.motion.overlapping.OverlapRulesApplierDefaultImpl;

public class NidhoggOverlapRulesApplier extends OverlapRulesApplierDefaultImpl {

	public void overlapRule(Sword sword, Player player) {
		// This is not an error, we intend to verify if the holder of the sword if the same instance that the player
		if (sword.getHolder() != player) {
			// TODO : gérer le fait qu'on ramasse l'épée si on n'en a pas et qu'elle est au sol
			player.die();
		}
	}
}
