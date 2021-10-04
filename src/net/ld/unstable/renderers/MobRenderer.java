package net.ld.unstable.renderers;

import org.lwjgl.opengl.GL11;

import net.ld.unstable.ConstantsGame;
import net.ld.unstable.controllers.MobController;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.debug.Debug;
import net.lintford.library.core.graphics.ColorConstants;
import net.lintford.library.core.graphics.sprites.spritegraph.SpriteGraphRenderer;
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
	}

	@Override
	public void unloadGLContent() {
		super.unloadGLContent();

		mSpriteGraphRenderer.unloadGLContent();
	}

	@Override
	public void update(LintfordCore pCore) {
		super.update(pCore);

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
			}
		}

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
			if (lMobInstance.isAlive == false || lMobInstance.spriteGraphDirty || lMobInstance.mobDefinition == null)
				continue;

			final var lSubmarineSpritegraph = lMobInstance.spriteGraphInstance();
			if (lSubmarineSpritegraph != null) {

				final var lCrazyWhite = lMobInstance.flashOn ? ColorConstants.getColor(1000.f, 1000.0f, 1000.f) : ColorConstants.WHITE;

				mSpriteGraphRenderer.begin(pCore.gameCamera());
				mSpriteGraphRenderer.drawSpriteGraphList(pCore, lSubmarineSpritegraph, lCrazyWhite);
				mSpriteGraphRenderer.end();

				if (ConstantsGame.DEBUG_DRAW_MOB_COLLIDERS) {
					GL11.glLineWidth(2.f);

					if (lMobInstance.mobDefinition.largeCollisionEntity) {
						final float lColRadius = lMobInstance.mobDefinition.collisionRadius;
						Debug.debugManager().drawers().drawCircleImmediate(pCore.gameCamera(), lMobInstance.worldPositionX - lColRadius, lMobInstance.worldPositionY, lColRadius);
						Debug.debugManager().drawers().drawCircleImmediate(pCore.gameCamera(), lMobInstance.worldPositionX + lColRadius, lMobInstance.worldPositionY, lColRadius);
						Debug.debugManager().drawers().drawCircleImmediate(pCore.gameCamera(), lMobInstance.worldPositionX, lMobInstance.worldPositionY, lColRadius * 2.f);
					} else {
						Debug.debugManager().drawers().drawCircleImmediate(pCore.gameCamera(), lMobInstance.worldPositionX, lMobInstance.worldPositionY, lMobInstance.mobDefinition.collisionRadius);
					}
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
