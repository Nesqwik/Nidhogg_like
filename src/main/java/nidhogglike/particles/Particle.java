package nidhogglike.particles;

import java.awt.Color;

import nidhogglike.particles.behaviors.ParticleBehavior;

/**
 * Stores every useful information about a single particle
 * like it's position on the screen, it's color...
 */
public class Particle {
	protected double x;
	protected double y;
	protected double velocityY;
	protected int timeAlive;
	protected Color color;
	protected double randomness;
	protected int id;
	protected ParticleBehavior behavior;
	
	/**
	 * Constructor
	 * @param color Color of the particle
	 * @param id Id of the particle
	 * @param x Initial x position of the particle on the screen
	 * @param y Initial y position of the particle on the screen
	 * @param behavior Behavior used to update the particle
	 */
	public Particle(Color color, int id, int x, int y, ParticleBehavior behavior) {
		this.x = x;
		this.y = y;
		this.behavior = behavior;
		this.randomness = Math.random();
		this.color = color;
		this.id = id;
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
}
