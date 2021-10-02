package net.ld.unstable.renderers;

import net.ld.unstable.controllers.SubController;
import net.ld.unstable.data.Textures.MobTextureNames;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.debug.Debug;
import net.lintford.library.core.graphics.ColorConstants;
import net.lintford.library.core.graphics.sprites.spritesheet.SpriteSheetDefinition;
import net.lintford.library.renderers.BaseRenderer;
import net.lintford.library.renderers.RendererManager;

public class SubRenderer extends BaseRenderer {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String RENDERER_NAME = "Submarine Renderer";

	// --------------------------------------
	// Variables
	// --------------------------------------

	private SubController mSubController;
	private SpriteSheetDefinition mMobSpritesheet;

	// --------------------------------------
	// Properties
	// --------------------------------------

	@Override
	public boolean isInitialized() {
		return false;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public SubRenderer(RendererManager pRendererManager, int pEntityGroupID) {
		super(pRendererManager, RENDERER_NAME, pEntityGroupID);
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void initialize(LintfordCore pCore) {
		final var lControllerManager = pCore.controllerManager();

		mSubController = (SubController) lControllerManager.getControllerByNameRequired(SubController.CONTROLLER_NAME, entityGroupID());
	}

	@Override
	public void loadGLContent(ResourceManager pResourceManager) {
		super.loadGLContent(pResourceManager);

		mMobSpritesheet = pResourceManager.spriteSheetManager().loadSpriteSheet("res/spritesheets/spritesheetMobs.json", entityGroupID());
	}

	@Override
	public void unloadGLContent() {
		super.unloadGLContent();

		mMobSpritesheet = null;
	}

	@Override
	public void draw(LintfordCore pCore) {
		final var lSpriteBatch = rendererManager().uiSpriteBatch();

		// Render the player separtely
		final var lMobManager = mSubController.mobManager();
		final var lPlayerSubmarine = lMobManager.getPlayerSubmarine();

		final var lWidth = 150.f;
		final var lHeight = 90.f;

		final float lSubmarinePositionX = lPlayerSubmarine.x - lWidth * .5f;
		final float lSubmarinePositionY = lPlayerSubmarine.y - lHeight * .5f;

		Debug.debugManager().drawers().drawRectImmediate(pCore.gameCamera(), lSubmarinePositionX, lSubmarinePositionY, lWidth, lHeight, 2.f, 1.f, 1.f, 0.f);

		lSpriteBatch.begin(pCore.gameCamera());
		lSpriteBatch.draw(mMobSpritesheet, MobTextureNames.SUBMARINE, lSubmarinePositionX, lSubmarinePositionY, lWidth, lHeight, -0.01f, ColorConstants.WHITE);
		lSpriteBatch.end();

	}
}
