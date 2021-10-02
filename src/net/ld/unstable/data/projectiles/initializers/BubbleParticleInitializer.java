package net.ld.unstable.data.projectiles.initializers;

import net.lintford.library.core.maths.RandomNumbers;
import net.lintford.library.core.particles.Particle;
import net.lintford.library.core.particles.particlesystems.initializers.ParticleSingleValueInitializer;

public class BubbleParticleInitializer extends ParticleSingleValueInitializer {

	// --------------------------------------
	// Constants
	// --------------------------------------

	private static final long serialVersionUID = -208992042047147087L;

	public static final String INITIALIZER_NAME = "ParticleRandomRotationInitializer";

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public BubbleParticleInitializer() {
		super(INITIALIZER_NAME);

	}

	// --------------------------------------
	// Inherited-Methods
	// --------------------------------------

	@Override
	public void onIntialiseParticle(Particle pParticle, float pValue0) {
		pParticle.rotationInRadians = pValue0;
		pParticle.dr = pValue0;

		pParticle.worldPositionX += RandomNumbers.random(-20.f, 20.f);
		pParticle.worldPositionX += RandomNumbers.random(-15.f, 15.f);
	}

}
