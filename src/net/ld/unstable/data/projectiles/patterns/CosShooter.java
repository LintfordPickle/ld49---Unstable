package net.ld.unstable.data.projectiles.patterns;

import net.ld.unstable.data.projectiles.Projectile;
import net.lintford.library.core.LintfordCore;

public class CosShooter extends ProjectilePattern {

	public static final String NAME = "COS";

	public CosShooter() {
		super(NAME);
	}

	@Override
	protected void update(LintfordCore pCore, Projectile pProjectile) {
		super.update(pCore, pProjectile);
		float lDelta = (float) pCore.appTime().elapsedTimeMilli() / 1000f;

		pProjectile.dx = 20.0f + (30.0f * pProjectile.timeSinceStart * lDelta);
		pProjectile.dy = 0;

		pProjectile.worldPositionX += pProjectile.dx * lDelta;

		final float lTimeMod = 0.005f;
		final float lMagnitude = 115.f;

		pProjectile.worldPositionX = pProjectile.baseWorldPositionX;
		pProjectile.worldPositionY = pProjectile.baseWorldPositionY + ((float) Math.cos(pProjectile.timeSinceStart * lTimeMod)) * lMagnitude;

		pProjectile.rotationInRadians += Math.toRadians(pProjectile.dr * lDelta);
	}
}
