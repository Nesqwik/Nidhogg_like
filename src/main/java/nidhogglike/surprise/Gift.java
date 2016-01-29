package nidhogglike.surprise;


import java.util.List;

import gameframework.drawing.SpriteManager;
import nidhogglike.entities.Player;

public class Gift {

	protected int pointsToAdd;
	protected String urlImage;
	protected int x;
	private boolean isOpen;
	
	public Gift(int x) {
		urlImage = "/images/sword.png";
		this.pointsToAdd = 10;
		this.x=x;
		isOpen = false;
	}
	
	public String getUrlImage() {
		return this.urlImage;
	}
	
	public int getPositionX() {
		return x;
	}
	
	public void openGift(Player p) {
		p.increaseScore(pointsToAdd);
	}

	public boolean isOpened() {
		return isOpen;
	}

	public void setTypeSprite(SpriteManager sprite) {
		sprite.setType("good");
	}

	public void removeIsOpened(List<Gift> gifts) {
		if (this.isOpened()) {
			gifts.remove(this);
		}
	}

	
}
