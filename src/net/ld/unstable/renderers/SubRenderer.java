package net.ld.unstable.renderers;

import net.ld.unstable.controllers.SubController;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.debug.Debug;
import net.lintford.library.core.graphics.sprites.spritegraph.SpriteGraphRenderer;
import net.lintford.library.core.graphics.sprites.spritesheet.SpriteSheetDefinition;
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
	private SpriteSheetDefinition mMobSpritesheet;
	private SpriteGraphRenderer mSpriteGraphRenderer;

	// --------------------------------------
	// Properties
	// --------------------------------------

	@Override
	public boolean isInitialized() {
		return mSubController != null;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public SubRenderer(RendererManager pRendererManager, int pEntityGroupID) {
		super(pRendererManager, RENDERER_NAME, pEntityGroupID);

		mSpriteGraphRenderer = new SpriteGraphRenderer();
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

		mSpriteGraphRenderer.loadGLContent(pResourceManager);
		mMobSpritesheet = pResourceManager.spriteSheetManager().loadSpriteSheet("res/spritesheets/spritesheetSubmarines.json", entityGroupID());
	}

	@Override
	public void unloadGLContent() {
		super.unloadGLContent();

		mSpriteGraphRenderer.unloadGLContent();
		mMobSpritesheet = null;
	}

	@Override
	public void draw(LintfordCore pCore) {
		if (!isInitialized())
			return;

		final var lMobManager = mSubController.mobManager();
		final var lPlayerSubmarine = lMobManager.getPlayerSubmarine();

		final var lWidth = 150.f;
		final var lHeight = 90.f;

		final float lSubmarinePositionX = lPlayerSubmarine.x - lWidth * .5f;
		final float lSubmarinePositionY = lPlayerSubmarine.y - lHeight * .5f;

		Debug.debugManager().drawers().drawRectImmediate(pCore.gameCamera(), lSubmarinePositionX, lSubmarinePositionY, lWidth, lHeight, 2.f, 1.f, 1.f, 0.f);

		final var lPlayerSub = lMobManager.getPlayerSubmarine();
		lPlayerSub.spriteGraphInstance().update(pCore);

		mSpriteGraphRenderer.begin(pCore.gameCamera());
		mSpriteGraphRenderer.drawSpriteGraphList(pCore, lPlayerSub.spriteGraphInstance());
		mSpriteGraphRenderer.end();

	}
}
