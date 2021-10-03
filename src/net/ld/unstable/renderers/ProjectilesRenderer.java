package net.ld.unstable.renderers;

import net.ld.unstable.controllers.ProjectileController;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.graphics.ColorConstants;
import net.lintford.library.core.graphics.textures.Texture;
import net.lintford.library.renderers.BaseRenderer;
import net.lintford.library.renderers.RendererManager;

public class ProjectilesRenderer extends BaseRenderer {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String RENDERER_NAME = "Projectiles Renderer";

	// --------------------------------------
	// Variables
	// --------------------------------------

	private ProjectileController mProjectilesController;
	private Texture mParticlesTexture;

	// --------------------------------------
	// Properties
	// --------------------------------------

	@Override
	public boolean isInitialized() {
		return mProjectilesController != null;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public ProjectilesRenderer(RendererManager pRendererManager, int pEntityGroupID) {
		super(pRendererManager, RENDERER_NAME, pEntityGroupID);
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void initialize(LintfordCore pCore) {
		final var lControllerManager = pCore.controllerManager();

		mProjectilesController = (ProjectileController) lControllerManager.getControllerByNameRequired(ProjectileController.CONTROLLER_NAME, entityGroupID());
	}

	@Override
	public void loadGLContent(ResourceManager pResourceManager) {
		super.loadGLContent(pResourceManager);

		mParticlesTexture = pResourceManager.textureManager().getTexture("TEXTURE_PARTICLES", entityGroupID());
	}

	@Override
	public void unloadGLContent() {
		super.unloadGLContent();

		mParticlesTexture = null;
	}

	@Override
	public void draw(LintfordCore pCore) {
		if (!isInitialized())
			return;

		final var lTextureBatch = rendererManager().uiTextureBatch();

		final var lProjectiles = mProjectilesController.projectileManager().projectiles();
		final int lProjectileCount = lProjectiles.size();
		for (int i = 0; i < lProjectileCount; i++) {
			final var lProjectile = lProjectiles.get(i);
			if (lProjectile.isAssigned() == false)
				continue;

			final float lSrcX = lProjectile.sx;
			final float lSrcY = lProjectile.sy;
			final float lSrcW = lProjectile.sw;
			final float lSrcH = lProjectile.sh;

			final float lDstX = lProjectile.worldPositionX - lSrcW * .5f;
			final float lDstY = lProjectile.worldPositionY - lSrcH * .5f;

			lTextureBatch.begin(pCore.gameCamera());
			lTextureBatch.drawAroundCenter(
					mParticlesTexture, 
					lSrcX, lSrcY, lSrcW, lSrcH, 
					lDstX - lSrcW * .5f, lDstY - lSrcH * .5f, lSrcW, lSrcH, 
					-0.02f, lProjectile.rotationInRadians, 0.f, 0.f, 1.f,  ColorConstants.WHITE);
			lTextureBatch.end();

			// Debug.debugManager().drawers().drawCircleImmediate(pCore.gameCamera(), lDstX, lDstY, 10.0f);

		}
	}
}
