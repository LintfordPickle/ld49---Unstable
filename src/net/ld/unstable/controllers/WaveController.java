package net.ld.unstable.controllers;

import java.util.ArrayList;
import java.util.List;

import net.ld.unstable.data.mobs.ShmupMob;
import net.ld.unstable.data.mobs.definitions.MobDefEnemyBoatStraight;
import net.ld.unstable.data.mobs.definitions.MobDefEnemyMine;
import net.ld.unstable.data.mobs.definitions.MobDefEnemySubmarineStraight;
import net.ld.unstable.data.mobs.definitions.MobDefEnemyTurret;
import net.ld.unstable.data.mobs.definitions.MobDefEnemyTurretBoatStop;
import net.ld.unstable.data.waves.IMobSpawner;
import net.ld.unstable.data.waves.Wave;
import net.ld.unstable.data.waves.WaveManager;
import net.ld.unstable.data.waves.WaveSpawner;
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
	private final int TIME_BETWEEN_WAVES = 2000;

	// --------------------------------------
	// Variables
	// --------------------------------------

	private MobController mMobController;
	private GameStateController mGameStateController;

	private WaveManager mWaveManager;
	private float mTimerBetweenWaves;
	private Wave mCurrentWave;
	private final List<ShmupMob> mobUpdateList = new ArrayList<>();

	private boolean mHaveAllWavesFinished;
	private int mCurrentWaveNumber;

	// --------------------------------------
	// Properties
	// --------------------------------------

	@Override
	public boolean isInitialized() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean haveWavesFinished() {
		return mHaveAllWavesFinished;
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
		mCurrentWaveNumber = 0;
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void initialize(LintfordCore pCore) {
		final var lControllerManager = pCore.controllerManager();

		mMobController = (MobController) lControllerManager.getControllerByNameRequired(MobController.CONTROLLER_NAME, entityGroupID());
		mGameStateController = (GameStateController) lControllerManager.getControllerByNameRequired(GameStateController.CONTROLLER_NAME, entityGroupID());
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

		if (mCurrentWave == null && mCurrentWaveNumber < mGameStateController.currentWaveNumber()) {

			mTimerBetweenWaves -= pCore.gameTime().elapsedTimeMilli();

			if (mTimerBetweenWaves <= 0) {
				mCurrentWaveNumber = mGameStateController.currentWaveNumber();
				createNextWave();
			}
		} else {
			if (lWaveSpawner.isFinishedSpawning() == false) {
				lWaveSpawner.update(pCore);
			}

			if (lWaveSpawner.isFinishedSpawning()) {
				mGameStateController.setWaveHasFinishedSpawning();

				if (mCurrentWave != null && mCurrentWave.isWaveComplete()) {
					mCurrentWave = null;
					mTimerBetweenWaves = TIME_BETWEEN_WAVES;
				}
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
	// Methods
	// --------------------------------------

	private boolean mHasGameStarted = false;

	public void startNewGame() {
		mHasGameStarted = true;
		mCurrentWaveNumber = mGameStateController.currentWaveNumber();

		createNextWave();
	}

	private void createNextWave() {
		mCurrentWave = mWaveManager.getFreePooledItem();
		final var lSpawner = mWaveManager.waveSpawner();

		lSpawner.addSpawnItem(700, -200, 2000, "");

		Debug.debugManager().logger().i(getClass().getSimpleName(), "Creating Wave #" + mCurrentWaveNumber);
		switch (mCurrentWaveNumber) {
		case 0:
			createNewWave00(lSpawner);
			return;
		case 1:
			createNewWave01(lSpawner);
			return;

		case 2:
			createNewWave02(lSpawner);
			return;

		case 3:
			createNewWave04(lSpawner);
			return;

		case 4:
			createNewWave04(lSpawner);
			return;

		default:
			mHaveAllWavesFinished = true;
		}
	}

	// WAVES --------------------------------

	// DONE
	private void createNewWave00(WaveSpawner pWaveSpawner) {
		pWaveSpawner.addSpawnItem(700, -200, 0, MobDefEnemyMine.MOB_DEFINITION_NAME);
		pWaveSpawner.addSpawnItem(700, 200, 0, MobDefEnemyMine.MOB_DEFINITION_NAME);
		pWaveSpawner.addSpawnItem(800, 200, 0, MobDefEnemyTurret.MOB_DEFINITION_NAME);
		pWaveSpawner.addSpawnItem(1100, 200, 0, MobDefEnemyTurret.MOB_DEFINITION_NAME);
		pWaveSpawner.addSpawnItem(1400, 200, 0, MobDefEnemyTurret.MOB_DEFINITION_NAME);
	}

	// DONE
	private void createNewWave01(WaveSpawner pWaveSpawner) {
		createMineWall(pWaveSpawner, 700);
		createMineWall(pWaveSpawner, 1100);
		createMineWall(pWaveSpawner, 1500);

		pWaveSpawner.addSpawnItem(900, 0, 2000, MobDefEnemyBoatStraight.MOB_DEFINITION_NAME);
		pWaveSpawner.addSpawnItem(1300, 0, 2000, MobDefEnemyBoatStraight.MOB_DEFINITION_NAME);
		pWaveSpawner.addSpawnItem(1900, 0, 2000, MobDefEnemyTurretBoatStop.MOB_DEFINITION_NAME);
	}

	private void createNewWave02(WaveSpawner pWaveSpawner) {
		for (int i = 0; i < 5; i++) {
			pWaveSpawner.addSpawnItem(700 + (i * 150), 120 + (float) Math.sin(i) * 150, 0, MobDefEnemySubmarineStraight.MOB_DEFINITION_NAME);
		}

		pWaveSpawner.addSpawnItem(1000, 0, 200, MobDefEnemyTurretBoatStop.MOB_DEFINITION_NAME);
		pWaveSpawner.addSpawnItem(1700, 0, 200, MobDefEnemyTurretBoatStop.MOB_DEFINITION_NAME);
		pWaveSpawner.addSpawnItem(2400, 0, 200, MobDefEnemyTurretBoatStop.MOB_DEFINITION_NAME);

		pWaveSpawner.addSpawnItem(700, 0, 200, MobDefEnemyTurret.MOB_DEFINITION_NAME);
		pWaveSpawner.addSpawnItem(1500, 0, 200, MobDefEnemyTurret.MOB_DEFINITION_NAME);
		pWaveSpawner.addSpawnItem(3000, 0, 200, MobDefEnemyTurret.MOB_DEFINITION_NAME);

		for (int i = 0; i < 5; i++) {
			pWaveSpawner.addSpawnItem(2500 + (i * 150), -50 + (float) Math.sin(i) * 100, 0, MobDefEnemySubmarineStraight.MOB_DEFINITION_NAME);
		}

	}

	private void createNewWave03(WaveSpawner pWaveSpawner) {

	}

	private void createNewWave04(WaveSpawner pWaveSpawner) {
		final float lTimeMod = 3.0f;
		pWaveSpawner.addSpawnItem(0, 0, TIME_BETWEEN_WAVES, null);

		pWaveSpawner.addSpawnItem(700, 0, 200 * lTimeMod, MobDefEnemyTurret.MOB_DEFINITION_NAME);

		pWaveSpawner.addSpawnItem(700, 0, 200 * lTimeMod, MobDefEnemySubmarineStraight.MOB_DEFINITION_NAME);

		pWaveSpawner.addSpawnItem(700, 0, 200 * lTimeMod, MobDefEnemyTurretBoatStop.MOB_DEFINITION_NAME);

		pWaveSpawner.addSpawnItem(700, -150, 0 * lTimeMod, MobDefEnemyMine.MOB_DEFINITION_NAME);
		pWaveSpawner.addSpawnItem(700, +150, 1000 * lTimeMod, MobDefEnemyMine.MOB_DEFINITION_NAME);
		pWaveSpawner.addSpawnItem(700, 0, 200 * lTimeMod, MobDefEnemyMine.MOB_DEFINITION_NAME);

		pWaveSpawner.addSpawnItem(700, 0, 2500 * lTimeMod, MobDefEnemyTurret.MOB_DEFINITION_NAME);
		pWaveSpawner.addSpawnItem(700, 0, 200 * lTimeMod, MobDefEnemyTurret.MOB_DEFINITION_NAME);

		pWaveSpawner.addSpawnItem(700, -100, 2000 * lTimeMod, MobDefEnemySubmarineStraight.MOB_DEFINITION_NAME);
		pWaveSpawner.addSpawnItem(700, 0, 2000 * lTimeMod, MobDefEnemySubmarineStraight.MOB_DEFINITION_NAME);
		pWaveSpawner.addSpawnItem(700, 100, 2000 * lTimeMod, MobDefEnemySubmarineStraight.MOB_DEFINITION_NAME);
		pWaveSpawner.addSpawnItem(700, 200, 2000 * lTimeMod, MobDefEnemySubmarineStraight.MOB_DEFINITION_NAME);

		pWaveSpawner.addSpawnItem(700, 0, 200 * lTimeMod, MobDefEnemyTurretBoatStop.MOB_DEFINITION_NAME);

		pWaveSpawner.addSpawnItem(700, 0, 2500 * lTimeMod, MobDefEnemyTurret.MOB_DEFINITION_NAME);
		pWaveSpawner.addSpawnItem(700, 0, 200 * lTimeMod, MobDefEnemyTurret.MOB_DEFINITION_NAME);

		pWaveSpawner.addSpawnItem(700, -150, 0 * lTimeMod, MobDefEnemyMine.MOB_DEFINITION_NAME);
		pWaveSpawner.addSpawnItem(700, +150, 1000 * lTimeMod, MobDefEnemyMine.MOB_DEFINITION_NAME);
		pWaveSpawner.addSpawnItem(700, 0, 200 * lTimeMod, MobDefEnemyMine.MOB_DEFINITION_NAME);

		pWaveSpawner.addSpawnItem(700, 0, 200 * lTimeMod, MobDefEnemyTurretBoatStop.MOB_DEFINITION_NAME);

		pWaveSpawner.addSpawnItem(700, -100, 2000 * lTimeMod, MobDefEnemySubmarineStraight.MOB_DEFINITION_NAME);
		pWaveSpawner.addSpawnItem(700, 0, 2000 * lTimeMod, MobDefEnemySubmarineStraight.MOB_DEFINITION_NAME);
		pWaveSpawner.addSpawnItem(700, 100, 2000 * lTimeMod, MobDefEnemySubmarineStraight.MOB_DEFINITION_NAME);
		pWaveSpawner.addSpawnItem(700, 200, 2000 * lTimeMod, MobDefEnemySubmarineStraight.MOB_DEFINITION_NAME);
	}

	// SET PIECES ---------------------------

	private void createMineField(WaveSpawner pWaveSpawner) {
		createMineWall(pWaveSpawner, 700);
		createMineWall(pWaveSpawner, 900);
		createMineWall(pWaveSpawner, 1100);
		createMineWall(pWaveSpawner, 1300);
	}

	private void createBombingRun(WaveSpawner pWaveSpawner) {
		pWaveSpawner.addSpawnItem(600, 0, 1000, MobDefEnemyBoatStraight.MOB_DEFINITION_NAME);
		pWaveSpawner.addSpawnItem(600, 0, 1000, MobDefEnemyBoatStraight.MOB_DEFINITION_NAME);
	}

	private void createMineWall(WaveSpawner pWaveSpawner, float pWorldX) {
		pWaveSpawner.addSpawnItem(pWorldX, -145, 0, MobDefEnemyMine.MOB_DEFINITION_NAME);
		pWaveSpawner.addSpawnItem(pWorldX, 65, 0, MobDefEnemyMine.MOB_DEFINITION_NAME);
		pWaveSpawner.addSpawnItem(pWorldX, 265, 0000, MobDefEnemyMine.MOB_DEFINITION_NAME);
	}

	private void createTurretVolley(WaveSpawner pWaveSpawner) {
		pWaveSpawner.addSpawnItem(700, 0, 1500, MobDefEnemyTurret.MOB_DEFINITION_NAME);
		pWaveSpawner.addSpawnItem(700, 0, 1500, MobDefEnemyTurret.MOB_DEFINITION_NAME);
		pWaveSpawner.addSpawnItem(700, 0, 1500, MobDefEnemyBoatStraight.MOB_DEFINITION_NAME);
	}

}
