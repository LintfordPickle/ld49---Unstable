package net.ld.unstable.renderers;

import org.lwjgl.opengl.GL11;

import net.ld.unstable.ConstantsGame;
import net.ld.unstable.controllers.MobController;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.debug.Debug;
import net.lintford.library.core.graphics.ColorConstants;
import net.lintford.library.core.graphics.sprites.spritegraph.SpriteGraphRenderer;
import net.lintford.library.core.graphics.sprites.spritesheet.SpriteSheetDefinition;
import net.lintford.library.renderers.BaseRenderer;
import net.lintford.library.renderers.RendererManager;

public class MobRenderer extends BaseRenderer {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String RENDERER_NAME = "Submarine Renderer";

	// --------------------------------------
	// Variables
	// --------------------------------------

	private MobController mMobController;
	private SpriteSheetDefinition mMobSpritesheet;
	private SpriteGraphRenderer mSpriteGraphRenderer;

	// --------------------------------------
	// Properties
	// --------------------------------------

	@Override
	public boolean isInitialized() {
		return mMobController != null;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public MobRenderer(RendererManager pRendererManager, int pEntityGroupID) {
		super(pRendererManager, RENDERER_NAME, pEntityGroupID);

		mSpriteGraphRenderer = new SpriteGraphRenderer();
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void initialize(LintfordCore pCore) {
		final var lControllerManager = pCore.controllerManager();

		mMobController = (MobController) lControllerManager.getControllerByNameRequired(MobController.CONTROLLER_NAME, entityGroupID());
	}

	@Override
	public void loadGLContent(ResourceManager pResourceManager) {
		super.loadGLContent(pResourceManager);

		mSpriteGraphRenderer.loadGLContent(pResourceManager);
		mMobSpritesheet = pResourceManager.spriteSheetManager().loadSpriteSheet("res/spritesheets/spritesheetSubmarines.json", entityGroupID());
	}

	@Override
	public void unloadGLContent() {
		super.unloadGLContent();

		mSpriteGraphRenderer.unloadGLContent();
		mMobSpritesheet = null;
	}

	@Override
	public void draw(LintfordCore pCore) {
		if (!isInitialized())
			return;

		final var lMobManager = mMobController.mobManager();
		final var lMobs = lMobManager.mobs();
		final int lMobCount = lMobs.size();
		for (int i = 0; i < lMobCount; i++) {
			final var lMobInstance = lMobs.get(i);
			if (lMobInstance.isAlive == false || lMobInstance.spriteGraphDirty)
				continue;

			final var lSubmarineSpritegraph = lMobInstance.spriteGraphInstance();
			if (lSubmarineSpritegraph != null) {
				lMobInstance.spriteGraphInstance().update(pCore);

				final var lCrazyWhite = lMobInstance.flashOn ? ColorConstants.getColor(1000.f, 1000.0f, 1000.f) : ColorConstants.WHITE;

				mSpriteGraphRenderer.begin(pCore.gameCamera());
				mSpriteGraphRenderer.drawSpriteGraphList(pCore, lSubmarineSpritegraph, lCrazyWhite);
				mSpriteGraphRenderer.end();

				if (ConstantsGame.DEBUG_DRAW_MOB_COLLIDERS) {
					GL11.glLineWidth(2.f);
					Debug.debugManager().drawers().drawCircleImmediate(pCore.gameCamera(), lMobInstance.worldPositionX - 25, lMobInstance.worldPositionY, 25.f);
					Debug.debugManager().drawers().drawCircleImmediate(pCore.gameCamera(), lMobInstance.worldPositionX + 25, lMobInstance.worldPositionY, 25.f);
					Debug.debugManager().drawers().drawCircleImmediate(pCore.gameCamera(), lMobInstance.worldPositionX, lMobInstance.worldPositionY, 52.f);
				}
			}
		}

		if (ConstantsGame.DEBUG_OOB_DRAWERS) {
			final var lPlayerSub = lMobManager.playerSubmarine;
			final float lPlayerWorldSubPosX = lPlayerSub.worldPositionX;
			Debug.debugManager().drawers().drawLineImmediate(pCore.gameCamera(), lPlayerWorldSubPosX, -50, lPlayerWorldSubPosX, +50, -0.01f, 1.f, 0.f, 0.f);
		}
	}
}
