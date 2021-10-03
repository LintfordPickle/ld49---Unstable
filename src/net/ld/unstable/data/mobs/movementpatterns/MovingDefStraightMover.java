package net.ld.unstable.data.mobs.movementpatterns;

import net.ld.unstable.data.mobs.ShmupMob;
import net.lintford.library.core.LintfordCore;

public class MovingDefStraightMover implements MovingDefBase {

	@Override
	public void update(LintfordCore pCore, ShmupMob pMob) {
		float lDelta = (float) pCore.appTime().elapsedTimeMilli() / 1000f;

		pMob.dx = -100.0f;
		pMob.dy = 0.0f;

		pMob.worldPositionX += pMob.dx * lDelta;

		pMob.worldPositionX = pMob.baseWorldPositionX;
		pMob.worldPositionY = pMob.baseWorldPositionY;

		// Need to move the base position along
		pMob.baseWorldPositionX += pMob.dx * lDelta;
		pMob.baseWorldPositionY += pMob.dy * lDelta;
	}
}
