package nidhogglike.particles;

import java.awt.Color;

import nidhogglike.particles.behaviors.ParticleBehavior;

public class Particle {
	protected double x;
	protected double y;
	protected double velocity_y;
	protected int timeAlive;
	protected Color color;
	protected double randomness;
	protected int id;
	protected ParticleBehavior behavior;
	
	public Particle(Color color, int id, int x, int y, ParticleBehavior behavior) {
		this.x = x;
		this.y = y;
		this.behavior = behavior;
		this.randomness = Math.random();
		this.color = color;
		this.id = id;
	}
	
	/**
	 * @return the x
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
	 * @return the y
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
	 * @return the timeAlive
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
	 * @return the color
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
	 * @param behavior the behavior to set
	 */
	public void setBehavior(ParticleBehavior behavior) {
		this.behavior = behavior;
	}

	/**
	 * @return the randomness
	 */
	public double getRandomness() {
		return randomness;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the velocity_y
	 */
	public double getVelocity_y() {
		return velocity_y;
	}

	/**
	 * @param velocity_y the velocity_y to set
	 */
	public void setVelocity_y(double velocity_y) {
		this.velocity_y = velocity_y;
	}
	
	
}
