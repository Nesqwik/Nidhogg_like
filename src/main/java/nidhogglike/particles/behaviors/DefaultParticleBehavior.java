package nidhogglike.particles.behaviors;

import nidhogglike.particles.Particle;

/**
 * Default behavior of a particle
 * This behavior should do nothing to the particle
 */
public class DefaultParticleBehavior extends ParticleBehavior {

	public DefaultParticleBehavior() {
		super(null);
	}

	/**
	 * By default, the particle cant die
	 * @see nidhogglike.particles.behaviors.ParticleBehavior#isDead(nidhogglike.particles.Particle)
	 */
	@Override
	public boolean isDead(Particle particle) {
		return false;
	}

	/**
	 * By default, nothing is done
	 * @see nidhogglike.particles.behaviors.ParticleBehavior#update(nidhogglike.particles.Particle)
	 */
	@Override
	public void update(Particle particle) {
		// Does nothing
	}

	/**
	 * By default, the particle is always drawn
	 * @see nidhogglike.particles.behaviors.ParticleBehavior#isDrawn(nidhogglike.particles.Particle)
	 */
	@Override
	public boolean isDrawn(Particle p) {
		return true;
	}
}
