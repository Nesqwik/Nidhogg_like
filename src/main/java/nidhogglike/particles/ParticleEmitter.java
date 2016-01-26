package nidhogglike.particles;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;

import nidhogglike.particles.behaviors.ParticleBehavior;
import gameframework.game.GameEntity;

public class ParticleEmitter implements GameEntity {
	protected List<Particle> particles;
	protected List<Particle> deadParticles;
	public ParticleEmitter() {
		particles = new LinkedList<>();
		deadParticles = new LinkedList<>();
	}
	
	protected void update(Particle particle) {
		particle.setTimeAlive(particle.getTimeAlive() + 1);
		
		if (particle.getBehavior().isDead(particle)) {
			deadParticles.add(particle);
		}
		
		if (particle.getBehavior().isDrawn(particle)) {
			particle.getBehavior().update(particle);
		}
	}
	
	protected void removeDeadParticles() {
		for (Particle p : deadParticles) {
			particles.remove(p);
		}
	}
	
	@Override
	public void draw(Graphics g) {
		for (Particle p : particles) {
			update(p);
			if (p.getBehavior().isDrawn(p)) {
				g.setColor(p.getColor());
				g.fillRect((int) p.getX(), (int) p.getY(), 5, 5);
			}
		}
		
		removeDeadParticles();
	}
	
	public void emit(Color color, int x, int y, int nb, ParticleBehavior behavior) {
		for (; nb >= 0; --nb) {
			particles.add(new Particle(color, nb, x, y, behavior));
		}
	}

	@Override
	public boolean isMovable() {
		return false;
	}

}
