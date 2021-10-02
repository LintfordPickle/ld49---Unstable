package net.ld.unstable.screens;

import org.lwjgl.glfw.GLFW;

import net.ld.unstable.controllers.LevelController;
import net.ld.unstable.controllers.PlayerSubController;
import net.ld.unstable.controllers.SubController;
import net.ld.unstable.data.MobManager;
import net.ld.unstable.renderers.LevelRenderer;
import net.ld.unstable.renderers.SubRenderer;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.screenmanager.ScreenManager;
import net.lintford.library.screenmanager.screens.BaseGameScreen;

public class GameScreen extends BaseGameScreen {

	// --------------------------------------
	// Variable
	// --------------------------------------

	// Data
	private MobManager mMobManager;

	// Controllers
	private SubController mSubController;
	private PlayerSubController mPlayerSubController;
	private LevelController mLevelController;

	// Renderers
	private SubRenderer mSubRenderer;
	private LevelRenderer mLevelRenderer;

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public GameScreen(ScreenManager pScreenManager) {
		super(pScreenManager);

		mShowBackgroundScreens = true;

		mMobManager = new MobManager();
		mMobManager.addNewPlayerSub(0.f, 0.f);

		createControllers();
		initializeControllers();

		createRenderers();
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void loadGLContent(ResourceManager pResourceManager) {
		super.loadGLContent(pResourceManager);

		pResourceManager.textureManager().loadTexturesFromMetafile("res/textures/_meta.json", entityGroupID());

	}

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
	}

	// --------------------------------------

	private void createControllers() {
		final var lControllerManager = screenManager.core().controllerManager();

		mLevelController = new LevelController(lControllerManager, entityGroupID());
		mSubController = new SubController(lControllerManager, mMobManager, entityGroupID());
		mPlayerSubController = new PlayerSubController(lControllerManager, mMobManager, entityGroupID());
	}

	private void initializeControllers() {
		mLevelController.initialize(screenManager.core());
		mSubController.initialize(screenManager.core());
		mPlayerSubController.initialize(screenManager.core());
	}

	private void createRenderers() {
		mSubRenderer = new SubRenderer(rendererManager, entityGroupID());
		mLevelRenderer = new LevelRenderer(rendererManager, entityGroupID());
	}
}
