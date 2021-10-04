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
	private Texture mBackgroundTexture;

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
		mBackgroundTexture = pResourceManager.textureManager().loadTexture("TEXTURE_MENU_BACKGROUND", "res/textures/textureMenuBackground.png", entityGroupID());
	}

	@Override
	public void draw(LintfordCore pCore) {
		super.draw(pCore);
		final var lHeaderRect = pCore.HUD().boundingRectangle();

		final float lWidth = 571;
		final float lHeight = 151;

		final var ltextureBatch = rendererManager.uiTextureBatch();
		final var lLogoColor = ColorConstants.getWhiteWithAlpha(1.f);

		ltextureBatch.begin(pCore.HUD());
		ltextureBatch.draw(mBackgroundTexture, 0, 0, mBackgroundTexture.getTextureWidth(), mBackgroundTexture.getTextureHeight(), -lHeaderRect.w() * .5f, -lHeaderRect.h() * .5f, lHeaderRect.w(), lHeaderRect.h(), -0.9f,
				lLogoColor);
		ltextureBatch.draw(mLogoTexture, 0, 0, lWidth, lHeight, -lWidth * .5f, lHeaderRect.top() + 15, lWidth, lHeight, -0.9f, lLogoColor);
		ltextureBatch.end();

		final var lTextFont = rendererManager.uiTextFont();
		lTextFont.begin(pCore.HUD());
		lTextFont.drawText("Created for LD49 by LintfordPickle", lHeaderRect.left() + 5f, lHeaderRect.bottom() - 35, -0.08f, 1.f);
		lTextFont.end();
	}

}
