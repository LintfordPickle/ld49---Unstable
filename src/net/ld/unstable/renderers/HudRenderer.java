package net.ld.unstable.renderers;

import net.ld.unstable.controllers.LevelController;
import net.ld.unstable.controllers.MobController;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.graphics.Color;
import net.lintford.library.core.graphics.ColorConstants;
import net.lintford.library.core.graphics.textures.Texture;
import net.lintford.library.core.maths.MathHelper;
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

	private LevelController mLevelController;
	private MobController mMobController;
	private Texture mHudtexture;

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
