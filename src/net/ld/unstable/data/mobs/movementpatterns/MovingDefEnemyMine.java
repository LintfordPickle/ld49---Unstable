package net.ld.unstable.data.mobs.movementpatterns;

import net.ld.unstable.data.mobs.ShmupMob;
import net.lintford.library.core.LintfordCore;

public class MovingDefEnemyMine implements MovingDefBase {

	private float mSeaLevel;

	public MovingDefEnemyMine(float pSeaLevel) {
		mSeaLevel = pSeaLevel;
	}

	@Override
	public void update(LintfordCore pCore, ShmupMob pMob) {
		final float lTimeMod = 0.0005f;
		final float lMagnitude = 30.f;

		if (pMob.worldPositionY < mSeaLevel) {
			pMob.baseWorldPositionY = mSeaLevel;
		}

		pMob.worldPositionX = pMob.baseWorldPositionX + ((float) Math.cos(pMob.timeSinceStart * lTimeMod)) * lMagnitude * .5f;
		pMob.worldPositionY = pMob.baseWorldPositionY + ((float) Math.sin(pMob.timeSinceStart * lTimeMod)) * lMagnitude;
	}
}
