package net.ld.unstable.data.projectiles.modifiers;

import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.particles.Particle;
import net.lintford.library.core.particles.particlesystems.modifiers.ParticleModifierBase;

public class BarrelPhysicsModifier extends ParticleModifierBase {

	// --------------------------------------
	// Constants
	// --------------------------------------

	private static final long serialVersionUID = 3823714289519272208L;

	public static final String MODIFIER_NAME = "BarrelModifier";

	/** The factor of the particle vertical velocity to conserve after collisions with the floor */
	public static final float PARTICLE_FLOOR_BOUNCE_AMT = 0.5f;

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public BarrelPhysicsModifier() {
		super(MODIFIER_NAME);

	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void initialize(Particle pParticle) {

	}

	@Override
	public void update(LintfordCore pCore) {

	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	@Override
	public void updateParticle(LintfordCore pCore, Particle pParticle) {
		float lDelta = (float) pCore.appTime().elapsedTimeMilli() / 1000f;

		pParticle.dx *= 0.99f;
		
		pParticle.worldPositionX += pParticle.dx * lDelta;
		pParticle.worldPositionY += pParticle.dy * lDelta;
		pParticle.rotationInRadians += Math.toRadians(pParticle.dr * lDelta);

	}

}
