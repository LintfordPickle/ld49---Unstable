package net.ld.unstable.data.mobs.definitions;

public abstract class MobDefinition {

	public final String mobDefinitionName;
	public String SpritegraphName;

	public float maxHealth;

	public MobDefinition(String pMobDefinitionName) {
		mobDefinitionName = pMobDefinitionName;
	}

}
