package nidhogglike.motion;

import gameframework.motion.GameMovable;
import gameframework.motion.GameMovableDriver;
import gameframework.motion.blocking.MoveBlocker;

public abstract class NidhoggMovable extends GameMovable {
	
	public NidhoggMovable(){
		super();
	}

	public NidhoggMovable(GameMovableDriver driver){
		super(driver);
	}
	/**
	 * This method is called when this movable collides with a move blocker.
	 * @param lastBlockingBlocker the blocker the player collided with
	 */
	public void onMoveFailure(MoveBlocker lastBlockingBlocker) {	}
}
