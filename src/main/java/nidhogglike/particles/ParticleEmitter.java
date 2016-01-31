package nidhogglike.particles;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;

import nidhogglike.particles.behaviors.ParticleBehavior;
import gameframework.game.GameEntity;

/**
 * A game entity that can be added to a game universe
 * This entity can emit, draw and update an infinite number of particles
 */
public class ParticleEmitter implements GameEntity {
	/* List containing every "alive" particles that will be updated */
	protected List<Particle> particles;
	/* List containing every particles that should be removed */
	protected List<Particle> deadParticles;
	
	/**
	 * Constructor
	 */
	public ParticleEmitter() {
		particles = new LinkedList<>();
		deadParticles = new LinkedList<>();
	}
	
	/**
	 * Update a particle according to its behavior
	 * @param particle Particle to update
	 */
	protected void update(Particle particle) {
		particle.setTimeAlive(particle.getTimeAlive() + 1);
		
		if (particle.getBehavior().isDead(particle)) {
			deadParticles.add(particle);
		}
		
		if (particle.getBehavior().isDrawn(particle)) {
			particle.getBehavior().update(particle);
		}
	}
	
	/**
	 * Remove every dead particles contained in deadParticles
	 */
	protected void removeDeadParticles() {
		for (Particle p : deadParticles) {
			particles.remove(p);
		}
		deadParticles.clear();
	}
	
	/**
	 * Draw every particles that are still alive
	 * @see gameframework.game.GameEntity#draw(java.awt.Graphics)
	 */
	@Override
	public void draw(Graphics g) {
		for (Particle p : particles) {
			update(p);
			if (p.getBehavior().isDrawn(p)) {
				g.setColor(p.getColor());
				g.fillRect((int) p.getX(), (int) p.getY(), 5, 5);
			}
		}
		
		// We remove the particles after the loop to avoid concurrent modifications
		removeDeadParticles();
	}
	
	/**
	 * Emits a certain amount of particles that will react
	 * according to the specified behavior
	 * @param color Color of these particle
	 * @param x X position on the screen of these particles
	 * @param y Y position on the screen of these particles
	 * @param nb Number of particles to emit
	 * @param behavior Behavior of these newly created particles
	 */
	public void emit(Color color, int x, int y, int nb, ParticleBehavior behavior) {
		for (; nb >= 0; --nb) {
			particles.add(new Particle(color, nb, x, y, behavior));
		}
	}

	/**
	 * Returns always false as this entity is not movable and shouldn't be tested for collisions
	 * @see gameframework.game.GameEntity#isMovable()
	 */
	@Override
	public boolean isMovable() {
		return false;
	}

}
