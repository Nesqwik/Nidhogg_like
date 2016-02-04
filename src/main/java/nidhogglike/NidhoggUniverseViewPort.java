package nidhogglike;

import java.awt.Color;
import java.awt.Rectangle;
import java.net.URL;

import nidhogglike.particles.ParticleEmitter;
import nidhogglike.particles.behaviors.DefaultParticleBehavior;
import nidhogglike.particles.behaviors.DyingParticleBehavior;
import nidhogglike.particles.behaviors.GravityParticleBehavior;
import nidhogglike.particles.behaviors.ParticleBehavior;
import gameframework.drawing.GameUniverseViewPortDefaultImpl;

/**
 * @author Team 2
 *
 * Nidhogg's universe view port which defines the location of the background image
 */
public class NidhoggUniverseViewPort extends GameUniverseViewPortDefaultImpl {
	protected ParticleEmitter emitter;
	protected ParticleBehavior rainBehavior;
	
	/**
	 * Constructor
	 * @param emitter Emitter of particles
	 */
	public NidhoggUniverseViewPort(ParticleEmitter emitter) {
		this.emitter = emitter;
		this.rainBehavior = new DefaultParticleBehavior();
		this.rainBehavior = new GravityParticleBehavior(rainBehavior, 250f / 1000, 250f / 1000);
		this.rainBehavior = new DyingParticleBehavior(rainBehavior, 80, false);
	}

	/**
	 * @see gameframework.drawing.GameUniverseViewPortDefaultImpl#backgroundImage()
	 */
	@Override
	protected URL backgroundImage() {
		return backgroundImage("/images/background.png");
	}

	/**
	 * @see gameframework.drawing.GameUniverseViewPortDefaultImpl#paint()
	 */
	@Override
	public void paint() {
		// Raining effect
		Rectangle rectangle = new Rectangle((int) (Math.random() * Nidhogg.WIDTH), -10, 2, 10);
		emitter.emit(Color.WHITE, rectangle, 1, rainBehavior);
		
		super.paint();
	}
}
