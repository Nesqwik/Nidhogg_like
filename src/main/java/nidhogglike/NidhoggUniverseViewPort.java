package nidhogglike;

import java.net.URL;

import gameframework.drawing.GameUniverseViewPortDefaultImpl;

/**
 * @author Team 2
 *
 * Nidhogg's universe view port which defines the location of the background image
 */
public class NidhoggUniverseViewPort extends GameUniverseViewPortDefaultImpl {

	/**
	 * @see gameframework.drawing.GameUniverseViewPortDefaultImpl#backgroundImage()
	 */
	@Override
	protected URL backgroundImage() {
		return backgroundImage("/images/background.png");
	}
	
}
