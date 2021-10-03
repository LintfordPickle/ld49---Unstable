package net.ld.unstable.screens;

import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.graphics.ColorConstants;
import net.lintford.library.core.graphics.textures.Texture;
import net.lintford.library.screenmanager.Screen;
import net.lintford.library.screenmanager.ScreenManager;

public class BackgroundScreen extends Screen {

	// --------------------------------------
	// Variables
	// --------------------------------------

	private Texture mLogoTexture;

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public BackgroundScreen(ScreenManager pScreenManager) {
		super(pScreenManager);

	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void loadGLContent(ResourceManager pResourceManager) {
		super.loadGLContent(pResourceManager);

		mLogoTexture = pResourceManager.textureManager().loadTexture("TEXTURE_MENU_LOGO", "res/textures/textureMenuLogo.png", entityGroupID());

	}

	@Override
	public void draw(LintfordCore pCore) {
		super.draw(pCore);

		final var lUiStructureController = screenManager.UiStructureController();
		final var lHeaderRect = pCore.HUD().boundingRectangle();

		final float lWidth = 960;
		final float lHeight = 540;

		final var ltextureBatch = rendererManager.uiTextureBatch();
		final var lLogoColor = ColorConstants.getWhiteWithAlpha(1.f);

		ltextureBatch.begin(pCore.HUD());
		ltextureBatch.draw(mLogoTexture, 0, 0, lWidth, lHeight, -lWidth * .5f, lHeaderRect.top(), lWidth, lHeight, -0.9f, lLogoColor);
		ltextureBatch.end();
	}

}
