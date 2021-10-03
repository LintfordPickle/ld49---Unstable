package net.ld.unstable.data.mobs.definitions;

import net.ld.unstable.controllers.MobController;
import net.ld.unstable.controllers.ProjectileController;
import net.ld.unstable.data.mobs.SmhupMob;
import net.ld.unstable.data.mobs.attachments.PropellerAttachment;
import net.ld.unstable.data.mobs.attachments.SubAttachment;
import net.lintford.library.core.geometry.spritegraph.instance.SpriteGraphInstance;

public class MobDefEnemySubmarineStraight extends MobDefinition {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String MOB_DEFINITION_NAME = "ENEMY_SUBMARINE";

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public MobDefEnemySubmarineStraight() {
		super(MOB_DEFINITION_NAME);

		SpritegraphName = "SPRITEGRAPH_SUBMARINE";

		shootsTorpedoes = true;
		shootsBarrels = false;

		maxHealth = 20;
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	@Override
	public void AttachMovementPattern(SmhupMob pSubmarineInstance) {
		pSubmarineInstance.movementPattern = MobController.CosMover;
	}

	@Override
	public void AttachShootingPattern(SmhupMob pSubmarineInstance) {
		pSubmarineInstance.shootingPattern = ProjectileController.shootingProfileSubmarine;
	}

	@Override
	public void AttachSpriteGraphStuff(SmhupMob pSubmarineInstance, SpriteGraphInstance pSpriteGraphInstance) {
		pSpriteGraphInstance.attachItemToNode(new SubAttachment());
		pSpriteGraphInstance.attachItemToNode(new PropellerAttachment());
	}

}
