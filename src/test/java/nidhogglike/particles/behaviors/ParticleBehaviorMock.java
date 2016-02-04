package nidhogglike.particles.behaviors;

import nidhogglike.particles.Particle;

public class ParticleBehaviorMock extends ParticleBehavior {
	protected int nbUpdate;
	protected int nbIsDead;
	protected int nbIsDrawn;

	public ParticleBehaviorMock(ParticleBehavior behavior) {
		super(behavior);
		nbUpdate = 0;
		nbIsDead = 0;
		nbIsDrawn = 0;
	}

	/**
	 * @see nidhogglike.particles.behaviors.ParticleBehavior#update(nidhogglike.particles.Particle)
	 */
	@Override
	public void update(Particle particle) {
		super.update(particle);
		++nbUpdate;
	}
	
	

	/**
	 * @see nidhogglike.particles.behaviors.ParticleBehavior#isDead(nidhogglike.particles.Particle)
	 */
	@Override
	public boolean isDead(Particle particle) {
		++nbIsDead;
		return super.isDead(particle);
	}

	/**
	 * @see nidhogglike.particles.behaviors.ParticleBehavior#isDrawn(nidhogglike.particles.Particle)
	 */
	@Override
	public boolean isDrawn(Particle particle) {
		++nbIsDrawn;
		return super.isDrawn(particle);
	}

	/**
	 * @return Number of times "update" was called 
	 */
	public int getNbUpdate() {
		return nbUpdate;
	}

	/**
	 * @return Number of times "isDead" was called
	 */
	public int getNbIsDead() {
		return nbIsDead;
	}

	/**
	 * @return Number of times "isDrawn" was called
	 */
	public int getNbIsDrawn() {
		return nbIsDrawn;
	}
}
