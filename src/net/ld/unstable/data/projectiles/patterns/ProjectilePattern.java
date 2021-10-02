package net.ld.unstable.data.projectiles.patterns;

import java.util.ArrayList;
import java.util.List;

import net.ld.unstable.data.projectiles.Projectile;
import net.lintford.library.core.LintfordCore;

public abstract class ProjectilePattern {

	// --------------------------------------
	// Variables
	// --------------------------------------

	private final String mPatternName;
	private final List<Projectile> projectilesToUpdate = new ArrayList<>();
	public final List<Projectile> projectiles = new ArrayList<>();

	// --------------------------------------
	// Properties
	// --------------------------------------

	public String patternName() {
		return mPatternName;
	}

	protected boolean isProjectileDead(Projectile pProjectile) {
		return pProjectile.timeSinceStart >= pProjectile.lifeTime();
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public ProjectilePattern(String pPatternName) {
		mPatternName = pPatternName;
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	public void update(LintfordCore pCore) {
		final int lNumberOfParticles = projectiles.size();

		projectilesToUpdate.clear();

		for (int i = 0; i < lNumberOfParticles; i++) {
			projectilesToUpdate.add(projectiles.get(i));
		}

		for (int i = 0; i < lNumberOfParticles; i++) {
			final var lParticle = projectilesToUpdate.get(i);
			if (isProjectileDead(lParticle))
				projectiles.remove(lParticle);

			update(pCore, lParticle);
		}
	}

	protected void update(LintfordCore pCore, Projectile pProjectile) {
		float lDelta = (float) pCore.appTime().elapsedTimeMilli() / 1000f;

		pProjectile.baseWorldPositionX += pProjectile.dx * lDelta;
		pProjectile.baseWorldPositionY += pProjectile.dy * lDelta;
		pProjectile.rotationInRadians += Math.toRadians(pProjectile.dr * lDelta);
	}

	public void addProjectile(Projectile pProjectile) {
		projectiles.add(pProjectile);
	}

}
