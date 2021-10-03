package net.ld.unstable.data.mobs.shootprofiles;

import net.ld.unstable.controllers.ProjectileController;
import net.ld.unstable.data.mobs.SmhupMob;
import net.lintford.library.core.LintfordCore;

public abstract interface ShootingDefBase {

	public abstract void update(LintfordCore pCore, SmhupMob pMob, ProjectileController pProjectileController);
}
