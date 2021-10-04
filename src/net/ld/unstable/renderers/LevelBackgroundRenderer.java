package net.ld.unstable.renderers;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import net.ld.unstable.controllers.LevelController;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.graphics.ColorConstants;
import net.lintford.library.core.graphics.geometry.FullScreenTexturedQuad;
import net.lintford.library.core.graphics.shaders.ShaderMVP_PT;
import net.lintford.library.core.graphics.sprites.SpriteInstance;
import net.lintford.library.core.graphics.sprites.spritesheet.SpriteSheetDefinition;
import net.lintford.library.core.graphics.textures.Texture;
import net.lintford.library.core.maths.Matrix4f;
import net.lintford.library.renderers.BaseRenderer;
import net.lintford.library.renderers.RendererManager;

public class LevelBackgroundRenderer extends BaseRenderer {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String RENDERER_NAME = "Level Background Renderer";
	private static final String FragShaderFilename = "res/shaders/shaderUnderwater.frag";

	// --------------------------------------
	// Variable
	// --------------------------------------

	private LevelController mLevelController;
	private Texture mLevelBackground;
	private Texture mUnderwaterBackground;
	private SpriteSheetDefinition mOceanSpritesheet;
	private SpriteInstance mWaveSpriteInstance;

	private ShaderMVP_PT mUnderwaterShader;
	private FullScreenTexturedQuad mFullScreenQuad;

	// --------------------------------------
	// Properties
	// --------------------------------------

	@Override
	public boolean isInitialized() {
		return mLevelController != null;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public LevelBackgroundRenderer(RendererManager pRendererManager, int pEntityGroupID) {
		super(pRendererManager, RENDERER_NAME, pEntityGroupID);

		mUnderwaterShader = new ShaderMVP_PT("SHADER_UNDERWATER", ShaderMVP_PT.BASIC_VERT_FILENAME, FragShaderFilename);
		mFullScreenQuad = new FullScreenTexturedQuad();
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void initialize(LintfordCore pCore) {
		final var lControllerManager = pCore.controllerManager();

		mLevelController = (LevelController) lControllerManager.getControllerByNameRequired(LevelController.CONTROLLER_NAME, entityGroupID());
	}

	@Override
	public void loadGLContent(ResourceManager pResourceManager) {
		super.loadGLContent(pResourceManager);

		mFullScreenQuad.loadGLContent(pResourceManager);

		mLevelBackground = pResourceManager.textureManager().getTexture("TEXTURE_GAME_BACKGROUND", entityGroupID());
		mUnderwaterBackground = pResourceManager.textureManager().getTexture("TEXTURE_UNDERWATER_BACKGROUND", entityGroupID());

		mOceanSpritesheet = pResourceManager.spriteSheetManager().loadSpriteSheet("res/spritesheets/spritesheetOcean.json", entityGroupID());
		mWaveSpriteInstance = mOceanSpritesheet.getSpriteInstance("waves_small");

		mUnderwaterShader.loadGLContent(pResourceManager);
	}

	@Override
	public void unloadGLContent() {
		super.unloadGLContent();

		mFullScreenQuad.unloadGLContent();

		mLevelBackground = null;
		mOceanSpritesheet = null;
		mWaveSpriteInstance = null;

		mUnderwaterShader.unloadGLContent();
	}

	@Override
	public void update(LintfordCore pCore) {
		super.update(pCore);

		if (mWaveSpriteInstance != null)
			mWaveSpriteInstance.update(pCore);
	}

	@Override
	public void draw(LintfordCore pCore) {
		if (!isInitialized())
			return;

		final var lCameraRect = pCore.gameCamera().boundingRectangle();

		if (pCore.input().keyboard().isKeyDown(GLFW.GLFW_KEY_U)) {
			mUnderwaterShader.recompile();
		}

		drawUnderwaterEffect(pCore);

		final var lTextureBatch = rendererManager().uiTextureBatch();
		lTextureBatch.begin(pCore.gameCamera());
		lTextureBatch.draw(mLevelBackground, 0, 0, 960, 540, lCameraRect, -0.9f, ColorConstants.WHITE);
		lTextureBatch.end();

		final var lSpriteBatch = rendererManager().uiSpriteBatch();

		final float lSeaLevel = mLevelController.seaLevel();

		final float lWavesWidth = mWaveSpriteInstance.width() * 1.5f;
		final float lWavesHeight = mWaveSpriteInstance.height() * 1.5f;

		lSpriteBatch.begin(pCore.gameCamera());

		final var lWaterColor = ColorConstants.getWhiteWithAlpha(1f);

		final float lLeftEdge = lCameraRect.left() - lWavesWidth;
		for (int t = (int) lLeftEdge; t < lLeftEdge + 100 + lCameraRect.w() + lWavesWidth * 2; t += (int) lWavesWidth) {
			lSpriteBatch.draw(mOceanSpritesheet.texture(), mWaveSpriteInstance.currentSpriteFrame(), t, lSeaLevel - 5f - lWavesHeight * .5f, lWavesWidth, lWavesHeight, -0.9f, lWaterColor);
		}

		lSpriteBatch.end();
	}

	private void drawUnderwaterEffect(LintfordCore pCore) {

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, mUnderwaterBackground.getTextureID());

		mFullScreenQuad.zDepth(-.9f);
		mFullScreenQuad.createModelMatrix();

		mUnderwaterShader.projectionMatrix(pCore.HUD().projection());
		mUnderwaterShader.viewMatrix(Matrix4f.IDENTITY);
		mUnderwaterShader.modelMatrix(mFullScreenQuad.modelMatrix());
		mUnderwaterShader.bind();

		final float lScreenWidth = pCore.config().display().windowWidth();
		final float lScreenHeight = pCore.config().display().windowHeight();
		final float lTimeAcc = (float) (pCore.gameTime().totalTimeMilli() * .0001f);

		final float lCamPosX = pCore.gameCamera().getPosition().x * 0.001f;
		final float lCamPosY = -pCore.gameCamera().getPosition().y * 0.001f;

		GL20.glUniform2f(GL20.glGetUniformLocation(mUnderwaterShader.shaderID(), "screenDimensions"), lScreenWidth, lScreenHeight);
		GL20.glUniform2f(GL20.glGetUniformLocation(mUnderwaterShader.shaderID(), "cameraPosition"), lCamPosX, lCamPosY);
		GL20.glUniform1f(GL20.glGetUniformLocation(mUnderwaterShader.shaderID(), "time"), lTimeAcc);

		mFullScreenQuad.draw(pCore);

		mUnderwaterShader.unbind();
	}

}
