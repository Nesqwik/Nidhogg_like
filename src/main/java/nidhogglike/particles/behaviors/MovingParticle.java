package nidhogglike.particles.behaviors;

import nidhogglike.particles.Particle;

public class MovingParticle extends ParticleBehavior {
	protected int speed;
	protected double minAngle, maxAngle;
	
	public MovingParticle(ParticleBehavior behavior, int speed, double minAngle, double maxAngle) {
		super(behavior);
		this.speed = speed;
		this.minAngle = minAngle;
		this.maxAngle = maxAngle;
	}
	/**
	 * @see nidhogglike.particles.behaviors.ParticleBehavior#update(nidhogglike.particles.Particle)
	 */
	@Override
	public void update(Particle particle) {
		super.update(particle);
		// Get the angle corresponding to the randomness of the particle
		double angle = minAngle + (particle.getRandomness() * (maxAngle - minAngle));
		
		particle.setX(particle.getX() + Math.cos(angle) * speed);
		particle.setY(particle.getY() + Math.sin(angle) * speed);
	}
}
