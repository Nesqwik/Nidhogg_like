package nidhogglike.particles;

import java.awt.Graphics;

public class ParticleEmitterMock extends ParticleEmitter {

	/**
	 * @see nidhogglike.particles.ParticleEmitter#drawParticle(nidhogglike.particles.Particle, java.awt.Graphics)
	 */
	@Override
	public void drawParticle(Particle p, Graphics g) {
		// Draw should do nothing as it's not easily testable
	}
}
