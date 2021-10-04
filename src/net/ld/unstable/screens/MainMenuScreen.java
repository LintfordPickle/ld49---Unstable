package net.ld.unstable.screens;

import net.lintford.library.screenmanager.MenuEntry;
import net.lintford.library.screenmanager.MenuScreen;
import net.lintford.library.screenmanager.ScreenManager;
import net.lintford.library.screenmanager.layouts.ListLayout;
import net.lintford.library.screenmanager.screens.LoadingScreen;

public class MainMenuScreen extends MenuScreen {

	// --------------------------------------
	// Constants
	// --------------------------------------

	private static final int BUTTON_START = 0;
	private static final int BUTTON_EXIT = 2;

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public MainMenuScreen(ScreenManager pScreenManager) {
		super(pScreenManager, "");

		var lMainLayout = new ListLayout(this);

		var lStartGameEntry = new MenuEntry(pScreenManager, lMainLayout, "Start Game");
		var lExitEntry = new MenuEntry(pScreenManager, lMainLayout, "Exit");

		lStartGameEntry.registerClickListener(this, BUTTON_START);
		lExitEntry.registerClickListener(this, BUTTON_EXIT);

		lMainLayout.addMenuEntry(lStartGameEntry);
		lMainLayout.addMenuEntry(MenuEntry.menuSeparator());
		lMainLayout.addMenuEntry(lExitEntry);

		addLayout(lMainLayout);

		mShowBackgroundScreens = true;
		mESCBackEnabled = false;
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	protected void handleOnClick() {
		switch (mClickAction.consume()) {
		case BUTTON_START:
			LoadingScreen.load(screenManager, true, new GameScreen(screenManager));
			break;

		case BUTTON_EXIT:
			screenManager.exitGame();
			break;
		}
	}
}
