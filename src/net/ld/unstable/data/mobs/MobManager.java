package net.ld.unstable.data.mobs;

import java.util.ArrayList;
import java.util.List;

import net.ld.unstable.data.mobs.definitions.MobDefEnemyBoatStop;
import net.ld.unstable.data.mobs.definitions.MobDefEnemyBoatStraight;
import net.ld.unstable.data.mobs.definitions.MobDefEnemyMine;
import net.ld.unstable.data.mobs.definitions.MobDefEnemySubmarineStraight;
import net.ld.unstable.data.mobs.definitions.MobDefEnemyTurret;
import net.ld.unstable.data.mobs.definitions.MobDefEnemyTurretBoatStop;
import net.ld.unstable.data.mobs.definitions.MobDefinition;
import net.ld.unstable.data.mobs.definitions.MobDefPlayerSubmarine;

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
			mobDefinitions.add(new MobDefPlayerSubmarine());
			mobDefinitions.add(new MobDefEnemySubmarineStraight());
			
			mobDefinitions.add(new MobDefEnemyBoatStraight());
			mobDefinitions.add(new MobDefEnemyBoatStop());
			
			mobDefinitions.add(new MobDefEnemyTurretBoatStop());
			mobDefinitions.add(new MobDefEnemyMine());
			mobDefinitions.add(new MobDefEnemyTurret());
		}
	}

	// --------------------------------------
	// Variables
	// --------------------------------------

	private final MobDefinitionManager mMobDefinitionManager = new MobDefinitionManager();
	public SmhupMob playerSubmarine;
	private final List<SmhupMob> mMobEntities = new ArrayList<>();

	// --------------------------------------
	// Properties
	// --------------------------------------

	public List<SmhupMob> mobs() {
		return mMobEntities;
	}

	public MobDefinitionManager mobDefinitionManager() {
		return mMobDefinitionManager;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public MobManager() {
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

	public SmhupMob getMobByIndex(int pIndex) {
		if (pIndex < 0 || pIndex >= mMobEntities.size()) {
			return null;
		}

		return mMobEntities.get(pIndex);
	}

	// FIXME: recycle
	public SmhupMob addNewMob(MobDefinition pMobDefinition, float pPosX, float pPosY) {
		final var lNewMob = new SmhupMob();
		lNewMob.worldPositionX = pPosX;
		lNewMob.worldPositionY = pPosY;
		lNewMob.baseWorldPositionX = pPosX;
		lNewMob.baseWorldPositionY = pPosY;

		mMobEntities.add(lNewMob);

		return lNewMob;
	}

}
