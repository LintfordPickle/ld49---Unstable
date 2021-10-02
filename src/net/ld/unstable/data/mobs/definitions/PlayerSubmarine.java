package net.ld.unstable.data.mobs.definitions;

public class PlayerSubmarine extends MobDefinition {

	public static final String MOB_DEFINITION_NAME = "Player Submarine";

	public PlayerSubmarine() {
		super(MOB_DEFINITION_NAME);

		SpritegraphName = "SPRITEGRAPH_SUBMARINE";
		
		maxHealth = 100;
	}

}
