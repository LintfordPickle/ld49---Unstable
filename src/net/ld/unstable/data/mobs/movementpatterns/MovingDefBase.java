package net.ld.unstable.data.mobs.movementpatterns;

import net.ld.unstable.data.mobs.ShmupMob;
import net.lintford.library.core.LintfordCore;

public abstract interface MovingDefBase {

	public abstract void update(LintfordCore pCore, ShmupMob pMob);
}
