package net.ld.unstable.controllers;

import net.ld.unstable.data.mobs.MobManager;
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
		for (int i = 0; i < lMobCount; i++) {
			final var lMobInstance = lMobs.get(i);

			final var lSubmarineSpriteGraph = lMobInstance.spriteGraphInstance();

			lSubmarineSpriteGraph.positionX = lMobInstance.x;
			lSubmarineSpriteGraph.positionY = lMobInstance.y;
			lSubmarineSpriteGraph.rotationInRadians = 0.f;
			lSubmarineSpriteGraph.mFlipHorizontal = lMobInstance.isPlayerControlled == false;
			lSubmarineSpriteGraph.update(pCore);

			final var lEmitter = lMobInstance.bubbleEmitter;
			if (lEmitter != null) {
				lEmitter.worldPositionX = lMobInstance.x;
				lEmitter.worldPositionY = lMobInstance.y;
			}
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
