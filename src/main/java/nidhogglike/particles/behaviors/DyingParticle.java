package nidhogglike.particles.behaviors;

import java.awt.Color;

import nidhogglike.particles.Particle;

public class DyingParticle extends ParticleBehavior {
	protected int timeToLive;
	protected boolean fadeOpacity;
	
	public DyingParticle(ParticleBehavior behavior, int timeToLive, boolean fadeOpacity) {
		super(behavior);
		this.timeToLive = timeToLive;
		this.fadeOpacity = fadeOpacity;
	}

	/**
	 * @see nidhogglike.particles.behaviors.ParticleBehavior#isDead(nidhogglike.particles.Particle)
	 */
	@Override
	public boolean isDead(Particle particle) {
		return particle.getTimeAlive() >= timeToLive;
	}

	/**
	 * @see nidhogglike.particles.behaviors.ParticleBehavior#update(nidhogglike.particles.Particle)
	 */
	@Override
	public void update(Particle particle) {
		super.update(particle);
		if (fadeOpacity) {
			int opacity = 255 - (int) ((particle.getTimeAlive() + 0f) / timeToLive * 255);
			Color previousColor = particle.getColor();
			Color color = new Color(previousColor.getRed(), previousColor.getGreen(), previousColor.getBlue(), opacity);
			particle.setColor(color);
		}
	}
	
}
