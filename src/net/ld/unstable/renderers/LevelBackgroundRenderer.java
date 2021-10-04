package net.ld.unstable.renderers;

import net.ld.unstable.controllers.LevelController;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.graphics.ColorConstants;
import net.lintford.library.core.graphics.sprites.SpriteInstance;
import net.lintford.library.core.graphics.sprites.spritesheet.SpriteSheetDefinition;
import net.lintford.library.core.graphics.textures.Texture;
import net.lintford.library.renderers.BaseRenderer;
import net.lintford.library.renderers.RendererManager;

public class LevelBackgroundRenderer extends BaseRenderer {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String RENDERER_NAME = "Level Background Renderer";

	// --------------------------------------
	// Variable
	// --------------------------------------

	private LevelController mLevelController;
	private Texture mLevelBackground;
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

	public LevelBackgroundRenderer(RendererManager pRendererManager, int pEntityGroupID) {
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

		mLevelBackground = pResourceManager.textureManager().getTexture("TEXTURE_GAME_BACKGROUND", entityGroupID());
		mOceanSpritesheet = pResourceManager.spriteSheetManager().loadSpriteSheet("res/spritesheets/spritesheetOcean.json", entityGroupID());
		mWaveSpriteInstance = mOceanSpritesheet.getSpriteInstance("waves_small");
	}

	@Override
	public void unloadGLContent() {
		super.unloadGLContent();

		mLevelBackground = null;
		mOceanSpritesheet = null;
		mWaveSpriteInstance = null;
	}

	@Override
	public void draw(LintfordCore pCore) {
		if (!isInitialized())
			return;

		final var lTextureBatch = rendererManager().uiTextureBatch();
		final var lCameraRect = pCore.gameCamera().boundingRectangle();

		lTextureBatch.begin(pCore.gameCamera());
		lTextureBatch.draw(mLevelBackground, 0, 0, 960, 540, lCameraRect, -0.9f, ColorConstants.WHITE);
		lTextureBatch.end();

		final var lSpriteBatch = rendererManager().uiSpriteBatch();

		final float lSeaLevel = mLevelController.seaLevel();
		mWaveSpriteInstance.update(pCore);

		final float lWavesWidth = mWaveSpriteInstance.width() * 1.5f;
		final float lWavesHeight = mWaveSpriteInstance.height() * 1.5f;

		lSpriteBatch.begin(pCore.gameCamera());

		final var lWaterColor = ColorConstants.getWhiteWithAlpha(1f);

		final float lLeftEdge = lCameraRect.left() - lWavesWidth;
		for (int t = (int) lLeftEdge; t < lLeftEdge + 100 + lCameraRect.w() + lWavesWidth * 2; t += (int) lWavesWidth) {
			lSpriteBatch.draw(mOceanSpritesheet.texture(), mWaveSpriteInstance.currentSpriteFrame(), t, lSeaLevel - 5f - lWavesHeight * .5f, lWavesWidth, lWavesHeight, -0.9f, lWaterColor);
		}

		lSpriteBatch.end();
	}
}
