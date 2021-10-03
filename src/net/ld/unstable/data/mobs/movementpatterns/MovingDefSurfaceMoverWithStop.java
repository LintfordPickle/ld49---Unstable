package net.ld.unstable.data.mobs.movementpatterns;

import net.ld.unstable.data.mobs.ShmupMob;
import net.lintford.library.core.LintfordCore;

public class MovingDefSurfaceMoverWithStop implements MovingDefBase {

	private float mSeaLevel;

	public MovingDefSurfaceMoverWithStop(float pSeaLevel) {
		mSeaLevel = pSeaLevel;
	}

	@Override
	public void update(LintfordCore pCore, ShmupMob pMob) {
		float lDelta = (float) pCore.appTime().elapsedTimeMilli() / 1000f;

		final boolean stillMoving = pMob.timeSinceStart < 5000;
		
		pMob.dx = stillMoving ? -100.0f : 0.f;
		pMob.dy = 0.0f;

		// Need to move the base position along
		pMob.baseWorldPositionX += pMob.dx * lDelta;
		pMob.baseWorldPositionY = mSeaLevel - 20;

		pMob.worldPositionX += pMob.dx * lDelta;

		pMob.worldPositionX = pMob.baseWorldPositionX;
		pMob.worldPositionY = pMob.baseWorldPositionY + (float)Math.cos(pMob.timeSinceStart * .005f) * 5f;
	}
}
