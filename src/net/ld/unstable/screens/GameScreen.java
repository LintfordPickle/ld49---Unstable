package net.ld.unstable.screens;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import net.lintford.library.core.LintfordCore;
import net.lintford.library.screenmanager.ScreenManager;
import net.lintford.library.screenmanager.screens.BaseGameScreen;

public class GameScreen extends BaseGameScreen {

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public GameScreen(ScreenManager pScreenManager) {
		super(pScreenManager);

		mShowBackgroundScreens = true;
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void handleInput(LintfordCore pCore) {
		super.handleInput(pCore);

		if (mIsExiting)
			return;

		if (pCore.input().keyboard().isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
			screenManager.addScreen(new PauseScreen(screenManager));
			return;
		}
	}

	@Override
	public void draw(LintfordCore pCore) {
		super.draw(pCore);

		GL11.glClearColor(1.f, 1.f, 0.f, 1.f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
	}
}
