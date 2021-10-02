package net.ld.unstable.screens;

import org.lwjgl.glfw.GLFW;

import net.ld.unstable.controllers.LevelController;
import net.ld.unstable.controllers.PlayerSubController;
import net.ld.unstable.controllers.SubController;
import net.ld.unstable.data.mobs.MobManager;
import net.ld.unstable.renderers.LevelRenderer;
import net.ld.unstable.renderers.SubRenderer;
import net.lintford.library.controllers.geometry.SpriteGraphController;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.geometry.spritegraph.SpriteGraphManager;
import net.lintford.library.screenmanager.ScreenManager;
import net.lintford.library.screenmanager.screens.BaseGameScreen;

public class GameScreen extends BaseGameScreen {

	// --------------------------------------
	// Variable
	// --------------------------------------

	// Data
	private SpriteGraphManager mSpriteGraphManager;
	private MobManager mMobManager;

	// Controllers
	private SpriteGraphController mSpriteGraphController;
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

		mSpriteGraphManager = new SpriteGraphManager();

		mMobManager = new MobManager();
		mMobManager.addNewPlayerSub(0.f, 0.f);

		createControllers();
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void loadGLContent(ResourceManager pResourceManager) {
		super.loadGLContent(pResourceManager);

		pResourceManager.spriteGraphRepository().loadSpriteGraphsFromMeta("res/spritegraphs/_meta.json", entityGroupID());
		pResourceManager.textureManager().loadTexturesFromMetafile("res/textures/_meta.json", entityGroupID());

		pResourceManager.spriteSheetManager().loadSpriteSheet("res/spritesheets/spritesheetSubmarines.json", entityGroupID());
		pResourceManager.spriteSheetManager().loadSpriteSheet("res/spritesheets/spritesheetPropeller.json", entityGroupID());
		pResourceManager.spriteSheetManager().loadSpriteSheet("res/spritesheets/spritesheetPowercore.json", entityGroupID());
		
		initializeControllers();

		createRenderers(pResourceManager);
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

		mSpriteGraphController = new SpriteGraphController(lControllerManager, mSpriteGraphManager, entityGroupID());
		mLevelController = new LevelController(lControllerManager, entityGroupID());
		mSubController = new SubController(lControllerManager, mMobManager, entityGroupID());
		mPlayerSubController = new PlayerSubController(lControllerManager, mMobManager, entityGroupID());
	}

	private void initializeControllers() {
		mSpriteGraphController.initialize(screenManager.core());
		mLevelController.initialize(screenManager.core());
		mSubController.initialize(screenManager.core());
		mPlayerSubController.initialize(screenManager.core());
	}

	private void createRenderers(ResourceManager pResourceManager) {
		final var lCore = screenManager.core();
		mSubRenderer = new SubRenderer(rendererManager, entityGroupID());
		mSubRenderer.initialize(lCore);
		mSubRenderer.loadGLContent(pResourceManager);

		mLevelRenderer = new LevelRenderer(rendererManager, entityGroupID());
		mLevelRenderer.initialize(lCore);
		mLevelRenderer.loadGLContent(pResourceManager);

	}
}