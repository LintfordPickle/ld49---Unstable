package net.ld.unstable.data.projectiles.patterns;

import org.joml.Math;

import net.ld.unstable.data.projectiles.Projectile;
import net.lintford.library.core.LintfordCore;

public class PlayerMissileShooter extends ProjectilePattern {

	public static final String NAME = "STRAIGHT";

	private float mSeaHeight;

	public PlayerMissileShooter(float pSeaHeight) {
		super(NAME);

		mSeaHeight = pSeaHeight;
	}

	@Override
	protected void update(LintfordCore pCore, Projectile pProjectile) {
		float lDelta = (float) pCore.appTime().elapsedTimeMilli() / 1000f;

		final boolean isAboveWater = pProjectile.worldPositionY <= mSeaHeight;

		pProjectile.dx = isAboveWater ? (40.0f + (20.0f * pProjectile.timeSinceStart * lDelta)) : 150.f;
		pProjectile.dy = (20.0f + (40.0f * pProjectile.timeSinceStart * lDelta)) * pProjectile.ody;

		pProjectile.worldPositionX = pProjectile.baseWorldPositionX;
		pProjectile.worldPositionY = pProjectile.baseWorldPositionY;
		pProjectile.rotationInRadians = (float) Math.atan2(pProjectile.dx, -pProjectile.dy) - (float) Math.toRadians(90.f);

		super.update(pCore, pProjectile);
	}
}
