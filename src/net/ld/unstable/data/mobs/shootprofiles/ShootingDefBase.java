package net.ld.unstable.data.mobs.shootprofiles;

import net.ld.unstable.controllers.ProjectileController;
import net.ld.unstable.data.mobs.ShmupMob;
import net.lintford.library.core.LintfordCore;

public abstract interface ShootingDefBase {

	public abstract void update(LintfordCore pCore, ShmupMob pMob, ProjectileController pProjectileController);
}
