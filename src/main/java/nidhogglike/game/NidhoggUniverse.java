package nidhogglike.game;

import nidhogglike.motion.NidhoggBlockerRulesApplier;
import nidhogglike.motion.NidhoggMovable;
import gameframework.game.GameEntity;
import gameframework.game.GameUniverseDefaultImpl;
import gameframework.motion.GameMovable;
import gameframework.motion.SpeedVector;

public class NidhoggUniverse extends GameUniverseDefaultImpl {
	
	@Override
	public void allOneStepMoves() {
		for (GameEntity entity : gameEntities) {
			if (entity.isMovable()) {
				GameMovable movable = (GameMovable) entity;
				SpeedVector vector = movable.getSpeedVector();
				if (getMoveBlockerChecker().moveValidation(movable, vector)) {
					movable.oneStepMove();
				} else {
					NidhoggMovable nMovable = (NidhoggMovable) movable;
					nMovable.onMoveFailure(((NidhoggBlockerRulesApplier) data.getMoveBlockerRulesApplier()).getLastBlockingBlocker());
				}
			}
		}
	}

}
