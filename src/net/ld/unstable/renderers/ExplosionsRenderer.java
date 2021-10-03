package net.ld.unstable.renderers;

import java.util.ArrayList;
import java.util.List;

import net.ld.unstable.data.explosions.ExplosionsController;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.graphics.ColorConstants;
import net.lintford.library.core.graphics.sprites.AnimatedSpriteListener;
import net.lintford.library.core.graphics.sprites.SpriteInstance;
import net.lintford.library.core.graphics.sprites.spritesheet.SpriteSheetDefinition;
import net.lintford.library.renderers.BaseRenderer;
import net.lintford.library.renderers.RendererManager;

public class ExplosionsRenderer extends BaseRenderer implements AnimatedSpriteListener {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String RENDERER_NAME = "Explosions Renderer";

	// --------------------------------------
	// Variables
	// --------------------------------------

	private SpriteSheetDefinition mExplosionsSpritesheet;
	private ExplosionsController mExplosionsController;

	// --------------------------------------
	// Properties
	// --------------------------------------

	@Override
	public boolean isInitialized() {
		return mExplosionsController != null;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public ExplosionsRenderer(RendererManager pRendererManager, int pEntityGroupID) {
		super(pRendererManager, RENDERER_NAME, pEntityGroupID);
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void initialize(LintfordCore pCore) {
		final var lControllerManager = pCore.controllerManager();

		mExplosionsController = (ExplosionsController) lControllerManager.getControllerByNameRequired(ExplosionsController.CONTROLLER_NAME, entityGroupID());
	}

	@Override
	public void loadGLContent(ResourceManager pResourceManager) {
		super.loadGLContent(pResourceManager);

		mExplosionsSpritesheet = pResourceManager.spriteSheetManager().loadSpriteSheet("res/spritesheets/spritesheetExplosions.json", entityGroupID());
	}

	@Override
	public void unloadGLContent() {
		super.unloadGLContent();

		mExplosionsSpritesheet = null;
	}

	@Override
	public void draw(LintfordCore pCore) {
		if (!isInitialized())
			return;

		// TODO: Add to engine
		// FIXME : Seem to be missing some way to 'fire and forget' animations

		int counter = 0; // don't take the piss
		boolean moreExplosions = mExplosionsController.hasUnprocessedExplosions();
		while (moreExplosions && counter < 4) {
			final var lExplosion = mExplosionsController.getExplosionToProcess();
			if(lExplosion.animName == null) continue;
			final var lNewExplosion = mExplosionsSpritesheet.getSpriteInstance(lExplosion.animName);
			
			lNewExplosion.animatedSpriteListender(this);
			lNewExplosion.x(lExplosion.worldX);
			lNewExplosion.y(lExplosion.worldY);
			lNewExplosion.setFrame(0);

			animationsToUpdate.add(lNewExplosion);

			// FIXME: Recycle explosions too
			moreExplosions = mExplosionsController.hasUnprocessedExplosions();
			counter++;
		}

		animationsToUpdate2.clear();
		animationsToUpdate2.addAll(animationsToUpdate);

		final var lSpriteBatch = rendererManager().uiSpriteBatch();

		final int lNumAnimations = animationsToUpdate2.size();
		for (int i = 0; i < lNumAnimations; i++) {
			final var lAnimInstance = animationsToUpdate2.get(i);
			lAnimInstance.update(pCore);
		}

		lSpriteBatch.begin(pCore.gameCamera());
		for (int i = 0; i < lNumAnimations; i++) {
			final var lAnimInstance = animationsToUpdate2.get(i);

			final float lDstW = lAnimInstance.width();
			final float lDstH = lAnimInstance.height();
			final float lDstX = lAnimInstance.x() - lDstW * .5f;
			final float lDstY = lAnimInstance.y() - lDstH * .5f;

			lSpriteBatch.draw(mExplosionsSpritesheet, lAnimInstance.currentSpriteFrame(), lDstX, lDstY, lDstW, lDstH, -0.4f, ColorConstants.WHITE);
		}

		lSpriteBatch.end();
	}

	private List<SpriteInstance> animationsToUpdate = new ArrayList<>();
	private List<SpriteInstance> animationsToUpdate2 = new ArrayList<>();

	// --------------------------------------
	// Interface Methods
	// --------------------------------------

	@Override
	public void onStarted(SpriteInstance pSender) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLooped(SpriteInstance pSender) {

	}

	@Override
	public void onStopped(SpriteInstance pSender) {
		animationsToUpdate.remove(pSender);
		mExplosionsSpritesheet.releaseInstance(pSender);
	}
}
