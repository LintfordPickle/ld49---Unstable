package net.ld.unstable.data.mobs.movementpatterns;

import net.ld.unstable.data.mobs.ShmupMob;
import net.lintford.library.core.LintfordCore;

public class MovingDefCosineMover implements MovingDefBase {

	final float mSeaLevel;

	public MovingDefCosineMover(float pSeaLevel) {
		mSeaLevel = pSeaLevel;
	}

	@Override
	public void update(LintfordCore pCore, ShmupMob pMob) {
		float lDelta = (float) pCore.appTime().elapsedTimeMilli() / 1000f;

		pMob.dx = -20.0f;
		pMob.dy = 0.0f;

		// Need to move the base position along
		pMob.baseWorldPositionX += pMob.dx * lDelta;
		pMob.baseWorldPositionY += pMob.dy * lDelta;

		pMob.worldPositionX += pMob.dx * lDelta;

		final float lTimeMod = 0.0025f;
		final float lMagnitude = 80.f;

		pMob.worldPositionX = pMob.baseWorldPositionX;
		pMob.worldPositionY = pMob.baseWorldPositionY + ((float) Math.cos(pMob.timeSinceStart * lTimeMod)) * lMagnitude;
		
		if(pMob.worldPositionY < mSeaLevel) {
			pMob.worldPositionY = mSeaLevel;
		}
	}
}
