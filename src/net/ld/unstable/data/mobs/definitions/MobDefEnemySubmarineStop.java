package net.ld.unstable.data.mobs.definitions;

import net.ld.unstable.controllers.MobController;
import net.ld.unstable.controllers.ProjectileController;
import net.ld.unstable.data.mobs.ShmupMob;
import net.ld.unstable.data.mobs.attachments.EnemySubAttachment;
import net.ld.unstable.data.mobs.attachments.PropellerAttachment;
import net.lintford.library.core.geometry.spritegraph.instance.SpriteGraphInstance;

public class MobDefEnemySubmarineStop extends MobDefinition {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String MOB_DEFINITION_NAME = "ENEMY_SUBMARINE_STOP";

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public MobDefEnemySubmarineStop() {
		super(MOB_DEFINITION_NAME);

		SpritegraphName = "SPRITEGRAPH_SUBMARINE";

		shootsTorpedoes = true;
		shootsBarrels = false;
		
		emitsBubbles = true;

		maxHealth = 30;
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	@Override
	public void AttachMovementPattern(ShmupMob pSubmarineInstance) {
		pSubmarineInstance.movementPattern = MobController.movingDefEnemySubStop;
	}

	@Override
	public void AttachShootingPattern(ShmupMob pSubmarineInstance) {
		pSubmarineInstance.shootingPattern = ProjectileController.shootingProfileSubmarine;
	}

	@Override
	public void AttachSpriteGraphStuff(ShmupMob pSubmarineInstance, SpriteGraphInstance pSpriteGraphInstance) {
		pSpriteGraphInstance.attachItemToNode(new EnemySubAttachment());
		pSpriteGraphInstance.attachItemToNode(new PropellerAttachment());
	}

}
