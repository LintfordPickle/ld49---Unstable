package net.ld.unstable.controllers;

import java.util.ArrayList;
import java.util.List;

import net.ld.unstable.data.mobs.MobManager;
import net.ld.unstable.data.mobs.Submarine;
import net.ld.unstable.data.mobs.attachments.PowerCoreAttachment;
import net.ld.unstable.data.mobs.attachments.PropellerAttachment;
import net.ld.unstable.data.mobs.attachments.SubAttachment;
import net.ld.unstable.data.mobs.definitions.EnemySubmarineStraight00;
import net.ld.unstable.data.mobs.patterns.CosMover;
import net.ld.unstable.data.mobs.patterns.StraightMover;
import net.lintford.library.controllers.BaseController;
import net.lintford.library.controllers.core.ControllerManager;
import net.lintford.library.controllers.core.particles.ParticleFrameworkController;
import net.lintford.library.controllers.geometry.SpriteGraphController;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.debug.Debug;
import net.lintford.library.core.maths.RandomNumbers;

public class SubController extends BaseController {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String CONTROLLER_NAME = "Submarine Controller";

	public final static StraightMover StraightPattern = new StraightMover();
	public final static CosMover CosMover = new CosMover();

	// --------------------------------------
	// Variables
	// --------------------------------------

	private ProjectileController mProjectileController;
	private ParticleFrameworkController mParticleFrameworkController;
	private SpriteGraphController mSpriteGraphController;
	private final MobManager mMobManager;

	private List<Submarine> mUpdateMobList = new ArrayList<>();

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

	public SubController(ControllerManager pControllerManager, MobManager pMobManager, int pEntityGroupID) {
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
		mProjectileController = (ProjectileController) lControllerManager.getControllerByNameRequired(ProjectileController.CONTROLLER_NAME, entityGroupID());
		mParticleFrameworkController = (ParticleFrameworkController) lControllerManager.getControllerByNameRequired(ParticleFrameworkController.CONTROLLER_NAME, entityGroupID());
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

			if (lMobInstance.worldPositionX + 200.0f < pCore.gameCamera().boundingRectangle().left()) {
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

		if (lMobCount < 3) {
			addNewSubmarine(false, EnemySubmarineStraight00.MOB_DEFINITION_NAME, 600, RandomNumbers.random(-10.f, 300.0f));
		}

	}

	private void updateSubmarine(LintfordCore pCore, Submarine pSubmarine) {
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

		// ---
		if (pSubmarine.isPlayerControlled == false) {
			if (pSubmarine.shootTimer <= 0.f) {
				pSubmarine.shootTimer -= pCore.gameTime().elapsedTimeMilli();
				mProjectileController.shootTorpedo(pSubmarine.uid, pSubmarine.worldPositionX, pSubmarine.worldPositionY, -1, 0);
				pSubmarine.shootTimer = 350.f;
			}
		}
		// ---

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

	private static int subCounter = 0;

	public void addNewSubmarine(boolean pPlayerControlled, String pDefinitionName, float pWorldX, float pWorldY) {
		final var lNewMobDefinition = mMobManager.mobDefinitionManager().getMobDefinitionByName(pDefinitionName);

		if (lNewMobDefinition == null) {
			Debug.debugManager().logger().e(getClass().getSimpleName(), "Couldn't find mob definition with name : " + pDefinitionName);
			return;
		}
		final var lNewMobInstance = mMobManager.addNewMob(lNewMobDefinition, pWorldX, pWorldY);
		lNewMobInstance.init(subCounter++);

		final var lSpriteGraphInstance = mSpriteGraphController.getSpriteGraphInstance(lNewMobDefinition.SpritegraphName, entityGroupID());

		lSpriteGraphInstance.animatedSpriteGraphListener(lNewMobInstance);
		lNewMobInstance.setSpriteGraphInstance(lSpriteGraphInstance);
		final var lCurrentAnimationName = "normal";
		lSpriteGraphInstance.currentAnimation(lCurrentAnimationName);

		lSpriteGraphInstance.attachItemToNode(new SubAttachment());
		lSpriteGraphInstance.attachItemToNode(new PropellerAttachment());
		lSpriteGraphInstance.mFlipHorizontal = lNewMobInstance.isPlayerControlled == false;

		lNewMobInstance.health = lNewMobDefinition.maxHealth;
		lNewMobInstance.isPlayerControlled = false;
		if (pPlayerControlled) {
			lNewMobInstance.isPlayerControlled = true;
			mMobManager.playerSubmarine = lNewMobInstance;
			lSpriteGraphInstance.attachItemToNode(new PowerCoreAttachment());
		} else {

			lNewMobInstance.movementPattern = CosMover;
		}

		// particles
		{
			final var lBubbleEmitter = mParticleFrameworkController.particleFrameworkData().emitterManager().getNewParticleEmitterInstanceByDefName("EMITTER_BUBBLE");
			lNewMobInstance.bubbleEmitter = lBubbleEmitter;
		}
	}
}
