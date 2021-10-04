package net.ld.unstable.data.mobs.definitions;

import net.ld.unstable.controllers.MobController;
import net.ld.unstable.data.mobs.ShmupMob;
import net.ld.unstable.data.mobs.attachments.MineAttachment;
import net.lintford.library.core.geometry.spritegraph.instance.SpriteGraphInstance;

public class MobDefEnemyMine extends MobDefinition {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String MOB_DEFINITION_NAME = "MINE";

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public MobDefEnemyMine() {
		super(MOB_DEFINITION_NAME);

		SpritegraphName = "SPRITEGRAPH_SUBMARINE";

		largeCollisionEntity = false;
		collisionRadius = 40.f;
		
		shootsTorpedoes = true;
		shootsMissiles = false;
		shootsBarrels = true;
		
		emitsBubbles = false;

		maxHealth = 20;
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	@Override
	public void AttachMovementPattern(ShmupMob pMobInstance) {
		pMobInstance.movementPattern = MobController.movingDefEnemyMine;
	}

	@Override
	public void AttachShootingPattern(ShmupMob pMobInstance) {

	}

	@Override
	public void AttachSpriteGraphStuff(ShmupMob pMobInstance, SpriteGraphInstance pSpriteGraphInstance) {
		pSpriteGraphInstance.attachItemToNode(new MineAttachment());
	}

}
