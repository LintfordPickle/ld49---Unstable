package net.ld.unstable;

import org.lwjgl.opengl.GL11;

import net.ld.unstable.controllers.SoundFxController;
import net.ld.unstable.screens.BackgroundScreen;
import net.ld.unstable.screens.GameScreen;
import net.ld.unstable.screens.MainMenuScreen;
import net.lintford.library.GameInfo;
import net.lintford.library.controllers.core.MouseCursorController;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.graphics.fonts.BitmapFontManager;
import net.lintford.library.renderers.RendererManager;
import net.lintford.library.screenmanager.ScreenManager;

public class BaseGame extends LintfordCore {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	private static final String WINDOW_TITLE = "Strontium Submergence";
	private static final String APPLICATION_NAME = "Strontium Submergence";

	private static boolean DEBUG_SKIP_MENU = false;

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private ScreenManager mScreenManager;

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public BaseGame(GameInfo pGameInfo, String[] pArgs, boolean pHeadless) {
		super(pGameInfo, pArgs, pHeadless);

		mScreenManager = new ScreenManager(this);

		mIsFixedTimeStep = true;
	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	protected void onInitializeApp() {
		super.onInitializeApp();

		mScreenManager.initialize();
		if (DEBUG_SKIP_MENU == false) {
			mScreenManager.addScreen(new BackgroundScreen(mScreenManager));
			mScreenManager.addScreen(new MainMenuScreen(mScreenManager));
		} else {
			mScreenManager.addScreen(new GameScreen(mScreenManager));
		}
	}

	@Override
	protected void onLoadGLContent() {
		super.onLoadGLContent();
		
		var lMouseController = new MouseCursorController(mControllerManager, CORE_ENTITY_GROUP_ID);
		lMouseController.initialize(this);
		lMouseController.loadCursorFromFile("default", "res/cursors/cursorDefault.png", 0, 0);
		lMouseController.setCursor("default");
		
		final var lAudioManager = mResourceManager.audioManager();
		lAudioManager.loadAudioFilesFromMetafile("res/audio/_meta.json");
		
		var lSoundFxController = new SoundFxController(mControllerManager, mResourceManager.audioManager(), CORE_ENTITY_GROUP_ID);
		lSoundFxController.initialize(this);
		
		mScreenManager.loadGLContent(mResourceManager);
	}

	@Override
	protected void onUnloadGLContent() {
		super.onUnloadGLContent();
		mScreenManager.unloadGLContent();
	}

	@Override
	protected void onHandleInput() {
		super.onHandleInput();

		mScreenManager.handleInput(this);
	}

	@Override
	protected void onUpdate() {
		super.onUpdate();

		mScreenManager.update(this);
	}

	@Override
	protected void onDraw() {
		super.onDraw();

		mScreenManager.draw(this);
	}

	// ------------------------------

	@Override
	protected void oninitializeGL() {
		super.oninitializeGL();

		// Enable depth testing
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		// Enable depth testing
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);

		// Set the clear color to corn flower blue
		GL11.glClearColor(0.0f / 255.0f, 0.0f / 255.0f, 0.0f / 255.0f, 1.0f);

	}

	@Override
	protected void onInitializeBitmapFontSources(BitmapFontManager pFontManager) {
		super.onInitializeBitmapFontSources(pFontManager);

		BitmapFontManager.CoreFonts.AddOrUpdate(ScreenManager.FONT_MENU_ENTRY_NAME, "res/fonts/fontOrangeKid24.json");
		BitmapFontManager.CoreFonts.AddOrUpdate(ScreenManager.FONT_MENU_TITLE_NAME, "res/fonts/fontOrangeKid24.json");

		BitmapFontManager.CoreFonts.AddOrUpdate(RendererManager.UI_FONT_TEXT_NAME, "res/fonts/fontOrangeKid24.json");
		BitmapFontManager.CoreFonts.AddOrUpdate(RendererManager.UI_FONT_TEXT_BOLD_NAME, "res/fonts/fontOrangeKid24.json");
	}

	// -------------------------------
	// Entry Point
	// -------------------------------

	public static void main(String[] args) {
		GameInfo lGameInfo = new GameInfo() {
			@Override
			public String windowTitle() {
				return WINDOW_TITLE;

			}

			@Override
			public String applicationName() {
				return APPLICATION_NAME;

			}

			@Override
			public int minimumWindowWidth() {
				return ConstantsGame.WINDOW_WIDTH;
			}

			@Override
			public int minimumWindowHeight() {
				return ConstantsGame.WINDOW_HEIGHT;
			}

			@Override
			public int baseGameResolutionWidth() {
				return ConstantsGame.WINDOW_WIDTH;
			}

			@Override
			public int baseGameResolutionHeight() {
				return ConstantsGame.WINDOW_HEIGHT;
			}

			@Override
			public boolean stretchGameResolution() {
				return true;
			}

			@Override
			public boolean windowResizeable() {
				return true;
			}

		};

		var lBaseGame = new BaseGame(lGameInfo, args, false);
		lBaseGame.createWindow();
	}
}
