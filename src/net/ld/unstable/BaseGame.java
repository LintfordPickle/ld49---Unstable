package net.ld.unstable;

import org.lwjgl.opengl.GL11;

import net.ld.unstable.screens.BackgroundScreen;
import net.ld.unstable.screens.GameScreen;
import net.ld.unstable.screens.MainMenuScreen;
import net.lintford.library.GameInfo;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.graphics.fonts.BitmapFontManager;
import net.lintford.library.renderers.RendererManager;
import net.lintford.library.screenmanager.ScreenManager;

public class BaseGame extends LintfordCore {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	private static final String WINDOW_TITLE = "Unstable";
	private static final String APPLICATION_NAME = "Unstable";

	private static final int WINDOW_WIDTH = 960;
	private static final int WINDOW_HEIGHT = 540;

	private static boolean DEBUG_SKIP_MENU = true;

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
		GL11.glClearColor(0.0f / 255.0f, 149.0f / 255.0f, 237.0f / 255.0f, 1.0f);

	}

	@Override
	protected void onInitializeBitmapFontSources(BitmapFontManager pFontManager) {
		super.onInitializeBitmapFontSources(pFontManager);

		BitmapFontManager.CoreFonts.AddOrUpdate(ScreenManager.FONT_MENU_ENTRY_NAME, "res/fonts/fontOrangeKid24.json");

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
				return WINDOW_WIDTH;
			}

			@Override
			public int minimumWindowHeight() {
				return WINDOW_HEIGHT;
			}

			@Override
			public int baseGameResolutionWidth() {
				return WINDOW_WIDTH;
			}

			@Override
			public int baseGameResolutionHeight() {
				return WINDOW_HEIGHT;
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
