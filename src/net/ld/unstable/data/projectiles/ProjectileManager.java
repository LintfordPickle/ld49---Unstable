package net.ld.unstable.data.projectiles;

import java.util.ArrayList;
import java.util.List;

import net.lintford.library.core.LintfordCore;

public class ProjectileManager {

	// --------------------------------------
	// Variables
	// --------------------------------------

	private List<Projectile> mProjectiles;
	private int mCapacity;

	// --------------------------------------
	// Properties
	// --------------------------------------

	public List<Projectile> projectiles() {
		return mProjectiles;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public ProjectileManager(int pCap) {
		super();

		mCapacity = pCap;

		mProjectiles = new ArrayList<>();
		for (int i = 0; i < mCapacity; i++) {
			mProjectiles.add(new Projectile());
		}
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	public void update(LintfordCore pCore) {
		for (int i = 0; i < mCapacity; i++) {
			final var p = mProjectiles.get(i);

			if (!p.isAssigned())
				continue;

			p.timeSinceStart += pCore.appTime().elapsedTimeMilli();
			if (p.timeSinceStart >= p.lifeTime()) {
				p.reset();

			}
		}
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	/** Spawns a new {@link Particle} and applys the {@link IParticleinitializer} attached to this {@link ParticleSystemInstance}. */
	public Projectile spawnParticle(float pX, float pY, float pLife) {
		for (int i = 0; i < mCapacity; i++) {
			Projectile lSpawnedParticle = mProjectiles.get(i);
			if (lSpawnedParticle.isAssigned())
				continue;

			lSpawnedParticle.spawnParticle(pX, pY, pLife);

			return lSpawnedParticle;
		}

		return null;
	}

	public void reset() {
		for (int i = 0; i < mCapacity; i++) {
			Projectile p = mProjectiles.get(i);
			p.reset();
		}
	}
}
