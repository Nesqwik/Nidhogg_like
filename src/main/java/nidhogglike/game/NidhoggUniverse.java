package nidhogglike.game;

import nidhogglike.entities.Sword;
import nidhogglike.motion.NidhoggBlockerRulesApplier;
import nidhogglike.motion.NidhoggMovable;

import java.util.ArrayList;
import java.util.List;



import gameframework.game.GameData;
import gameframework.game.GameEntity;
import gameframework.game.GameUniverseDefaultImpl;
import gameframework.motion.GameMovable;
import gameframework.motion.SpeedVector;

public class NidhoggUniverse extends GameUniverseDefaultImpl {
	
	public NidhoggUniverse(GameData gameData) {
		super(gameData);
	}

	protected List<Sword> swords = new ArrayList<>();
	
	@Override
	public synchronized void addGameEntity(GameEntity gameEntity) {
		if (gameEntity instanceof Sword) {
			swords.add((Sword) gameEntity);
		}
		
		super.addGameEntity(gameEntity);
	}
	
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
	
	public Sword getFreeSword() {
		for (Sword s : swords) {
			if (!s.isHeld()) {
				return s;
			}
		}
		return null;
	}

}
