package net.ld.unstable.data.mobs.definitions;

import net.ld.unstable.data.mobs.ShmupMob;
import net.lintford.library.core.geometry.spritegraph.instance.SpriteGraphInstance;

public abstract class MobDefinition {

	// --------------------------------------
	// Variables
	// --------------------------------------

	public final String mobDefinitionName;
	public String SpritegraphName;

	public boolean largeCollisionEntity; // two circles horizontally next to each other (collisionRadius*2)
	public float collisionRadius;

	public boolean shootsTorpedoes;
	public boolean shootsMissiles;
	public boolean shootsBarrels;

	public int maxCoolant;
	public int maxHealth;

	// --------------------------------------
	// Constuctor
	// --------------------------------------

	public MobDefinition(String pMobDefinitionName) {
		mobDefinitionName = pMobDefinitionName;
		
		largeCollisionEntity = true;
		collisionRadius = 25.f;
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	public abstract void AttachMovementPattern(ShmupMob pSubmarineInstance);

	public abstract void AttachShootingPattern(ShmupMob pSubmarineInstance);

	public abstract void AttachSpriteGraphStuff(ShmupMob pSubmarineInstance, SpriteGraphInstance pSpriteGraphInstance);
}
