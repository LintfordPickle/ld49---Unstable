package net.ld.unstable.controllers;

import java.util.ArrayList;
import java.util.List;

import net.ld.unstable.data.mobs.MobManager;
import net.ld.unstable.data.mobs.SmhupMob;
import net.ld.unstable.data.mobs.movementpatterns.MovingDefCosineMover;
import net.ld.unstable.data.mobs.movementpatterns.MovingDefEnemyMine;
import net.ld.unstable.data.mobs.movementpatterns.MovingDefStraightMover;
import net.ld.unstable.data.mobs.movementpatterns.MovingDefSurfaceMover;
import net.ld.unstable.data.mobs.movementpatterns.MovingDefSurfaceMoverWithStop;
import net.ld.unstable.data.mobs.movementpatterns.MovingDefTurret;
import net.lintford.library.controllers.BaseController;
import net.lintford.library.controllers.core.ControllerManager;
import net.lintford.library.controllers.core.particles.ParticleFrameworkController;
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

	// --------------------------------------
	// Variables
	// --------------------------------------

	private ProjectileController mProjectileController;
	private ParticleFrameworkController mParticleFrameworkController;
	private SpriteGraphController mSpriteGraphController;
	private LevelController mLevelController;
	private final MobManager mMobManager;
	private final List<SmhupMob> mUpdateMobList = new ArrayList<>();

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
		mParticleFrameworkController = (ParticleFrameworkController) lControllerManager.getControllerByNameRequired(ParticleFrameworkController.CONTROLLER_NAME, entityGroupID());

		final float lFloorLevel = pCore.gameCamera().boundingRectangle().bottom();

		turretMovementDef = new MovingDefTurret(lFloorLevel);
		StraightPattern = new MovingDefStraightMover();
		CosMover = new MovingDefCosineMover();
		surfaceMover = new MovingDefSurfaceMover(mLevelController.seaLevel());
		surfaceMoveWithStop = new MovingDefSurfaceMoverWithStop(mLevelController.seaLevel());
		movingDefEnemyMine = new MovingDefEnemyMine();
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

		mUpdateMobList.clear();
		for (int i = 0; i < lMobCount; i++) {
			mUpdateMobList.add(lMobs.get(i));
		}

		for (int i = 0; i < lMobCount; i++) {
			final var lMobInstance = mUpdateMobList.get(i);

			final float lDespawnTol = 75.f;
			if (!lMobInstance.isPlayerControlled && lMobInstance.worldPositionX + lDespawnTol < pCore.gameCamera().boundingRectangle().left()) {
				Debug.debugManager().logger().i(getClass().getSimpleName(), "Despawn mob");

				lMobInstance.kill();
				lMobs.remove(lMobInstance);

				mParticleFrameworkController.particleFrameworkData().emitterManager().returnPooledItem(lMobInstance.bubbleEmitter);
				lMobInstance.bubbleEmitter = null;
				continue;
			}

			if (lMobInstance.health < 0.f) {
				lMobInstance.kill();
				lMobs.remove(lMobInstance);

				mParticleFrameworkController.particleFrameworkData().emitterManager().returnPooledItem(lMobInstance.bubbleEmitter);
				lMobInstance.bubbleEmitter = null;

				// TODO: explosions

				continue;
			}

			updateSubmarine(pCore, lMobInstance);

			if (lMobInstance.isPlayerControlled == false) {
				if (lMobInstance.movementPattern != null) {
					lMobInstance.movementPattern.update(pCore, lMobInstance);
				}
			}
		}
	}

	private void updateSubmarine(LintfordCore pCore, SmhupMob pSubmarine) {
		final var lSubmarineSpriteGraph = pSubmarine.spriteGraphInstance();

		pSubmarine.timeSinceStart += pCore.gameTime().elapsedTimeMilli();

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

		if (pSubmarine.shootingPattern != null) {
			pSubmarine.shootingPattern.update(pCore, pSubmarine, mProjectileController);
		}

		lSubmarineSpriteGraph.positionX = pSubmarine.worldPositionX;
		lSubmarineSpriteGraph.positionY = pSubmarine.worldPositionY;
		lSubmarineSpriteGraph.rotationInRadians = 0.f;
		lSubmarineSpriteGraph.mFlipHorizontal = pSubmarine.isPlayerControlled == false;
		lSubmarineSpriteGraph.update(pCore);
		pSubmarine.spriteGraphDirty = false;

		final var lEmitter = pSubmarine.bubbleEmitter;
		if (lEmitter != null) {
			lEmitter.worldPositionX = pSubmarine.worldPositionX - 50.f;
			lEmitter.worldPositionY = pSubmarine.worldPositionY;
		}
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	public SmhupMob addNewMob(boolean pPlayerControlled, String pDefinitionName, float pScreenX, float pScreenY) {
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

		lNewMobInstance.health = lNewMobDefinition.maxHealth;
		lNewMobInstance.isPlayerControlled = false;
		if (pPlayerControlled) {
			lNewMobInstance.isPlayerControlled = true;
			mMobManager.playerSubmarine = lNewMobInstance;
		}

		{
			final var lBubbleEmitter = mParticleFrameworkController.particleFrameworkData().emitterManager().getNewParticleEmitterInstanceByDefName("EMITTER_BUBBLE");
			lNewMobInstance.bubbleEmitter = lBubbleEmitter;
		}

		return lNewMobInstance;
	}
}
