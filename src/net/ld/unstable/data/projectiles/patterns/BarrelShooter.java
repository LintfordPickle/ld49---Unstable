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

		pProjectile.dx *= 0.99f;
		pProjectile.dr *= 0.99f;

		pProjectile.worldPositionX += pProjectile.dx * lDelta;
		pProjectile.worldPositionY += pProjectile.dy * lDelta;
		pProjectile.rotationInRadians += Math.toRadians(pProjectile.dr * lDelta);

		super.update(pCore, pProjectile);
	}
}
