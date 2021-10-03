package net.ld.unstable.data.mobs.definitions;

import net.ld.unstable.data.mobs.ShmupMob;
import net.ld.unstable.data.mobs.attachments.PowerCoreAttachment;
import net.ld.unstable.data.mobs.attachments.PropellerAttachment;
import net.ld.unstable.data.mobs.attachments.SubAttachment;
import net.lintford.library.core.geometry.spritegraph.instance.SpriteGraphInstance;

public class MobDefPlayerSubmarine extends MobDefinition {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String MOB_DEFINITION_NAME = "PLAYER_SUBMARINE";

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public MobDefPlayerSubmarine() {
		super(MOB_DEFINITION_NAME);

		SpritegraphName = "SPRITEGRAPH_SUBMARINE";

		maxHealth = 100;
		maxCoolant = 100;
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	@Override
	public void AttachMovementPattern(ShmupMob pSubmarineInstance) {

	}

	@Override
	public void AttachShootingPattern(ShmupMob pSubmarineInstance) {

	}

	@Override
	public void AttachSpriteGraphStuff(ShmupMob pSubmarineInstance, SpriteGraphInstance pSpriteGraphInstance) {
		pSpriteGraphInstance.attachItemToNode(new SubAttachment());
		pSpriteGraphInstance.attachItemToNode(new PropellerAttachment());
		pSpriteGraphInstance.attachItemToNode(new PowerCoreAttachment());
	}

}
