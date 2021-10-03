package net.ld.unstable.data.mobs.patterns;

import net.ld.unstable.data.mobs.Submarine;
import net.lintford.library.core.LintfordCore;

public class SurfaceMover implements MobMovementPattern {

	private float mSeaLevel;

	public SurfaceMover(float pSeaLevel) {
		mSeaLevel = pSeaLevel;
	}

	@Override
	public void update(LintfordCore pCore, Submarine pSubmarine) {
		float lDelta = (float) pCore.appTime().elapsedTimeMilli() / 1000f;

		pSubmarine.dx = -100.0f;
		pSubmarine.dy = 0.0f;

		// Need to move the base position along
		pSubmarine.baseWorldPositionX += pSubmarine.dx * lDelta;
		pSubmarine.baseWorldPositionY = mSeaLevel - 20;

		pSubmarine.worldPositionX += pSubmarine.dx * lDelta;

		pSubmarine.worldPositionX = pSubmarine.baseWorldPositionX;
		pSubmarine.worldPositionY = pSubmarine.baseWorldPositionY + (float)Math.cos(pSubmarine.timeSinceStart * .005f) * 5f;
	}
}
