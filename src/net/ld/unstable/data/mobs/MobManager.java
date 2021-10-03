package net.ld.unstable.data.mobs;

import java.util.ArrayList;
import java.util.List;

import net.ld.unstable.data.mobs.definitions.EnemyBoatStraight;
import net.ld.unstable.data.mobs.definitions.EnemySubmarineStraight00;
import net.ld.unstable.data.mobs.definitions.MobDefinition;
import net.ld.unstable.data.mobs.definitions.PlayerSubmarine;

public class MobManager {

	// --------------------------------------
	// Inner-Classes
	// --------------------------------------

	public class MobDefinitionManager {
		private List<MobDefinition> mobDefinitions = new ArrayList<>();

		/// Returns null if not found
		public MobDefinition getMobDefinitionByName(String pMobDefinitionName) {
			final int lMobDefCount = mobDefinitions.size();
			for (int i = 0; i < lMobDefCount; i++) {
				if (mobDefinitions.get(i).mobDefinitionName.equals(pMobDefinitionName)) {
					return mobDefinitions.get(i);
				}
			}

			return null;
		}

		public MobDefinitionManager() {

		}

		public void initialise() {
			loadDefintions();
		}

		public void loadDefintions() {
			mobDefinitions.add(new PlayerSubmarine());
			mobDefinitions.add(new EnemySubmarineStraight00());
			mobDefinitions.add(new EnemyBoatStraight());
		}
	}

	// --------------------------------------
	// Variables
	// --------------------------------------

	private int shooterUidCounter;

	private final MobDefinitionManager mMobDefinitionManager = new MobDefinitionManager();
	public Submarine playerSubmarine;
	private final List<Submarine> mMobEntities = new ArrayList<>();

	// --------------------------------------
	// Properties
	// --------------------------------------

	public List<Submarine> mobs() {
		return mMobEntities;
	}

	public MobDefinitionManager mobDefinitionManager() {
		return mMobDefinitionManager;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public MobManager() {
		shooterUidCounter = 0;
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	public void initialize() {
		mMobDefinitionManager.initialise();
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	public Submarine getMobByIndex(int pIndex) {
		if (pIndex < 0 || pIndex >= mMobEntities.size()) {
			return null;
		}

		return mMobEntities.get(pIndex);
	}

	// FIXME: recycle
	public Submarine addNewMob(MobDefinition pMobDefinition, float pPosX, float pPosY) {
		final var lNewMob = new Submarine();
		lNewMob.worldPositionX = pPosX;
		lNewMob.worldPositionY = pPosY;
		lNewMob.baseWorldPositionX = pPosX;
		lNewMob.baseWorldPositionY = pPosY;

		mMobEntities.add(lNewMob);

		return lNewMob;
	}

}
