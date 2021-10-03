package net.ld.unstable.data.mobs.definitions;

import net.ld.unstable.controllers.SubController;
import net.ld.unstable.data.mobs.Submarine;
import net.ld.unstable.data.mobs.attachments.PropellerAttachment;
import net.ld.unstable.data.mobs.attachments.SubAttachment;
import net.lintford.library.core.geometry.spritegraph.instance.SpriteGraphInstance;

public class EnemySubmarineStraight00 extends MobDefinition {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String MOB_DEFINITION_NAME = "Submarine 00";

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public EnemySubmarineStraight00() {
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
	public void AttachMovementPattern(Submarine pSubmarineInstance) {
		pSubmarineInstance.movementPattern = SubController.CosMover;
	}

	@Override
	public void AttachSpriteGraphStuff(Submarine pSubmarineInstance, SpriteGraphInstance pSpriteGraphInstance) {
		pSpriteGraphInstance.attachItemToNode(new SubAttachment());
		pSpriteGraphInstance.attachItemToNode(new PropellerAttachment());
	}

}
