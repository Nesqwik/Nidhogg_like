package nidhogglike.entities;

import java.awt.Graphics;

import nidhogglike.Nidhogg;

public class Ground extends Platform {

	protected final static int HEIGHT = 138;
	public Ground() {
		super(0, Nidhogg.HEIGHT - HEIGHT, Nidhogg.WIDTH, HEIGHT);
	}
	
	@Override
	public void draw(Graphics g) {	}

}
