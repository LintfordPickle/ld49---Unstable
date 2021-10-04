package net.ld.unstable.controllers;

import java.util.ArrayList;
import java.util.List;

import net.ld.unstable.data.mobs.ShmupMob;
import net.ld.unstable.data.mobs.definitions.MobDefEnemyMine;
import net.ld.unstable.data.mobs.definitions.MobDefEnemySubmarineStop;
import net.ld.unstable.data.mobs.definitions.MobDefEnemySubmarineStraight;
import net.ld.unstable.data.mobs.definitions.MobDefEnemyTurret;
import net.ld.unstable.data.mobs.definitions.MobDefEnemyTurretBoatStop;
import net.ld.unstable.data.mobs.movementpatterns.MovingDefEnemySubStop;
import net.ld.unstable.data.waves.IMobSpawner;
import net.ld.unstable.data.waves.Wave;
import net.ld.unstable.data.waves.WaveManager;
import net.lintford.library.controllers.BaseController;
import net.lintford.library.controllers.core.ControllerManager;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.debug.Debug;

public class WaveController extends BaseController implements IMobSpawner {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String CONTROLLER_NAME = "Wave Controller";

	private final int TIME_BEFORE_FIRST_WAVE = 10000;
	private final int TIME_BETWEEN_WAVES = 1500;

	// --------------------------------------
	// Variables
	// --------------------------------------

	private MobController mMobController;

	private WaveManager mWaveManager;
	private float mTimerBetweenWaves;
	private Wave mCurrentWave;
	private final List<ShmupMob> mobUpdateList = new ArrayList<>();

	// --------------------------------------
	// Properties
	// --------------------------------------

	@Override
	public boolean isInitialized() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean hasWaveFinished() {
		return mCurrentWave == null || mCurrentWave.isWaveComplete() && mWaveManager.waveSpawner().isFinishedSpawning();
	}

	public boolean haveWavesStarted() {
		return mHasGameStarted;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public WaveController(ControllerManager pControllerManager, WaveManager pWaveManager, int pEntityGroupUid) {
		super(pControllerManager, CONTROLLER_NAME, pEntityGroupUid);

		mWaveManager = pWaveManager;
		mWaveManager.waveSpawner().mMobSpawner = this;
		mTimerBetweenWaves = TIME_BEFORE_FIRST_WAVE;
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void initialize(LintfordCore pCore) {
		final var lControllerManager = pCore.controllerManager();

		mMobController = (MobController) lControllerManager.getControllerByNameRequired(MobController.CONTROLLER_NAME, entityGroupID());
	}

	@Override
	public void unload() {

	}

	@Override
	public void update(LintfordCore pCore) {
		super.update(pCore);

		if (mHasGameStarted == false)
			return;

		final var lWaveSpawner = mWaveManager.waveSpawner();
		updateWave(pCore);

		if (mCurrentWave == null) {
			mTimerBetweenWaves -= pCore.gameTime().elapsedTimeMilli();

			if (mTimerBetweenWaves <= 0) {
				createNewWave();
			}
		} else {
			if (lWaveSpawner.isFinishedSpawning() == false) {
				lWaveSpawner.update(pCore);
			}

			if (lWaveSpawner.isFinishedSpawning() && mCurrentWave.isWaveComplete()) {
				mCurrentWave = null;
				mTimerBetweenWaves = TIME_BETWEEN_WAVES;
				Debug.debugManager().logger().i(getClass().getSimpleName(), "Wave Completed");
			}
		}
	}

	public void updateWave(LintfordCore pCore) {
		if (mCurrentWave == null || mCurrentWave.isWaveCompleted())
			return;

		mobUpdateList.clear();
		final var lMobsInWave = mCurrentWave.waveMembers;
		final int lNumMobsInWave = lMobsInWave.size();
		mobUpdateList.addAll(lMobsInWave);
		for (int i = 0; i < +lNumMobsInWave; i++) {
			final var lCurrentMob = mobUpdateList.get(i);
			if (lCurrentMob.isAlive == false) {
				lMobsInWave.remove(lCurrentMob);
			}
		}
	}

	@Override
	public void spawnMob(float pWorldX, float pWorldY, String pDefName) {
		final var lNewMob = mMobController.addNewMob(false, pDefName, pWorldX, pWorldY);

		if (lNewMob != null) {
			mCurrentWave.waveMembers.add(lNewMob);

		}
	}

	// --------------------------------------

	private boolean mHasGameStarted = false;

	public void startNewGame() {
		mHasGameStarted = true;
		
		
		final float lTimeMod = 2.0f;
		mCurrentWave = mWaveManager.getFreePooledItem();
		final var lWaveSpawner = mWaveManager.waveSpawner();
		lWaveSpawner.addSpawnItem(0, 0, TIME_BEFORE_FIRST_WAVE, null);
		
		lWaveSpawner.addSpawnItem(700, -50, 200 * lTimeMod, MobDefEnemySubmarineStop.MOB_DEFINITION_NAME);
		lWaveSpawner.addSpawnItem(700, 200, 200 * lTimeMod, MobDefEnemySubmarineStop.MOB_DEFINITION_NAME);
		
		
		//createLevelWave();
	}

	private void createLevelWave() {
		Debug.debugManager().logger().i(getClass().getSimpleName(), "Creating New Wave");

		final float lTimeMod = 2.0f;
		mCurrentWave = mWaveManager.getFreePooledItem();

		final var lWaveSpawner = mWaveManager.waveSpawner();
		lWaveSpawner.addSpawnItem(0, 0, TIME_BEFORE_FIRST_WAVE, null);

		lWaveSpawner.addSpawnItem(700, 0, 200 * lTimeMod, MobDefEnemyTurret.MOB_DEFINITION_NAME);

		lWaveSpawner.addSpawnItem(700, 0, 200 * lTimeMod, MobDefEnemySubmarineStraight.MOB_DEFINITION_NAME);

		lWaveSpawner.addSpawnItem(700, 0, 200 * lTimeMod, MobDefEnemyTurretBoatStop.MOB_DEFINITION_NAME);

		lWaveSpawner.addSpawnItem(700, -150, 0 * lTimeMod, MobDefEnemyMine.MOB_DEFINITION_NAME);
		lWaveSpawner.addSpawnItem(700, +150, 1000 * lTimeMod, MobDefEnemyMine.MOB_DEFINITION_NAME);
		lWaveSpawner.addSpawnItem(700, 0, 200 * lTimeMod, MobDefEnemyMine.MOB_DEFINITION_NAME);

		lWaveSpawner.addSpawnItem(700, 0, 2500 * lTimeMod, MobDefEnemyTurret.MOB_DEFINITION_NAME);
		lWaveSpawner.addSpawnItem(700, 0, 200 * lTimeMod, MobDefEnemyTurret.MOB_DEFINITION_NAME);

		lWaveSpawner.addSpawnItem(700, -100, 2000 * lTimeMod, MobDefEnemySubmarineStraight.MOB_DEFINITION_NAME);
		lWaveSpawner.addSpawnItem(700, 0, 2000 * lTimeMod, MobDefEnemySubmarineStraight.MOB_DEFINITION_NAME);
		lWaveSpawner.addSpawnItem(700, 100, 2000 * lTimeMod, MobDefEnemySubmarineStraight.MOB_DEFINITION_NAME);
		lWaveSpawner.addSpawnItem(700, 200, 2000 * lTimeMod, MobDefEnemySubmarineStraight.MOB_DEFINITION_NAME);

		lWaveSpawner.addSpawnItem(700, 0, 200 * lTimeMod, MobDefEnemyTurretBoatStop.MOB_DEFINITION_NAME);

		lWaveSpawner.addSpawnItem(700, 0, 2500 * lTimeMod, MobDefEnemyTurret.MOB_DEFINITION_NAME);
		lWaveSpawner.addSpawnItem(700, 0, 200 * lTimeMod, MobDefEnemyTurret.MOB_DEFINITION_NAME);

		lWaveSpawner.addSpawnItem(700, -150, 0 * lTimeMod, MobDefEnemyMine.MOB_DEFINITION_NAME);
		lWaveSpawner.addSpawnItem(700, +150, 1000 * lTimeMod, MobDefEnemyMine.MOB_DEFINITION_NAME);
		lWaveSpawner.addSpawnItem(700, 0, 200 * lTimeMod, MobDefEnemyMine.MOB_DEFINITION_NAME);

		lWaveSpawner.addSpawnItem(700, 0, 200 * lTimeMod, MobDefEnemyTurretBoatStop.MOB_DEFINITION_NAME);

		lWaveSpawner.addSpawnItem(700, -100, 2000 * lTimeMod, MobDefEnemySubmarineStraight.MOB_DEFINITION_NAME);
		lWaveSpawner.addSpawnItem(700, 0, 2000 * lTimeMod, MobDefEnemySubmarineStraight.MOB_DEFINITION_NAME);
		lWaveSpawner.addSpawnItem(700, 100, 2000 * lTimeMod, MobDefEnemySubmarineStraight.MOB_DEFINITION_NAME);
		lWaveSpawner.addSpawnItem(700, 200, 2000 * lTimeMod, MobDefEnemySubmarineStraight.MOB_DEFINITION_NAME);

	}

	private void createNewWave() {
		Debug.debugManager().logger().i(getClass().getSimpleName(), "Creating New Wave");

		final float lTimeMod = 2.0f;
		mCurrentWave = mWaveManager.getFreePooledItem();

		final var lWaveSpawner = mWaveManager.waveSpawner();

		lWaveSpawner.addSpawnItem(700, -150, 0 * lTimeMod, MobDefEnemyMine.MOB_DEFINITION_NAME);
		lWaveSpawner.addSpawnItem(700, +150, 5000 * lTimeMod, MobDefEnemyMine.MOB_DEFINITION_NAME);
		lWaveSpawner.addSpawnItem(700, 0, 200 * lTimeMod, MobDefEnemyMine.MOB_DEFINITION_NAME);

	}

}
