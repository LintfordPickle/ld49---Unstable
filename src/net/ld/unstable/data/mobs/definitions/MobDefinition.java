package net.ld.unstable.data.mobs.definitions;

import net.ld.unstable.data.mobs.SmhupMob;
import net.lintford.library.core.geometry.spritegraph.instance.SpriteGraphInstance;

public abstract class MobDefinition {

	// --------------------------------------
	// Variables
	// --------------------------------------

	public final String mobDefinitionName;
	public String SpritegraphName;

	public boolean shootsTorpedoes;
	public boolean shootsMissiles;
	public boolean shootsBarrels;

	public float maxHealth;

	// --------------------------------------
	// Constuctor
	// --------------------------------------

	public MobDefinition(String pMobDefinitionName) {
		mobDefinitionName = pMobDefinitionName;
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	public abstract void AttachMovementPattern(SmhupMob pSubmarineInstance);

	public abstract void AttachShootingPattern(SmhupMob pSubmarineInstance);

	public abstract void AttachSpriteGraphStuff(SmhupMob pSubmarineInstance, SpriteGraphInstance pSpriteGraphInstance);
}
