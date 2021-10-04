package net.ld.unstable.data.mobs.definitions;

import net.ld.unstable.controllers.MobController;
import net.ld.unstable.controllers.ProjectileController;
import net.ld.unstable.data.mobs.ShmupMob;
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

		emitsBubbles = false;
		
		maxHealth = 30;
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	@Override
	public void AttachMovementPattern(ShmupMob pMobInstance) {
		pMobInstance.movementPattern = MobController.turretMovementDef;
	}

	@Override
	public void AttachShootingPattern(ShmupMob pMobInstance) {
		pMobInstance.shootingPattern = ProjectileController.shootingProfileTurret;
	}

	@Override
	public void AttachSpriteGraphStuff(ShmupMob pMobInstance, SpriteGraphInstance pSpriteGraphInstance) {
		pSpriteGraphInstance.attachItemToNode(new TurretAttachment());
	}

}
