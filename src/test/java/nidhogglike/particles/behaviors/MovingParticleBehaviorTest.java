package nidhogglike.particles.behaviors;

import java.awt.Color;
import java.awt.Rectangle;

import nidhogglike.particles.Particle;

import org.junit.Test;

import static org.junit.Assert.*;

public class MovingParticleBehaviorTest extends ParticleBehaviorTest {

	@Override
	public ParticleBehavior createBehavior(ParticleBehavior parentBehavior) {
		return new MovingParticleBehavior(parentBehavior, 1, 0, 0);
	}
	
	@Test
	public void particleGoesRightWhenAngleIsZero() {
		ParticleBehavior particleBehavior = new MovingParticleBehavior(new DefaultParticleBehavior(), 1, 0, 0);
		double previousX = particle.getX();
		particleBehavior.update(particle);
		assertTrue(previousX < particle.getX());
	}
	
	@Test
	public void particleGoesLeftWhenAngleIsPI() {
		ParticleBehavior particleBehavior = new MovingParticleBehavior(new DefaultParticleBehavior(), 1, Math.PI, Math.PI);
		double previousX = particle.getX();
		particleBehavior.update(particle);
		assertTrue(previousX > particle.getX());
	}
	
	@Test
	public void greaterSpeedMakesTheParticleGoesFurther() {
		ParticleBehavior slowBehavior = new MovingParticleBehavior(new DefaultParticleBehavior(), 1, 0, 0);
		ParticleBehavior fastBehavior = new MovingParticleBehavior(new DefaultParticleBehavior(), 42, 0, 0);
		
		Particle slowParticle = new Particle(0, Color.BLACK, new Rectangle(), slowBehavior);
		Particle fastParticle = new Particle(0, Color.BLACK, new Rectangle(), fastBehavior);
		
		slowBehavior.update(slowParticle);
		fastBehavior.update(fastParticle);
		
		assertTrue(slowParticle.getX() < fastParticle.getX());
	}
}
