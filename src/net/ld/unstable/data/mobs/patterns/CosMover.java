package net.ld.unstable.data.mobs.patterns;

import net.ld.unstable.data.mobs.Submarine;
import net.lintford.library.core.LintfordCore;

public class CosMover implements MobMovementPattern {

	@Override
	public void update(LintfordCore pCore, Submarine pSubmarine) {
		float lDelta = (float) pCore.appTime().elapsedTimeMilli() / 1000f;

		pSubmarine.dx = -100.0f;
		pSubmarine.dy = 0.0f;

		// Need to move the base position along
		pSubmarine.baseWorldPositionX += pSubmarine.dx * lDelta;
		pSubmarine.baseWorldPositionY += pSubmarine.dy * lDelta;
		
	
		pSubmarine.worldPositionX += pSubmarine.dx * lDelta;

		final float lTimeMod = 0.0025f;
		final float lMagnitude = 100.f;

		pSubmarine.worldPositionX = pSubmarine.baseWorldPositionX;
		pSubmarine.worldPositionY = pSubmarine.baseWorldPositionY + ((float) Math.cos(pSubmarine.timeSinceStart * lTimeMod)) * lMagnitude;
	}
}
