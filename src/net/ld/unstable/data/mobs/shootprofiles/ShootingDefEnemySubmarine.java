package net.ld.unstable.data.mobs.shootprofiles;

import net.ld.unstable.ConstantsGame;
import net.ld.unstable.controllers.ProjectileController;
import net.ld.unstable.data.mobs.ShmupMob;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.maths.RandomNumbers;

public class ShootingDefEnemySubmarine implements ShootingDefBase {

	@Override
	public void update(LintfordCore pCore, ShmupMob pMob, ProjectileController pProjectileShooter) {
		final float lTolerance = 400.f;
		if (pMob.worldPositionX > pCore.gameCamera().getPosition().x + ConstantsGame.WINDOW_WIDTH * .5f + lTolerance)
			return;

		if (pMob.mobDefinition.shootsTorpedoes && pMob.shootTimer <= 0.f) {
			pProjectileShooter.shootEnemyBullet(ProjectileController.ENEMY_BULLETS_UID, pMob.worldPositionX, pMob.worldPositionY, -1, 0);
			pMob.shootTimer = 550.f + RandomNumbers.random(100, 500);
		}
	}
}
