package nidhogglike.particles.behaviors;

import nidhogglike.particles.Particle;

/**
 * This behavior will adds a delay between every particle
 * So, if you emit 50 particles with this behavior, the first particle will be shown instantly, the second after
 * 1*delay, the third after 2*delay, and so on until the fiftieth particle (shown after 49*delay)
 */
public class DelayedParticleBehavior extends ParticleBehavior {
	protected int delay;
	
	/**
	 * Constructor
	 * @param behavior Particle's behavior
	 * @param delay Delay added between the particles
	 */
	public DelayedParticleBehavior(ParticleBehavior behavior, int delay) {
		super(behavior);
		this.delay = delay;
	}

	/**
	 * This will be true when the delay for the particle p has expired
	 * @see nidhogglike.particles.behaviors.ParticleBehavior#isDrawn(nidhogglike.particles.Particle)
	 */
	@Override
	public boolean isDrawn(Particle p) {
		return p.getTimeAlive() >= p.getId() * delay;
	}
}
