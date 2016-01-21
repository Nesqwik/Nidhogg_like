package nidhogglike.input;

import gameframework.drawing.GameCanvas;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Input  extends KeyAdapter {
	public boolean[] keys;
	
	public Input(GameCanvas canvas) {
		canvas.addKeyListener(this);
		keys = new boolean[256];
	}

	/**
	 * @see java.awt.event.KeyAdapter#keyPressed(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent keyEvent) {
		super.keyPressed(keyEvent);
		keys[keyEvent.getKeyCode()] = true;
	}

	/**
	 * @see java.awt.event.KeyAdapter#keyReleased(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyReleased(KeyEvent keyEvent) {
		super.keyReleased(keyEvent);
		keys[keyEvent.getKeyCode()] = false;
	}
	
	public boolean isPressed(int keyCode) {
		return keys[keyCode];
	}
}
