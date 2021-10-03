package net.ld.unstable.data.mobs.definitions;

import net.ld.unstable.controllers.SubController;
import net.ld.unstable.data.mobs.Submarine;
import net.ld.unstable.data.mobs.attachments.BoatAttachment;
import net.lintford.library.core.geometry.spritegraph.instance.SpriteGraphInstance;

public class EnemyBoatStraight extends MobDefinition {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String MOB_DEFINITION_NAME = "BOAT";

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public EnemyBoatStraight() {
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
	public void AttachMovementPattern(Submarine pSubmarineInstance) {
		pSubmarineInstance.movementPattern = SubController.surfaceMover;
	}

	@Override
	public void AttachSpriteGraphStuff(Submarine pSubmarineInstance, SpriteGraphInstance pSpriteGraphInstance) {
		pSpriteGraphInstance.attachItemToNode(new BoatAttachment());
	}

}
