package nidhogglike.entities.obstacles;

import java.awt.Graphics;

import nidhogglike.Nidhogg;

public class Ground extends Platform {

	public Ground() {
		super(0 - (Nidhogg.WIDTH / 2), Nidhogg.HEIGHT - Nidhogg.HEIGHT * 0.3f, Nidhogg.WIDTH * 2, Nidhogg.HEIGHT - Nidhogg.HEIGHT * 0.3f);
	}
	
	@Override
	public void draw(Graphics g) {	}

}
