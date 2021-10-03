package net.ld.unstable.data.projectiles.patterns;

import net.ld.unstable.data.projectiles.Projectile;
import net.lintford.library.core.LintfordCore;

public class PlayerTorpedoStraightShooter extends ProjectilePattern {

	public static final String NAME = "STRAIGHT";

	private float mSeaHeight;

	public PlayerTorpedoStraightShooter(float pSeaHeight) {
		super(NAME);

		mSeaHeight = pSeaHeight;
	}

	@Override
	protected void update(LintfordCore pCore, Projectile pProjectile) {
		float lDelta = (float) pCore.appTime().elapsedTimeMilli() / 1000f;

		final boolean isAboveWater = pProjectile.worldPositionY > mSeaHeight;

		pProjectile.dx = isAboveWater ? 0.f : (30.0f + (60.0f * pProjectile.timeSinceStart * lDelta)) * pProjectile.odx;
		pProjectile.dy = 60.f;

		pProjectile.worldPositionX = pProjectile.baseWorldPositionX;
		pProjectile.worldPositionY = pProjectile.baseWorldPositionY;

		super.update(pCore, pProjectile);
	}
}
