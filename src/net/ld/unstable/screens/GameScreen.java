package net.ld.unstable.screens;

import org.lwjgl.glfw.GLFW;

import net.ld.unstable.controllers.GameStateController;
import net.ld.unstable.controllers.LevelController;
import net.ld.unstable.controllers.PlayerSubController;
import net.ld.unstable.controllers.ProjectileController;
import net.ld.unstable.controllers.MobController;
import net.ld.unstable.controllers.WaveController;
import net.ld.unstable.data.mobs.MobManager;
import net.ld.unstable.data.mobs.definitions.MobDefPlayerSubmarine;
import net.ld.unstable.data.projectiles.ProjectileManager;
import net.ld.unstable.data.projectiles.initializers.BubbleParticleInitializer;
import net.ld.unstable.data.projectiles.modifiers.BubblePhysicsModifier;
import net.ld.unstable.data.waves.WaveManager;
import net.ld.unstable.renderers.HudRenderer;
import net.ld.unstable.renderers.LevelRenderer;
import net.ld.unstable.renderers.ProjectilesRenderer;
import net.ld.unstable.renderers.MobRenderer;
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
	private WaveManager mWaveManager;
	private ProjectileManager mProjectileSystemManager;

	// Controllers
	private SpriteGraphController mSpriteGraphController;
	private MobController mMobController;
	private PlayerSubController mPlayerSubController;
	private LevelController mLevelController;
	private WaveController mWaveController;
	private ProjectileController mProjectileController;
	private ParticleFrameworkController mParticleFrameworkController;
	private GameStateController mGameStateController;

	// Renderers
	private MobRenderer mMobRenderer;
	private LevelRenderer mLevelRenderer;
	private ParticleFrameworkRenderer mParticleFrameworkRenderer;
	private ProjectilesRenderer mProjectilesRenderer;
	private HudRenderer mHudRenderer;

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public GameScreen(ScreenManager pScreenManager) {
		super(pScreenManager);

		mShowBackgroundScreens = true;

		mSpriteGraphManager = new SpriteGraphManager();
		mWaveManager = new WaveManager();
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

		// TODO: Clean this mess
		pResourceManager.spriteSheetManager().loadSpriteSheet("res/spritesheets/spritesheetSubmarines.json", entityGroupID());
		pResourceManager.spriteSheetManager().loadSpriteSheet("res/spritesheets/spritesheetPropeller.json", entityGroupID());
		pResourceManager.spriteSheetManager().loadSpriteSheet("res/spritesheets/spritesheetPowercore.json", entityGroupID());
		pResourceManager.spriteSheetManager().loadSpriteSheet("res/spritesheets/spritesheetBoats.json", entityGroupID());
		pResourceManager.spriteSheetManager().loadSpriteSheet("res/spritesheets/spritesheetExplosions.json", entityGroupID());
		pResourceManager.spriteSheetManager().loadSpriteSheet("res/spritesheets/spritesheetBoatTurret.json", entityGroupID());
		pResourceManager.spriteSheetManager().loadSpriteSheet("res/spritesheets/spritesheetMines.json", entityGroupID());
		pResourceManager.spriteSheetManager().loadSpriteSheet("res/spritesheets/spritesheetTurrets.json", entityGroupID());

		initializeControllers();
		createRenderers(pResourceManager);

		mMobController.addNewMob(true, MobDefPlayerSubmarine.MOB_DEFINITION_NAME, 0, 0);

		//

		final var lBubbleParticlesLarge = mParticleFrameworkData.particleSystemManager().getParticleSystemByName("PARTICLESYSTEM_BUBBLE_LARGE");
		lBubbleParticlesLarge.addInitializer(new BubbleParticleInitializer());
		lBubbleParticlesLarge.addModifier(new BubblePhysicsModifier(mLevelController.seaLevel()));

		final var lBubbleParticlesSmall = mParticleFrameworkData.particleSystemManager().getParticleSystemByName("PARTICLESYSTEM_BUBBLE_SMALL");
		lBubbleParticlesSmall.addInitializer(new BubbleParticleInitializer());
		lBubbleParticlesSmall.addModifier(new BubblePhysicsModifier(mLevelController.seaLevel()));

		// Start game after everything has loaded
		mGameStateController.startGame();
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
	public void update(LintfordCore pCore, boolean pOtherScreenHasFocus, boolean pCoveredByOtherScreen) {
		super.update(pCore, pOtherScreenHasFocus, pCoveredByOtherScreen);

		if (pOtherScreenHasFocus || pCoveredByOtherScreen)
			return;

		if (mGameStateController.hasGameStarted() && mGameStateController.hasGameEnded()) {
			switch (mGameStateController.endConditionMet()) {
			case GameStateController.END_CONDITION_WON:
				screenManager.addScreen(new WonScreen(screenManager));
				return;

			case GameStateController.END_CONDITION_HEALTH:
				screenManager.addScreen(new LostHealthScreen(screenManager));
				return;

			case GameStateController.END_CONDITION_COOLANT:
				screenManager.addScreen(new LostCoolantScreen(screenManager));
				return;
			}
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
		mMobController = new MobController(lControllerManager, mMobManager, entityGroupID());
		mPlayerSubController = new PlayerSubController(lControllerManager, mMobManager, entityGroupID());
		mParticleFrameworkController = new ParticleFrameworkController(lControllerManager, mParticleFrameworkData, entityGroupID());
		mProjectileController = new ProjectileController(lControllerManager, mProjectileSystemManager, entityGroupID());
		mWaveController = new WaveController(lControllerManager, mWaveManager, entityGroupID());
		mGameStateController = new GameStateController(lControllerManager, entityGroupID());
	}

	private void initializeControllers() {
		final var lCore = screenManager.core();

		mSpriteGraphController.initialize(lCore);
		mLevelController.initialize(lCore);
		mMobController.initialize(lCore);
		mPlayerSubController.initialize(lCore);
		mParticleFrameworkController.initialize(lCore);
		mProjectileController.initialize(lCore);
		mWaveController.initialize(lCore);
		mGameStateController.initialize(lCore);
	}

	private void createRenderers(ResourceManager pResourceManager) {
		// FIXME: the call order is messed up here because of the way the SpriteGraphs depend on SpriteSheets etc.

		final var lCore = screenManager.core();

		mMobRenderer = new MobRenderer(rendererManager, entityGroupID());
		mMobRenderer.initialize(lCore);
		mMobRenderer.loadGLContent(pResourceManager);

		mProjectilesRenderer = new ProjectilesRenderer(rendererManager, entityGroupID());
		mProjectilesRenderer.initialize(lCore);
		mProjectilesRenderer.loadGLContent(pResourceManager);

		mParticleFrameworkRenderer = new ParticleFrameworkRenderer(rendererManager, entityGroupID());
		mParticleFrameworkRenderer.initialize(lCore);
		mParticleFrameworkRenderer.loadGLContent(pResourceManager);

		mLevelRenderer = new LevelRenderer(rendererManager, entityGroupID());
		mLevelRenderer.initialize(lCore);
		mLevelRenderer.loadGLContent(pResourceManager);

		mHudRenderer = new HudRenderer(rendererManager, entityGroupID());
		mHudRenderer.initialize(lCore);
		mHudRenderer.loadGLContent(pResourceManager);

	}
}