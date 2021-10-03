package net.ld.unstable.renderers;

import net.ld.unstable.controllers.LevelController;
import net.ld.unstable.data.Textures.WavesTextureNames;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.graphics.ColorConstants;
import net.lintford.library.core.graphics.sprites.SpriteInstance;
import net.lintford.library.core.graphics.sprites.spritesheet.SpriteSheetDefinition;
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
	private SpriteSheetDefinition mOceanSpritesheet;
	private SpriteInstance mWaveSpriteInstance;

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

		mOceanSpritesheet = pResourceManager.spriteSheetManager().loadSpriteSheet("res/spritesheets/spritesheetOcean.json", entityGroupID());
		mWaveSpriteInstance = mOceanSpritesheet.getSpriteInstance("waves");
	}

	@Override
	public void unloadGLContent() {
		super.unloadGLContent();

		mOceanSpritesheet = null;
	}

	@Override
	public void draw(LintfordCore pCore) {
		if (!isInitialized())
			return;

		final var lSpriteBatch = rendererManager().uiSpriteBatch();
		final var lCameraRect = pCore.gameCamera().boundingRectangle();

		final float lSeaLevel = mLevelController.seaLevel();
		mWaveSpriteInstance.update(pCore);

		final float lWavesWidth = mWaveSpriteInstance.width();
		final float lWavesHeight = mWaveSpriteInstance.height();

		lSpriteBatch.begin(pCore.gameCamera());

		final float lLeftEdge = lCameraRect.left() - lWavesWidth;
		for (int t = (int) lLeftEdge; t < lLeftEdge + lCameraRect.w() + lWavesWidth*2; t += (int) lWavesWidth) {
			lSpriteBatch.draw(mOceanSpritesheet.texture(), mWaveSpriteInstance.currentSpriteFrame(), t, lSeaLevel - lWavesHeight * .5f, lWavesWidth, lWavesHeight, -0.7f, ColorConstants.WHITE);
		}

		lSpriteBatch.draw(mOceanSpritesheet, WavesTextureNames.UNDERWATER, lLeftEdge, lSeaLevel, lCameraRect.width() + lWavesWidth * 2, lCameraRect.width(), -0.7f, ColorConstants.WHITE);

		lSpriteBatch.end();

		// Debug.debugManager().drawers().drawLineImmediate(pCore.gameCamera(), lCameraRect.left(), lSeaLevel, lCameraRect.w(), lSeaLevel, -0.01f, 1.f, 0.f, 0.f);
	}
}
