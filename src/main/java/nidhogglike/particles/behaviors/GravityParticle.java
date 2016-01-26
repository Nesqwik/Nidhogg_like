package nidhogglike.particles.behaviors;

import nidhogglike.particles.Particle;

public class GravityParticle extends ParticleBehavior {
	protected double minGravity, maxGravity;
	protected double velocity_y;
	
	public GravityParticle(ParticleBehavior behavior, double minGravity, double maxGravity) {
		super(behavior);
		this.minGravity = minGravity / 1000;
		this.maxGravity = maxGravity / 1000;
	}
	/**
	 * @see nidhogglike.particles.behaviors.ParticleBehavior#update(nidhogglike.particles.Particle)
	 */
	@Override
	public void update(Particle particle) {
		super.update(particle);
		// Get the gravity corresponding to the randomness of the particle
		double gravity = minGravity + (particle.getRandomness() * (maxGravity - minGravity));
		particle.setVelocity_y(particle.getVelocity_y() + gravity);
		particle.setY(particle.getY() + particle.getVelocity_y());
	}
	
}
