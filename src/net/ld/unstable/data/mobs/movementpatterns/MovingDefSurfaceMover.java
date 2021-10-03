package net.ld.unstable.data.mobs.movementpatterns;

import net.ld.unstable.data.mobs.SmhupMob;
import net.lintford.library.core.LintfordCore;

public class MovingDefSurfaceMover implements MovingDefBase {

	private float mSeaLevel;

	public MovingDefSurfaceMover(float pSeaLevel) {
		mSeaLevel = pSeaLevel;
	}

	@Override
	public void update(LintfordCore pCore, SmhupMob pMob) {
		float lDelta = (float) pCore.appTime().elapsedTimeMilli() / 1000f;

		pMob.dx = -100.0f;
		pMob.dy = 0.0f;

		// Need to move the base position along
		pMob.baseWorldPositionX += pMob.dx * lDelta;
		pMob.baseWorldPositionY = mSeaLevel - 20;

		pMob.worldPositionX += pMob.dx * lDelta;

		pMob.worldPositionX = pMob.baseWorldPositionX;
		pMob.worldPositionY = pMob.baseWorldPositionY + (float)Math.cos(pMob.timeSinceStart * .005f) * 5f;
	}
}
