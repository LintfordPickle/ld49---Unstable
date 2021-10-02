package net.ld.unstable.controllers;

import net.ld.unstable.data.mobs.MobManager;
import net.ld.unstable.data.mobs.attachments.PowerCoreAttachment;
import net.ld.unstable.data.mobs.attachments.PropellerAttachment;
import net.ld.unstable.data.mobs.attachments.SubAttachment;
import net.lintford.library.controllers.BaseController;
import net.lintford.library.controllers.core.ControllerManager;
import net.lintford.library.controllers.geometry.SpriteGraphController;
import net.lintford.library.core.LintfordCore;

public class SubController extends BaseController {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String CONTROLLER_NAME = "Submarine Controller";

	// --------------------------------------
	// Variables
	// --------------------------------------

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
		final var lSpriteGraphController = (SpriteGraphController) lControllerManager.getControllerByNameRequired(SpriteGraphController.CONTROLLER_NAME, entityGroupID());
		final var lSpriteGraphInstance = lSpriteGraphController.getSpriteGraphInstance("SPRITEGRAPH_SUBMARINE", entityGroupID());

		final var lPlayerSubmarine = mMobManager.getPlayerSubmarine();

		lSpriteGraphInstance.animatedSpriteGraphListener(lPlayerSubmarine);
		lPlayerSubmarine.setSpriteGraphInstance(lSpriteGraphInstance);
		final var lCurrentAnimationName = "normal";
		lSpriteGraphInstance.currentAnimation(lCurrentAnimationName);

		lSpriteGraphInstance.attachItemToNode(new SubAttachment());
		lSpriteGraphInstance.attachItemToNode(new PowerCoreAttachment());
		lSpriteGraphInstance.attachItemToNode(new PropellerAttachment());

	}

	@Override
	public void unload() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(LintfordCore pCore) {
		super.update(pCore);

		final var lPlayerSubmarine = mMobManager.getPlayerSubmarine();
		final var lPlayerSubmarineSpriteGraph = lPlayerSubmarine.spriteGraphInstance();

		lPlayerSubmarineSpriteGraph.positionX = lPlayerSubmarine.x;
		lPlayerSubmarineSpriteGraph.positionY = lPlayerSubmarine.y;
		lPlayerSubmarineSpriteGraph.rotationInRadians = 0.f;

	}

	// --------------------------------------
	// Methods
	// --------------------------------------

}
