package net.ld.unstable.screens;

import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.graphics.ColorConstants;
import net.lintford.library.screenmanager.MenuEntry;
import net.lintford.library.screenmanager.MenuScreen;
import net.lintford.library.screenmanager.ScreenManager;
import net.lintford.library.screenmanager.layouts.BaseLayout;
import net.lintford.library.screenmanager.layouts.ListLayout;
import net.lintford.library.screenmanager.screens.LoadingScreen;

public class LostHealthScreen extends MenuScreen {

	// --------------------------------------
	// Constants
	// --------------------------------------

	private static final int BUTTON_RESTART = 1;
	private static final int BUTTON_EXIT_MENU = 2;
	private static final int BUTTON_EXIT_DESKTOP = 3;
	// --------------------------------------
	// Constructor
	// --------------------------------------

	public LostHealthScreen(ScreenManager pScreenManager) {
		super(pScreenManager, "U-BOOT KAPUTT");

		BaseLayout lMainLayout = new ListLayout(this);

		var lRestartGameEntry = new MenuEntry(pScreenManager, lMainLayout, "Restart");

		var lExitMenuEntry = new MenuEntry(pScreenManager, lMainLayout, "Exit to Menu");
		var lExitDesktopEntry = new MenuEntry(pScreenManager, lMainLayout, "Exit to Desktop");

		lRestartGameEntry.registerClickListener(this, BUTTON_RESTART);

		lExitMenuEntry.registerClickListener(this, BUTTON_EXIT_MENU);
		lExitDesktopEntry.registerClickListener(this, BUTTON_EXIT_DESKTOP);

		lMainLayout.addMenuEntry(lRestartGameEntry);
		lMainLayout.addMenuEntry(MenuEntry.menuSeparator());
		lMainLayout.addMenuEntry(lExitMenuEntry);
		lMainLayout.addMenuEntry(lExitDesktopEntry);

		addLayout(lMainLayout);

		mBlockInputInBackground = true;
		mESCBackEnabled = false;
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	protected void handleOnClick() {
		switch (mClickAction.consume()) {
		case BUTTON_EXIT_MENU:
			LoadingScreen.load(screenManager, false, new BackgroundScreen(screenManager), new MainMenuScreen(screenManager));
			break;

		case BUTTON_RESTART:
			LoadingScreen.load(screenManager, true, new GameScreen(screenManager));
			return;

		case BUTTON_EXIT_DESKTOP:
			screenManager.exitGame();
			break;
		}
	}

	@Override
	public void draw(LintfordCore pCore) {

		final var lCoreTexture = pCore.resources().textureManager().getTexture("TEXTURE_CORE", LintfordCore.CORE_ENTITY_GROUP_ID);
		final var lTextureBatch = rendererManager.uiTextureBatch();
		final var lHudBounds = pCore.HUD().boundingRectangle();

		lTextureBatch.begin(pCore.HUD());
		lTextureBatch.draw(lCoreTexture, 0, 0, 32, 32, lHudBounds, -0.32f, ColorConstants.getBlackWithAlpha(0.5f));
		lTextureBatch.end();

		super.draw(pCore);

	}
}
