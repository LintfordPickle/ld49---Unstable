package net.ld.unstable.data.mobs.definitions;

import net.ld.unstable.data.mobs.Submarine;
import net.ld.unstable.data.mobs.attachments.PowerCoreAttachment;
import net.ld.unstable.data.mobs.attachments.PropellerAttachment;
import net.ld.unstable.data.mobs.attachments.SubAttachment;
import net.lintford.library.core.geometry.spritegraph.instance.SpriteGraphInstance;

public class PlayerSubmarine extends MobDefinition {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String MOB_DEFINITION_NAME = "Player Submarine";

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public PlayerSubmarine() {
		super(MOB_DEFINITION_NAME);

		SpritegraphName = "SPRITEGRAPH_SUBMARINE";

		maxHealth = 100;
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	@Override
	public void AttachMovementPattern(Submarine pSubmarineInstance) {

	}

	@Override
	public void AttachSpriteGraphStuff(Submarine pSubmarineInstance, SpriteGraphInstance pSpriteGraphInstance) {
		pSpriteGraphInstance.attachItemToNode(new SubAttachment());
		pSpriteGraphInstance.attachItemToNode(new PropellerAttachment());
		pSpriteGraphInstance.attachItemToNode(new PowerCoreAttachment());
	}

}
