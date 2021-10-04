package net.ld.unstable.screens;

import net.ld.unstable.ConstantsGame;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.graphics.ColorConstants;
import net.lintford.library.core.graphics.textures.Texture;
import net.lintford.library.screenmanager.MenuScreen;
import net.lintford.library.screenmanager.ScreenManager;

public class HelpScreen extends MenuScreen {

	// --------------------------------------
	// Constants
	// --------------------------------------

	private Texture mHelpTexture;

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public HelpScreen(ScreenManager pScreenManager) {
		super(pScreenManager, "");

		mShowBackgroundScreens = true;
		mBlockInputInBackground = true;
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void loadGLContent(ResourceManager pResourceManager) {
		super.loadGLContent(pResourceManager);

		mHelpTexture = pResourceManager.textureManager().loadTexture("TEXTURE_HELP_SCREEN", "res/textures/textureHelpScreen.png", entityGroupID());
	}

	@Override
	public void unloadGLContent() {
		super.unloadGLContent();

		mHelpTexture = null;
	}

	@Override
	public void draw(LintfordCore pCore) {
		super.draw(pCore);

		final var lTextureBatch = rendererManager.uiTextureBatch();

		final float lX = -ConstantsGame.WINDOW_WIDTH * .5f;
		final float lY = -ConstantsGame.WINDOW_HEIGHT * .5f;

		// mHelpTexture
		lTextureBatch.begin(pCore.HUD());
		lTextureBatch.draw(mHelpTexture, 0, 0, 960, 540, lX, lY, ConstantsGame.WINDOW_WIDTH, ConstantsGame.WINDOW_HEIGHT, -0.01f, ColorConstants.WHITE);
		lTextureBatch.end();
	}

	@Override
	protected void handleOnClick() {

	}
}
