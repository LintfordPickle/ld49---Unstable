package net.ld.unstable.data.mobs.patterns;

import net.ld.unstable.data.mobs.Submarine;
import net.lintford.library.core.LintfordCore;

public abstract interface MobMovementPattern {

	public abstract void update(LintfordCore pCore, Submarine pSubmarine);
}
