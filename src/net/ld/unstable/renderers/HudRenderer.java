package net.ld.unstable.renderers;

import net.ld.unstable.controllers.GameStateController;
import net.ld.unstable.controllers.LevelController;
import net.ld.unstable.controllers.MobController;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.graphics.Color;
import net.lintford.library.core.graphics.ColorConstants;
import net.lintford.library.core.graphics.textures.Texture;
import net.lintford.library.core.maths.MathHelper;
import net.lintford.library.core.noise.SimplexNoise;
import net.lintford.library.renderers.BaseRenderer;
import net.lintford.library.renderers.RendererManager;

public class HudRenderer extends BaseRenderer {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String RENDERER_NAME = "Hud Renderer";

	// --------------------------------------
	// Variable
	// --------------------------------------

	private GameStateController mGameStateController;
	private LevelController mLevelController;
	private MobController mMobController;
	private Texture mHudtexture;
	private final SimplexNoise noise = new SimplexNoise(10, 2, 192182);
	private int rRamp = 0;
	private int gRamp = 0;
	private int bRamp = 0;

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

	public HudRenderer(RendererManager pRendererManager, int pEntityGroupID) {
		super(pRendererManager, RENDERER_NAME, pEntityGroupID);
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void initialize(LintfordCore pCore) {
		final var lControllerManager = pCore.controllerManager();

		mLevelController = (LevelController) lControllerManager.getControllerByNameRequired(LevelController.CONTROLLER_NAME, entityGroupID());
		mMobController = (MobController) lControllerManager.getControllerByNameRequired(MobController.CONTROLLER_NAME, entityGroupID());
		mGameStateController = (GameStateController) lControllerManager.getControllerByNameRequired(GameStateController.CONTROLLER_NAME, entityGroupID());
	}

	@Override
	public void loadGLContent(ResourceManager pResourceManager) {
		super.loadGLContent(pResourceManager);

		mHudtexture = pResourceManager.textureManager().getTexture("TEXTURE_HUD", entityGroupID());
	}

	@Override
	public void unloadGLContent() {
		super.unloadGLContent();

		mHudtexture = null;
	}

	@Override
	public void draw(LintfordCore pCore) {
		if (!isInitialized())
			return;

		drawScore(pCore);
		drawSubmarineStats(pCore);

	}

	private void drawScore(LintfordCore pCore) {
		final var lHudBounds = pCore.HUD().boundingRectangle();

		final var lScore = String.valueOf(mGameStateController.score());
		final var lHudFont = rendererManager().uiTextFont();
		lHudFont.begin(pCore.HUD());
		final float lScoreLabelWidthHalf = lHudFont.getStringWidth("Score") * .5f;
		lHudFont.drawText("Score", lHudBounds.right() - 75.f - lScoreLabelWidthHalf, lHudBounds.top() + 5f, -0.35f, 1.f);
		final float lScoreWidthHalf = lHudFont.getStringWidth(lScore) * .5f;
		lHudFont.drawText(lScore, lHudBounds.right() - 75.f - lScoreWidthHalf, lHudBounds.top() + 30f, -0.35f, 1.f);
		if (mGameStateController.scoreMultiplier() > 1) {
			final String lMultiplier = mGameStateController.scoreMultiplier() + "x";
			final float lMultiplierWidth = lHudFont.getStringWidth(lMultiplier) * .5f;

			final float r = (float) noise.getNoise(rRamp += 1, 2);
			final float g = (float) noise.getNoise(gRamp += 2, 2);
			final float b = (float) noise.getNoise(bRamp += 3, 2);

			final var lRandomColor = ColorConstants.getColor(r, g, b);

			lHudFont.drawText(lMultiplier, lHudBounds.right() - 75.f - lMultiplierWidth, lHudBounds.top() + 55f, -0.35f, lRandomColor, 1.f);
		}

		lHudFont.end();
	}

	private void drawSubmarineStats(LintfordCore pCore) {
		final var lHudBounds = pCore.HUD().boundingRectangle();

		final var lTextureBatch = rendererManager().uiTextureBatch();
		final var lHudFont = rendererManager().uiTextFont();

		lHudFont.begin(pCore.HUD());
		lHudFont.drawText("HP", lHudBounds.left() + 5.f, lHudBounds.top() + 5f, -0.35f, 1.f);
		lHudFont.drawText("Coolant", lHudBounds.left() + 5.f, lHudBounds.top() + 5f + 25f, -0.35f, 1.f);
		lHudFont.end();

		final var lPlayerSub = mMobController.mobManager().playerSubmarine;

		final int lPlayerHp = lPlayerSub.health;
		final int lPlayerMaxHp = lPlayerSub.mobDefinition.maxHealth;

		final int lPlayerCoolant = lPlayerSub.coolant;
		final int lPlayerMaxCoolant = lPlayerSub.mobDefinition.maxCoolant;

		final float lBarWidth = 256.f;
		final float lHpBarInner = MathHelper.scaleToRange(lPlayerHp, 0, lPlayerMaxHp, 0, lBarWidth);
		final float lCoolantBarInner = MathHelper.scaleToRange(lPlayerCoolant, 0, lPlayerMaxCoolant, 0, lBarWidth);

		final float lBarPosX = lHudBounds.left() + 100.f;
		final float lBarPosY = lHudBounds.top();

		lTextureBatch.begin(pCore.HUD());
		drawBar(pCore, lBarPosX, lBarPosY, lBarWidth, 32, lHpBarInner, ColorConstants.GREEN);
		drawBar(pCore, lBarPosX, lBarPosY + 32, lBarWidth, 32, lCoolantBarInner, ColorConstants.RED);
		lTextureBatch.end();
	}

	private void drawBar(LintfordCore pCore, float pPosX, float pPosY, float pWidth, float pHeight, float pInnerWidth, Color pColor) {
		final var lTextureBatch = rendererManager().uiTextureBatch();

		// Outer bar
		float lMiddleWidth = pWidth - 64.f;
		lTextureBatch.draw(mHudtexture, 0, 0, 32, 32, pPosX + 0, pPosY + 0, 32, 32, -0.35f, ColorConstants.WHITE);
		lTextureBatch.draw(mHudtexture, 32, 0, 32, 32, pPosX + 32, pPosY + 0, lMiddleWidth, 32, -0.35f, ColorConstants.WHITE);
		lTextureBatch.draw(mHudtexture, 64, 0, 32, 32, pPosX + 32 + lMiddleWidth, pPosY + 0, 32, 32, -0.35f, ColorConstants.WHITE);

		if (pInnerWidth > 0.f) {
			float lWidth = (float) Math.min(32.f, pInnerWidth);
			lTextureBatch.draw(mHudtexture, 0, 32, lWidth, 32, pPosX + 0, pPosY + 0, lWidth, 32, -0.35f, pColor);
		}

		if (pInnerWidth > 32.f) {
			float lWidth = (float) Math.min(lMiddleWidth, pInnerWidth - 32.f);
			lTextureBatch.draw(mHudtexture, 32, 32, 32, 32, pPosX + 32, pPosY + 0, lWidth, 32, -0.35f, pColor);
		}

		if (pInnerWidth > pWidth - 32.0f) {
			float lWidth = 32.f - (float) Math.min(pWidth - pInnerWidth, 32.0f);
			lTextureBatch.draw(mHudtexture, 64, 32, lWidth, 32, pPosX + 32 + lMiddleWidth, pPosY + 0, lWidth, 32, -0.01f, pColor);
		}
	}
}
