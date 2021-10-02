package net.ld.unstable.renderers;

import net.ld.unstable.controllers.SubController;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.graphics.ColorConstants;
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
		final var lMobs = lMobManager.mobs();
		final int lMobCount = lMobs.size();
		for (int i = 0; i < lMobCount; i++) {
			final var lMobInstance = lMobs.get(i);

			final var lSubmarineSpritegraph = lMobInstance.spriteGraphInstance();
			if (lSubmarineSpritegraph != null) {
				lMobInstance.spriteGraphInstance().update(pCore);

				final var lCrazyWhite = lMobInstance.flashOn ? ColorConstants.getColor(1000.f, 1000.0f, 1000.f) : ColorConstants.WHITE;

				mSpriteGraphRenderer.begin(pCore.gameCamera());
				mSpriteGraphRenderer.drawSpriteGraphList(pCore, lSubmarineSpritegraph, lCrazyWhite);
				mSpriteGraphRenderer.end();

//				GL11.glLineWidth(2.f);
//				Debug.debugManager().drawers().drawCircleImmediate(pCore.gameCamera(), lMobInstance.x - 25, lMobInstance.y, 25.f);
//				Debug.debugManager().drawers().drawCircleImmediate(pCore.gameCamera(), lMobInstance.x + 25, lMobInstance.y, 25.f);
//				
//				Debug.debugManager().drawers().drawCircleImmediate(pCore.gameCamera(), lMobInstance.x, lMobInstance.y, 52.f);
			}
		}
	}
}
