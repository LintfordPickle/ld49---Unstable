package net.ld.unstable.renderers;

import net.ld.unstable.controllers.SubController;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.debug.Debug;
import net.lintford.library.renderers.BaseRenderer;
import net.lintford.library.renderers.RendererManager;

public class SubRenderer extends BaseRenderer {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String RENDERER_NAME = "Submarine Renderer";

	// --------------------------------------
	// Variables
	// --------------------------------------

	private SubController mSubController;

	// --------------------------------------
	// Properties
	// --------------------------------------

	@Override
	public boolean isInitialized() {
		return false;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public SubRenderer(RendererManager pRendererManager, int pEntityGroupID) {
		super(pRendererManager, RENDERER_NAME, pEntityGroupID);
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void initialize(LintfordCore pCore) {
		final var lControllerManager = pCore.controllerManager();

		mSubController = (SubController) lControllerManager.getControllerByNameRequired(SubController.CONTROLLER_NAME, entityGroupID());
	}

	@Override
	public void loadGLContent(ResourceManager pResourceManager) {
		super.loadGLContent(pResourceManager);

	}

	@Override
	public void unloadGLContent() {
		super.unloadGLContent();

	}

	@Override
	public void draw(LintfordCore pCore) {

		// Render the player separtely
		final var lMobManager = mSubController.mobManager();
		final var lPlayerSubmarine = lMobManager.getPlayerSubmarine();

		final var lWidth = 40.f;
		final var lHeight = 20.f;

		final float lSubmarinePositionX = lPlayerSubmarine.x - lWidth * .5f;
		final float lSubmarinePositionY = lPlayerSubmarine.y - lHeight * .5f;

		Debug.debugManager().drawers().drawRectImmediate(pCore.gameCamera(), lSubmarinePositionX, lSubmarinePositionY, lWidth, lHeight);

		// Render the other mobs

	}
}
