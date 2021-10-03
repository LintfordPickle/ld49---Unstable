package net.ld.unstable.data.mobs.shootprofiles;

import net.ld.unstable.controllers.ProjectileController;
import net.ld.unstable.data.mobs.ShmupMob;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.maths.RandomNumbers;

public class ShootingDefEnemyBoat implements ShootingDefBase {

	@Override
	public void update(LintfordCore pCore, ShmupMob pMob, ProjectileController pProjectileShooter) {
		if (pMob.mobDefinition.shootsBarrels && pMob.barrelTimer <= 0.f) {
			pProjectileShooter.dropBarrel(ProjectileController.ENEMY_BULLETS_UID, pMob.worldPositionX, pMob.worldPositionY);
			pMob.barrelTimer = 2000.f + RandomNumbers.random(0, 1000);
		}
	}
}
