package net.ld.unstable.data.mobs.movementpatterns;

import net.ld.unstable.data.mobs.ShmupMob;
import net.lintford.library.core.LintfordCore;

public class MovingDefEnemyMine implements MovingDefBase {

	@Override
	public void update(LintfordCore pCore, ShmupMob pMob) {
		final float lTimeMod = 0.0015f;
		final float lMagnitude = 15.f;

		pMob.worldPositionX = pMob.baseWorldPositionX + ((float) Math.cos(pMob.timeSinceStart * lTimeMod)) * lMagnitude * .5f;
		pMob.worldPositionY = pMob.baseWorldPositionY + ((float) Math.sin(pMob.timeSinceStart * lTimeMod)) * lMagnitude;
	}
}
