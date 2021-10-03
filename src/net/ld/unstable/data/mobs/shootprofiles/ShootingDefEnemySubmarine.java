package net.ld.unstable.data.mobs.shootprofiles;

import net.ld.unstable.controllers.ProjectileController;
import net.ld.unstable.data.mobs.SmhupMob;
import net.lintford.library.core.LintfordCore;

public class ShootingDefEnemySubmarine implements ShootingDefBase {

	@Override
	public void update(LintfordCore pCore, SmhupMob pMob, ProjectileController pProjectileShooter) {
		if (pMob.mobDefinition.shootsTorpedoes && pMob.shootTimer <= 0.f) {
			pProjectileShooter.shootEnemyBullet(ProjectileController.ENEMY_BULLETS_UID, pMob.worldPositionX, pMob.worldPositionY, -1, 0);
			pMob.shootTimer = 350.f;
		}
	}
}
