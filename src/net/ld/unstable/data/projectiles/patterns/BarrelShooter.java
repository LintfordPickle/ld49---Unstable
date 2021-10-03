package net.ld.unstable.data.projectiles.patterns;

import net.ld.unstable.data.projectiles.Projectile;
import net.lintford.library.core.LintfordCore;

public class BarrelShooter extends ProjectilePattern {

	public static final String NAME = "BARREL";

	public BarrelShooter() {
		super(NAME);
	}

	@Override
	protected void update(LintfordCore pCore, Projectile pProjectile) {
		float lDelta = (float) pCore.appTime().elapsedTimeMilli() / 1000f;

		pProjectile.dx = pProjectile.odx * (1.f / pProjectile.timeSinceStart);
		pProjectile.dy = pProjectile.ody;

		pProjectile.worldPositionX += pProjectile.dx * lDelta;
		pProjectile.worldPositionY += pProjectile.dy * lDelta;
		pProjectile.rotationInRadians += Math.toRadians(pProjectile.dr * lDelta);

		super.update(pCore, pProjectile);
	}
}
