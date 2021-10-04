package net.ld.unstable.renderers;

import net.ld.unstable.ConstantsGame;
import net.ld.unstable.controllers.LevelController;
import net.ld.unstable.data.textures.WavesTextureNames;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.debug.Debug;
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
		mWaveSpriteInstance = mOceanSpritesheet.getSpriteInstance("waves_big");
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

		final var lWaterColor = ColorConstants.getWhiteWithAlpha(.5f);

		final float lLeftEdge = lCameraRect.left() - lWavesWidth;
		for (int t = (int) lLeftEdge; t < lLeftEdge + 100 + lCameraRect.w() + lWavesWidth * 2; t += (int) lWavesWidth) {
			lSpriteBatch.draw(mOceanSpritesheet.texture(), mWaveSpriteInstance.currentSpriteFrame(), t, lSeaLevel - lWavesHeight * .5f, lWavesWidth, lWavesHeight, -0.4f, lWaterColor);
		}

		lSpriteBatch.draw(mOceanSpritesheet, WavesTextureNames.UNDERWATER, lLeftEdge, lSeaLevel + lWavesHeight * .5f, lCameraRect.width() + lWavesWidth * 2, lCameraRect.width(), -0.4f, lWaterColor);
		lSpriteBatch.end();

		// Sea level
		if (ConstantsGame.DEBUG_OOB_DRAWERS) {
			Debug.debugManager().drawers().drawLineImmediate(pCore.gameCamera(), lCameraRect.left(), lSeaLevel, lCameraRect.w(), lSeaLevel, -0.01f, 1.f, 0.f, 0.f);

			// world position X
			final float lWorldPositionX = mLevelController.worldPositionX();
			final float lWorldLeftX = lWorldPositionX - lCameraRect.w() * .5f + 2.0f;
			Debug.debugManager().drawers().drawLineImmediate(pCore.gameCamera(), lWorldLeftX, lSeaLevel - 50, lWorldLeftX, lSeaLevel + 50, -0.01f, 1.f, 0.f, 0.f);
			Debug.debugManager().drawers().drawLineImmediate(pCore.gameCamera(), lWorldPositionX, lSeaLevel - 50, lWorldPositionX, lSeaLevel + 50, -0.01f, 1.f, 0.f, 0.f);
		}
	}
}
