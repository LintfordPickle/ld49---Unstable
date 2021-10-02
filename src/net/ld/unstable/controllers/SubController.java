package net.ld.unstable.controllers;

import java.util.ArrayList;
import java.util.List;

import net.ld.unstable.data.mobs.MobManager;
import net.ld.unstable.data.mobs.Submarine;
import net.ld.unstable.data.mobs.attachments.PowerCoreAttachment;
import net.ld.unstable.data.mobs.attachments.PropellerAttachment;
import net.ld.unstable.data.mobs.attachments.SubAttachment;
import net.lintford.library.controllers.BaseController;
import net.lintford.library.controllers.core.ControllerManager;
import net.lintford.library.controllers.core.particles.ParticleFrameworkController;
import net.lintford.library.controllers.geometry.SpriteGraphController;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.debug.Debug;

public class SubController extends BaseController {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String CONTROLLER_NAME = "Submarine Controller";

	// --------------------------------------
	// Variables
	// --------------------------------------

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

			if (lMobInstance.health < 0.f) {
				lMobs.remove(lMobInstance);

				mParticleFrameworkController.particleFrameworkData().emitterManager().returnPooledItem(lMobInstance.bubbleEmitter);
				lMobInstance.bubbleEmitter = null;

				// TODO: explosions

				continue;
			}

			updateSubmarine(pCore, lMobInstance);
		}
	}

	private void updateSubmarine(LintfordCore pCore, Submarine pSubmarine) {
		final var lSubmarineSpriteGraph = pSubmarine.spriteGraphInstance();

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

		lSubmarineSpriteGraph.positionX = pSubmarine.x;
		lSubmarineSpriteGraph.positionY = pSubmarine.y;
		lSubmarineSpriteGraph.rotationInRadians = 0.f;
		lSubmarineSpriteGraph.mFlipHorizontal = pSubmarine.isPlayerControlled == false;
		lSubmarineSpriteGraph.update(pCore);

		final var lEmitter = pSubmarine.bubbleEmitter;
		if (lEmitter != null) {
			lEmitter.worldPositionX = pSubmarine.x - 50.f;
			lEmitter.worldPositionY = pSubmarine.y;
		}
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	public void addNewSubmarine(boolean pPlayerControlled, String pDefinitionName, float pWorldX, float pWorldY) {
		final var lNewMobDefinition = mMobManager.mobDefinitionManager().getMobDefinitionByName(pDefinitionName);

		if (lNewMobDefinition == null) {
			Debug.debugManager().logger().e(getClass().getSimpleName(), "Couldn't find mob definition with name : " + pDefinitionName);
			return;
		}
		final var lNewMobInstance = mMobManager.addNewMob(lNewMobDefinition, pWorldX, pWorldY);
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
		}

		// particles
		{
			final var lBubbleEmitter = mParticleFrameworkController.particleFrameworkData().emitterManager().getNewParticleEmitterInstanceByDefName("EMITTER_BUBBLE");
			lNewMobInstance.bubbleEmitter = lBubbleEmitter;
		}

	}
}
