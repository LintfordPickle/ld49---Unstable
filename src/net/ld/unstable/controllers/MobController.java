package net.ld.unstable.controllers;

import java.util.ArrayList;
import java.util.List;

import net.ld.unstable.ConstantsGame;
import net.ld.unstable.data.explosions.ExplosionsController;
import net.ld.unstable.data.mobs.MobManager;
import net.ld.unstable.data.mobs.ShmupMob;
import net.ld.unstable.data.mobs.definitions.MobDefEnemyMine;
import net.ld.unstable.data.mobs.movementpatterns.MovingDefCosineMover;
import net.ld.unstable.data.mobs.movementpatterns.MovingDefEnemyMine;
import net.ld.unstable.data.mobs.movementpatterns.MovingDefEnemySubStop;
import net.ld.unstable.data.mobs.movementpatterns.MovingDefStraightMover;
import net.ld.unstable.data.mobs.movementpatterns.MovingDefSurfaceMover;
import net.ld.unstable.data.mobs.movementpatterns.MovingDefSurfaceMoverWithStop;
import net.ld.unstable.data.mobs.movementpatterns.MovingDefTurret;
import net.lintford.library.controllers.BaseController;
import net.lintford.library.controllers.core.ControllerManager;
import net.lintford.library.controllers.geometry.SpriteGraphController;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.debug.Debug;

public class MobController extends BaseController {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String CONTROLLER_NAME = "Mobs Controller";

	public static MovingDefTurret turretMovementDef;
	public static MovingDefStraightMover StraightPattern;
	public static MovingDefCosineMover CosMover;
	public static MovingDefSurfaceMover surfaceMover;
	public static MovingDefSurfaceMoverWithStop surfaceMoveWithStop;
	public static MovingDefEnemyMine movingDefEnemyMine;
	public static MovingDefEnemySubStop movingDefEnemySubStop;

	// --------------------------------------
	// Variables
	// --------------------------------------

	private ExplosionsController mExplosionController;
	private ProjectileController mProjectileController;
	private SpriteGraphController mSpriteGraphController;
	private LevelController mLevelController;
	private GameStateController mGameStateController;
	private ScreenShakeController mScreenShakeController;

	private final MobManager mMobManager;
	private final List<ShmupMob> mUpdateMobList = new ArrayList<>();

	private int mCurrentWaveNumber = 0;

	// --------------------------------------
	// Properties
	// --------------------------------------

	public MobManager mobManager() {
		return mMobManager;
	}

	@Override
	public boolean isInitialized() {
		// TODO Auto-generated method stub
		return false;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public MobController(ControllerManager pControllerManager, MobManager pMobManager, int pEntityGroupID) {
		super(pControllerManager, CONTROLLER_NAME, pEntityGroupID);

		mMobManager = pMobManager;
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void initialize(LintfordCore pCore) {
		final var lControllerManager = pCore.controllerManager();
		mSpriteGraphController = (SpriteGraphController) lControllerManager.getControllerByNameRequired(SpriteGraphController.CONTROLLER_NAME, entityGroupID());
		mLevelController = (LevelController) lControllerManager.getControllerByNameRequired(LevelController.CONTROLLER_NAME, entityGroupID());
		mProjectileController = (ProjectileController) lControllerManager.getControllerByNameRequired(ProjectileController.CONTROLLER_NAME, entityGroupID());
		mExplosionController = (ExplosionsController) lControllerManager.getControllerByNameRequired(ExplosionsController.CONTROLLER_NAME, entityGroupID());
		mGameStateController = (GameStateController) lControllerManager.getControllerByNameRequired(GameStateController.CONTROLLER_NAME, entityGroupID());
		mScreenShakeController = (ScreenShakeController) lControllerManager.getControllerByNameRequired(ScreenShakeController.CONTROLLER_NAME, entityGroupID());

		final float lFloorLevel = pCore.gameCamera().getPosition().y + ConstantsGame.WINDOW_HEIGHT * .5f;

		turretMovementDef = new MovingDefTurret(lFloorLevel);
		StraightPattern = new MovingDefStraightMover();
		CosMover = new MovingDefCosineMover(mLevelController.seaLevel());
		surfaceMover = new MovingDefSurfaceMover(mLevelController.seaLevel());
		surfaceMoveWithStop = new MovingDefSurfaceMoverWithStop(mLevelController.seaLevel());
		movingDefEnemyMine = new MovingDefEnemyMine(mLevelController.seaLevel());
		movingDefEnemySubStop = new MovingDefEnemySubStop(mLevelController.seaLevel());
	}

	@Override
	public void unload() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(LintfordCore pCore) {
		super.update(pCore);

		final var lMobs = mMobManager.mobs();
		final int lMobCount = lMobs.size();

		updatePlayerSubmarineCollisions(pCore, mMobManager.playerSubmarine);

		mUpdateMobList.clear();
		for (int i = 0; i < lMobCount; i++) {
			mUpdateMobList.add(lMobs.get(i));
		}

		for (int i = 0; i < lMobCount; i++) {
			final var lMobInstance = mUpdateMobList.get(i);

			if (lMobInstance.mobDefinition == null) {
				lMobInstance.kill();
				lMobs.remove(lMobInstance);
				continue;
			}

			updateSubmarine(pCore, lMobInstance);

			if (lMobInstance.isPlayerControlled)
				continue;

			final float lDespawnTol = 200.f;
			final float lLeftOfScreen = pCore.gameCamera().getPosition().x - ConstantsGame.WINDOW_WIDTH * .5f;
			if (lMobInstance.worldPositionX + lDespawnTol < lLeftOfScreen) {
				Debug.debugManager().logger().i(getClass().getSimpleName(), "Despawn mob: " + lMobInstance.mobDefinition.getClass().getSimpleName());

				lMobInstance.kill();
				lMobs.remove(lMobInstance);
				continue;

			}

			if (lMobInstance.health < 0.f) {
				if (lMobInstance.mobDefinition.underwaterCraft) {
					mExplosionController.addWaterExplosion(lMobInstance.baseWorldPositionX, lMobInstance.baseWorldPositionY);
				} else {
					mExplosionController.addMajorExplosion(lMobInstance.baseWorldPositionX, lMobInstance.baseWorldPositionY);
				}

				mGameStateController.increaseScore(50);

				lMobInstance.kill();
				lMobs.remove(lMobInstance);

				continue;
			}

			if (lMobInstance.isPlayerControlled == false) {
				if (lMobInstance.movementPattern != null) {
					lMobInstance.movementPattern.update(pCore, lMobInstance);
				}
			}
		}

		if (mGameStateController.hasWaveFinishedSpawning() && mCurrentWaveNumber == mGameStateController.currentWaveNumber() && lMobCount <= 1) {
			Debug.debugManager().logger().i(getClass().getSimpleName(), "All mobs killed (#" + mCurrentWaveNumber + ")");
			mGameStateController.waveComplete();
			mCurrentWaveNumber = mGameStateController.currentWaveNumber();
		}

	}

	private void updatePlayerSubmarineCollisions(LintfordCore pCore, ShmupMob pPlayerSub) {
		final var lMobs = mMobManager.mobs();
		final int lMobCount = lMobs.size();

		for (int i = 0; i < lMobCount; i++) {
			final var lMobB = lMobs.get(i);

			if (lMobB.isAlive == false || lMobB.mobDefinition == null)
				continue;

			if (lMobB.mobDefinition instanceof MobDefEnemyMine) {
				if (pPlayerSub.collides(lMobB.worldPositionX, lMobB.worldPositionY, lMobB.mobDefinition.collisionRadius)) {

					mScreenShakeController.shakeCamera(350.f, 20.f);

					mExplosionController.addMajorExplosion(lMobB.worldPositionX, lMobB.worldPositionY);

					pPlayerSub.dealDamage(50, 80);
					lMobB.kill();
				}
			}
		}
	}

	private void updateSubmarine(LintfordCore pCore, ShmupMob pSubmarine) {
		if (pSubmarine.isAlive == false)
			return;

		pSubmarine.timeSinceStart += pCore.gameTime().elapsedTimeMilli();

		if (pSubmarine.coolantTimer > 0.f)
			pSubmarine.coolantTimer -= pCore.gameTime().elapsedTimeMilli();

		if (pSubmarine.shootTimer > 0.f)
			pSubmarine.shootTimer -= pCore.gameTime().elapsedTimeMilli();

		if (pSubmarine.barrelTimer > 0.f)
			pSubmarine.barrelTimer -= pCore.gameTime().elapsedTimeMilli();

		if (pSubmarine.missileTimer > 0.f)
			pSubmarine.missileTimer -= pCore.gameTime().elapsedTimeMilli();

		//
		if (pSubmarine.invulnerabilityTimer > .0f) {
			pSubmarine.invulnerabilityTimer -= pCore.gameTime().elapsedTimeMilli();
			pSubmarine.flashTimer -= pCore.gameTime().elapsedTimeMilli();
			if (pSubmarine.flashTimer < 0.f) {
				pSubmarine.flashTimer = 50.f;
				pSubmarine.flashOn = !pSubmarine.flashOn;
			}
		} else {
			pSubmarine.flashOn = false;
		}

		if (pSubmarine.coolant < pSubmarine.mobDefinition.maxCoolant) {
			if (pSubmarine.coolantTimer <= 0.f) {
				pSubmarine.coolant += 5;
				pSubmarine.coolantTimer = 50.f;
			}
		}

		if (pSubmarine.shootingPattern != null) {
			pSubmarine.shootingPattern.update(pCore, pSubmarine, mProjectileController);
		}

		final var lSubmarineSpriteGraph = pSubmarine.spriteGraphInstance();

		lSubmarineSpriteGraph.positionX = pSubmarine.worldPositionX;
		lSubmarineSpriteGraph.positionY = pSubmarine.worldPositionY;
		lSubmarineSpriteGraph.rotationInRadians = 0.f;
		lSubmarineSpriteGraph.mFlipHorizontal = pSubmarine.isPlayerControlled == false;
		lSubmarineSpriteGraph.update(pCore);
		pSubmarine.spriteGraphDirty = false;
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	public void startNewGame() {
		mCurrentWaveNumber = mGameStateController.currentWaveNumber();
	}

	public ShmupMob addNewMob(boolean pPlayerControlled, String pDefinitionName, float pScreenX, float pScreenY) {
		final var lNewMobDefinition = mMobManager.mobDefinitionManager().getMobDefinitionByName(pDefinitionName);

		if (lNewMobDefinition == null) {
			Debug.debugManager().logger().e(getClass().getSimpleName(), "Couldn't find mob definition with name : " + pDefinitionName);
			return null;
		}

		final float lWorldPositionX = mLevelController.worldPositionX();
		final float lWorldPositionY = mLevelController.worldPositionY();

		final var lNewMobInstance = mMobManager.addNewMob(lNewMobDefinition, pScreenX + lWorldPositionX, pScreenY + lWorldPositionY);

		final int lShooterUid = pPlayerControlled ? ProjectileController.PLAYER_BULLETS_UID : ProjectileController.ENEMY_BULLETS_UID;
		lNewMobInstance.init(lShooterUid, lNewMobDefinition);

		final var lSpriteGraphInstance = mSpriteGraphController.getSpriteGraphInstance(lNewMobDefinition.SpritegraphName, entityGroupID());

		lSpriteGraphInstance.animatedSpriteGraphListener(lNewMobInstance);
		lNewMobInstance.setSpriteGraphInstance(lSpriteGraphInstance);
		final var lCurrentAnimationName = "normal";
		lSpriteGraphInstance.currentAnimation(lCurrentAnimationName);

		lNewMobDefinition.AttachMovementPattern(lNewMobInstance);
		lNewMobDefinition.AttachShootingPattern(lNewMobInstance);
		lNewMobDefinition.AttachSpriteGraphStuff(lNewMobInstance, lSpriteGraphInstance);

		lSpriteGraphInstance.mFlipHorizontal = lNewMobInstance.isPlayerControlled == false;

		lNewMobInstance.health = (int) lNewMobDefinition.maxHealth;
		lNewMobInstance.coolant = (int) lNewMobDefinition.maxCoolant;

		lNewMobInstance.isPlayerControlled = false;
		if (pPlayerControlled) {
			lNewMobInstance.isPlayerControlled = true;
			mMobManager.playerSubmarine = lNewMobInstance;
		}

		return lNewMobInstance;
	}
}
