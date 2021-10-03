package net.ld.unstable.data.mobs.definitions;

import net.ld.unstable.controllers.MobController;
import net.ld.unstable.data.mobs.SmhupMob;
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
		pMobInstance.movementPattern = MobController.movingDefEnemyMine;
	}

	@Override
	public void AttachShootingPattern(SmhupMob pMobInstance) {

	}

	@Override
	public void AttachSpriteGraphStuff(SmhupMob pMobInstance, SpriteGraphInstance pSpriteGraphInstance) {
		pSpriteGraphInstance.attachItemToNode(new MineAttachment());
	}

}
