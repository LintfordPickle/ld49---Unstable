package net.ld.unstable.renderers;

import net.ld.unstable.controllers.LevelController;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.debug.Debug;
import net.lintford.library.core.graphics.ColorConstants;
import net.lintford.library.core.graphics.textures.Texture;
import net.lintford.library.renderers.BaseRenderer;
import net.lintford.library.renderers.RendererManager;

public class LevelRenderer extends BaseRenderer {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String RENDERER_NAME = "Level Renderer";

	// --------------------------------------
	// Variable
	// --------------------------------------

	private LevelController mLevelController;
	private Texture mCoreTexture;

	// --------------------------------------
	// Properties
	// --------------------------------------

	@Override
	public boolean isInitialized() {
		return mLevelController != null;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public LevelRenderer(RendererManager pRendererManager, int pEntityGroupID) {
		super(pRendererManager, RENDERER_NAME, pEntityGroupID);
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void initialize(LintfordCore pCore) {
		final var lControllerManager = pCore.controllerManager();

		mLevelController = (LevelController) lControllerManager.getControllerByNameRequired(LevelController.CONTROLLER_NAME, entityGroupID());
	}

	@Override
	public void loadGLContent(ResourceManager pResourceManager) {
		super.loadGLContent(pResourceManager);

		mCoreTexture = pResourceManager.textureManager().getTexture("TEXTURE_CORE", LintfordCore.CORE_ENTITY_GROUP_ID);
	}

	@Override
	public void unloadGLContent() {
		super.unloadGLContent();

		mCoreTexture = null;
	}

	@Override
	public void draw(LintfordCore pCore) {
		if (!isInitialized())
			return;

		final var lCameraRect = pCore.gameCamera().boundingRectangle();

		final float lSeaLevel = mLevelController.seaLevel();

		Debug.debugManager().drawers().drawLineImmediate(pCore.gameCamera(), lCameraRect.left(), lSeaLevel, lCameraRect.w(), lSeaLevel, -0.01f, 1.f, 0.f, 0.f);

	}
}
