package nidhogglike.particles.behaviors;

import nidhogglike.particles.Particle;

public class DelayedParticle extends ParticleBehavior {
	protected int delay;
	
	public DelayedParticle(ParticleBehavior behavior, int delay) {
		super(behavior);
		this.delay = delay;
	}

	/**
	 * @see nidhogglike.particles.behaviors.ParticleBehavior#isDrawn(nidhogglike.particles.Particle)
	 */
	@Override
	public boolean isDrawn(Particle p) {
		return p.getTimeAlive() > p.getId() * delay;
	}
}
