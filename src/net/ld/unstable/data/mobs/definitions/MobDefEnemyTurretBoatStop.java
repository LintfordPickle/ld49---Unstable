package net.ld.unstable.data.mobs.definitions;

import net.ld.unstable.controllers.MobController;
import net.ld.unstable.controllers.ProjectileController;
import net.ld.unstable.data.mobs.SmhupMob;
import net.ld.unstable.data.mobs.attachments.TurretBoatAttachment;
import net.lintford.library.core.geometry.spritegraph.instance.SpriteGraphInstance;

public class MobDefEnemyTurretBoatStop extends MobDefinition {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String MOB_DEFINITION_NAME = "TURRETBOAT_STOP";

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public MobDefEnemyTurretBoatStop() {
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
		pMobInstance.movementPattern = MobController.surfaceMoveWithStop;
	}

	@Override
	public void AttachShootingPattern(SmhupMob pSubmarineInstance) {
		pSubmarineInstance.shootingPattern = ProjectileController.shootingProfileTurretBoat;
	}

	@Override
	public void AttachSpriteGraphStuff(SmhupMob pMobInstance, SpriteGraphInstance pSpriteGraphInstance) {
		pSpriteGraphInstance.attachItemToNode(new TurretBoatAttachment());
	}

}
