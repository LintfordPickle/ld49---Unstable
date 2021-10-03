package net.ld.unstable.data.mobs.movementpatterns;

import net.ld.unstable.data.mobs.SmhupMob;
import net.lintford.library.core.LintfordCore;

public abstract interface MovingDefBase {

	public abstract void update(LintfordCore pCore, SmhupMob pMob);
}
