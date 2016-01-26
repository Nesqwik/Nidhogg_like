package nidhogglike.particles.behaviors;

import nidhogglike.particles.Particle;

public abstract class ParticleBehavior {
	protected ParticleBehavior behavior;
	
	public ParticleBehavior(ParticleBehavior behavior) {
		this.behavior = behavior;
	}
	
	public boolean isDead(Particle particle) {
		if (behavior == null) {
			return false;
		}
		return behavior.isDead(particle);
	}
	public void update(Particle particle) {
		if (behavior != null) {
			behavior.update(particle);
		}
	}

	public boolean isDrawn(Particle p) {
		if (behavior == null) {
			return true;
		}
		return behavior.isDrawn(p);
	}
	
	public void reset() {
		if (behavior != null) {
			behavior.reset();
		}
	}
}
