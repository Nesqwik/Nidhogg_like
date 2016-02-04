package nidhogglike.particles;

import java.awt.Color;
import java.awt.Rectangle;

import nidhogglike.particles.behaviors.ParticleBehavior;

/**
 * Stores every useful information about a single particle
 * like its position on the screen, its color...
 */
public class Particle {
	protected double x;
	protected double y;
	protected double velocityY;
	protected int timeAlive;
	protected Color color;
	protected double randomness;
	protected int id;
	protected int width;
	protected int height;
	protected ParticleBehavior behavior;
	
	/**
	 * Constructor
	 * @param id Id of the particle
	 * @param color Color of the particle
	 * @param x Initial x position of the particle on the screen
	 * @param y Initial y position of the particle on the screen
	 * @param width Initial width of the particle on the screen
	 * @param height Initial height of the particle on the screen
	 * @param behavior Behavior used to update the particle
	 */
	public Particle(int id, Color color, int x, int y, int width, int height, ParticleBehavior behavior) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.behavior = behavior;
		this.randomness = Math.random();
		this.color = color;
		this.id = id;
	}
	
	/**
	 * Constructor
	 * @param id Id of the particle
	 * @param color Color of the particle
	 * @param rectangle Rectangle defining the initial size and position of the particle
	 * @param behavior Behavior used to update the particle
	 */
	public Particle(int id, Color color, Rectangle rectangle, ParticleBehavior behavior) {
		this(id, color, rectangle.x, rectangle.y, rectangle.width, rectangle.height, behavior);
	}
	
	/**
	 * @return the x position on the screen of the particle
	 */
	public double getX() {
		return x;
	}
	/**
	 * @param x the x to set
	 */
	public void setX(double x) {
		this.x = x;
	}
	/**
	 * @return the y position on the screen of the particle
	 */
	public double getY() {
		return y;
	}
	/**
	 * @param y the y to set
	 */
	public void setY(double y) {
		this.y = y;
	}
	/**
	 * @return the timeAlive of the particle
	 */
	public int getTimeAlive() {
		return timeAlive;
	}
	/**
	 * @param timeAlive the timeAlive to set
	 */
	public void setTimeAlive(int timeAlive) {
		this.timeAlive = timeAlive;
	}
	/**
	 * @return the color of the particle
	 */
	public Color getColor() {
		return color;
	}
	/**
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	
	/**
	 * @return the behavior
	 */
	public ParticleBehavior getBehavior() {
		return behavior;
	}
	
	/**
	 * @return the randomness
	 */
	public double getRandomness() {
		return randomness;
	}

	/**
	 * @return the id of the particle
	 */ 
	public int getId() {
		return id;
	}

	/**
	 * @return the velocityY of the particle
	 */
	public double getVelocityY() {
		return velocityY;
	}

	/**
	 * @param velocityY the velocityY to set
	 */
	public void setVelocityY(double velocityY) {
		this.velocityY = velocityY;
	}

	/**
	 * @return the width of the particle
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the height of the particle
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}
}
