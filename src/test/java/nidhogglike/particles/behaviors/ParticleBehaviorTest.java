package nidhogglike.particles.behaviors;

import java.awt.Color;
import java.awt.Rectangle;

import nidhogglike.particles.Particle;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public abstract class ParticleBehaviorTest {
	protected ParticleBehaviorMock behaviorMock;
	protected ParticleBehavior behavior;
	protected Particle particle;
	
	public abstract ParticleBehavior createBehavior(ParticleBehavior parentBehavior);
	
	@Before
	public void initializeBehaviors() {
		behaviorMock = new ParticleBehaviorMock(new DefaultParticleBehavior());
		behavior = createBehavior(behaviorMock);
		particle = new Particle(0, Color.BLACK, new Rectangle(), behavior);
	}
	
	public boolean isDeadIntercepted() {
		return false;
	}
	
	public boolean isDrawnIntercepted() {
		return false;
	}
	
	@Test
	public void isDeadFromThePreviousBehaviorIsCalledIfNotIntercepted() {
		assertEquals(0, behaviorMock.getNbIsDead());
		behavior.isDead(particle);
		assertEquals(isDeadIntercepted() ? 0 : 1, behaviorMock.getNbIsDead());
	}
	
	@Test
	public void isDrawnFromThePreviousBehaviorIsCalledIfNotIntercepted() {
		assertEquals(0, behaviorMock.getNbIsDrawn());
		behavior.isDrawn(particle);
		assertEquals(isDrawnIntercepted() ? 0 : 1, behaviorMock.getNbIsDrawn());
	}
}
