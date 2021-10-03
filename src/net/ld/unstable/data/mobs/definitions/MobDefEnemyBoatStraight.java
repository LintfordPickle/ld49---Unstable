package net.ld.unstable.data.mobs.definitions;

import net.ld.unstable.controllers.MobController;
import net.ld.unstable.controllers.ProjectileController;
import net.ld.unstable.data.mobs.ShmupMob;
import net.ld.unstable.data.mobs.attachments.BoatAttachment;
import net.lintford.library.core.geometry.spritegraph.instance.SpriteGraphInstance;

public class MobDefEnemyBoatStraight extends MobDefinition {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String MOB_DEFINITION_NAME = "BOAT_STRAIGHT";

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public MobDefEnemyBoatStraight() {
		super(MOB_DEFINITION_NAME);

		SpritegraphName = "SPRITEGRAPH_SUBMARINE";

		shootsTorpedoes = false;
		shootsMissiles = false;
		shootsBarrels = true;

		maxHealth = 20;
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	@Override
	public void AttachMovementPattern(ShmupMob pSubmarineInstance) {
		pSubmarineInstance.movementPattern = MobController.surfaceMover;
	}

	@Override
	public void AttachShootingPattern(ShmupMob pSubmarineInstance) {
		pSubmarineInstance.shootingPattern = ProjectileController.shootingProfileBoat;
	}

	@Override
	public void AttachSpriteGraphStuff(ShmupMob pSubmarineInstance, SpriteGraphInstance pSpriteGraphInstance) {
		pSpriteGraphInstance.attachItemToNode(new BoatAttachment());
	}

}
