package net.ld.unstable.data.projectiles.patterns;

import net.ld.unstable.data.projectiles.Projectile;
import net.lintford.library.core.LintfordCore;

public class EnemyBulletStraightShooter extends ProjectilePattern {

	public static final String NAME = "STRAIGHT";

	public EnemyBulletStraightShooter() {
		super(NAME);
	}

	@Override
	protected void update(LintfordCore pCore, Projectile pProjectile) {
		float lDelta = (float) pCore.appTime().elapsedTimeMilli() / 1000f;
		pProjectile.dx = 40.0f + (15.0f * pProjectile.timeSinceStart * lDelta) * pProjectile.odx;
		pProjectile.dy = 0.f;

		pProjectile.worldPositionX += pProjectile.dx * lDelta;

		pProjectile.worldPositionX = pProjectile.baseWorldPositionX;
		pProjectile.worldPositionY = pProjectile.baseWorldPositionY;

		super.update(pCore, pProjectile);
	}
}
