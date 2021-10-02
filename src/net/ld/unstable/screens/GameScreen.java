package net.ld.unstable.screens;

import org.lwjgl.glfw.GLFW;

import net.ld.unstable.controllers.LevelController;
import net.ld.unstable.controllers.PlayerSubController;
import net.ld.unstable.controllers.ProjectileController;
import net.ld.unstable.controllers.SubController;
import net.ld.unstable.data.mobs.MobManager;
import net.ld.unstable.data.mobs.definitions.PlayerSubmarine;
import net.ld.unstable.data.projectiles.ProjectileManager;
import net.ld.unstable.data.projectiles.initializers.BubbleParticleInitializer;
import net.ld.unstable.data.projectiles.modifiers.BubblePhysicsModifier;
import net.ld.unstable.renderers.LevelRenderer;
import net.ld.unstable.renderers.ProjectilesRenderer;
import net.ld.unstable.renderers.SubRenderer;
import net.lintford.library.controllers.core.particles.ParticleFrameworkController;
import net.lintford.library.controllers.geometry.SpriteGraphController;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.geometry.spritegraph.SpriteGraphManager;
import net.lintford.library.core.particles.ParticleFrameworkData;
import net.lintford.library.renderers.particles.ParticleFrameworkRenderer;
import net.lintford.library.screenmanager.ScreenManager;
import net.lintford.library.screenmanager.screens.BaseGameScreen;

public class GameScreen extends BaseGameScreen {

	// --------------------------------------
	// Variable
	// --------------------------------------

	// Data
	private SpriteGraphManager mSpriteGraphManager;
	private MobManager mMobManager;
	private ParticleFrameworkData mParticleFrameworkData;

	private ProjectileManager mProjectileSystemManager;

	// Controllers
	private SpriteGraphController mSpriteGraphController;
	private SubController mSubController;
	private PlayerSubController mPlayerSubController;
	private LevelController mLevelController;
	private ProjectileController mProjectileController;
	private ParticleFrameworkController mParticleFrameworkController;

	// Renderers
	private SubRenderer mSubRenderer;
	private LevelRenderer mLevelRenderer;
	private ParticleFrameworkRenderer mParticleFrameworkRenderer;
	private ProjectilesRenderer mProjectilesRenderer;

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public GameScreen(ScreenManager pScreenManager) {
		super(pScreenManager);

		mShowBackgroundScreens = true;

		mSpriteGraphManager = new SpriteGraphManager();

		mMobManager = new MobManager();
		mParticleFrameworkData = new ParticleFrameworkData();
		mProjectileSystemManager = new ProjectileManager(1000);

		createControllers();
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void initialize() {
		super.initialize();

		mParticleFrameworkData.initialize(null);
		mMobManager.initialize();
	}

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

		mSubController.addNewSubmarine(true, PlayerSubmarine.MOB_DEFINITION_NAME, 0, 0);
		mSubController.addNewSubmarine(false, PlayerSubmarine.MOB_DEFINITION_NAME, 100, 0);

		//

		final var lBubbleParticlesLarge = mParticleFrameworkData.particleSystemManager().getParticleSystemByName("PARTICLESYSTEM_BUBBLE_LARGE");
		lBubbleParticlesLarge.addInitializer(new BubbleParticleInitializer());
		lBubbleParticlesLarge.addModifier(new BubblePhysicsModifier(mLevelController.seaLevel()));

		final var lBubbleParticlesSmall = mParticleFrameworkData.particleSystemManager().getParticleSystemByName("PARTICLESYSTEM_BUBBLE_SMALL");
		lBubbleParticlesSmall.addInitializer(new BubbleParticleInitializer());
		lBubbleParticlesSmall.addModifier(new BubblePhysicsModifier(mLevelController.seaLevel()));
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
		mParticleFrameworkController = new ParticleFrameworkController(lControllerManager, mParticleFrameworkData, entityGroupID());
		mProjectileController = new ProjectileController(lControllerManager, mProjectileSystemManager, entityGroupID());
	}

	private void initializeControllers() {
		mSpriteGraphController.initialize(screenManager.core());
		mLevelController.initialize(screenManager.core());
		mSubController.initialize(screenManager.core());
		mPlayerSubController.initialize(screenManager.core());
		mParticleFrameworkController.initialize(screenManager.core());
		mProjectileController.initialize(screenManager.core());
	}

	private void createRenderers(ResourceManager pResourceManager) {
		// FIXME: the call order is messed up here because of the way the SpriteGraphs depend on SpriteSheets etc.

		final var lCore = screenManager.core();

		mLevelRenderer = new LevelRenderer(rendererManager, entityGroupID());
		mLevelRenderer.initialize(lCore);
		mLevelRenderer.loadGLContent(pResourceManager);

		mParticleFrameworkRenderer = new ParticleFrameworkRenderer(rendererManager, entityGroupID());
		mParticleFrameworkRenderer.initialize(lCore);
		mParticleFrameworkRenderer.loadGLContent(pResourceManager);

		mProjectilesRenderer = new ProjectilesRenderer(rendererManager, entityGroupID());
		mProjectilesRenderer.initialize(lCore);
		mProjectilesRenderer.loadGLContent(pResourceManager);

		mSubRenderer = new SubRenderer(rendererManager, entityGroupID());
		mSubRenderer.initialize(lCore);
		mSubRenderer.loadGLContent(pResourceManager);

	}
}