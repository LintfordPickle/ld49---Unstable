package net.ld.unstable.data.mobs.definitions;

import net.ld.unstable.controllers.MobController;
import net.ld.unstable.controllers.ProjectileController;
import net.ld.unstable.data.mobs.SmhupMob;
import net.ld.unstable.data.mobs.attachments.TurretAttachment;
import net.lintford.library.core.geometry.spritegraph.instance.SpriteGraphInstance;

public class MobDefEnemyTurret extends MobDefinition {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String MOB_DEFINITION_NAME = "TURRET";

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public MobDefEnemyTurret() {
		super(MOB_DEFINITION_NAME);

		SpritegraphName = "SPRITEGRAPH_SUBMARINE";

		shootsTorpedoes = true;
		shootsMissiles = false;
		shootsBarrels = true;

		maxHealth = 20;
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	@Override
	public void AttachMovementPattern(SmhupMob pMobInstance) {
		pMobInstance.movementPattern = MobController.turretMovementDef;
	}

	@Override
	public void AttachShootingPattern(SmhupMob pMobInstance) {
		pMobInstance.shootingPattern = ProjectileController.shootingProfileTurret;
	}

	@Override
	public void AttachSpriteGraphStuff(SmhupMob pMobInstance, SpriteGraphInstance pSpriteGraphInstance) {
		pSpriteGraphInstance.attachItemToNode(new TurretAttachment());
	}

}
