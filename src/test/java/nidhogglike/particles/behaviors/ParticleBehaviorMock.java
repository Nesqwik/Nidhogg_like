package nidhogglike.particles.behaviors;

import nidhogglike.particles.Particle;

public class ParticleBehaviorMock extends ParticleBehavior {
	protected int nbUpdate;

	public ParticleBehaviorMock(ParticleBehavior behavior) {
		super(behavior);
		nbUpdate = 0;
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
	 * @return the nbUpdate
	 */
	public int getNbUpdate() {
		return nbUpdate;
	}

	/**
	 * @param nbUpdate the nbUpdate to set
	 */
	public void setNbUpdate(int nbUpdate) {
		this.nbUpdate = nbUpdate;
	}
}
