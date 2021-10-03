package net.ld.unstable.controllers;

import net.lintford.library.controllers.BaseController;
import net.lintford.library.controllers.core.ControllerManager;
import net.lintford.library.core.LintfordCore;

public class GameStateController extends BaseController {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String CONTROLLER_NAME = "Game State Controller";

	public static final int END_CONDITION_WON = 1;
	public static final int END_CONDITION_COOLANT = 2;
	public static final int END_CONDITION_HEALTH = 3;

	// --------------------------------------
	// Variables
	// --------------------------------------

	private LevelController mLevelController;
	private MobController mMobController;
	private WaveController mWaveController;

	private boolean mHasGameStarted;
	private boolean mHasGameEnded;
	private int mGameEndCondition;

	// --------------------------------------
	// Properties
	// --------------------------------------

	public boolean hasGameStarted() {
		return mHasGameStarted && mWaveController.haveWavesStarted();
	}

	public boolean hasGameEnded() {
		return mHasGameEnded;
	}

	public int endConditionMet() {
		return mGameEndCondition;
	}

	@Override
	public boolean isInitialized() {
		return false;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public GameStateController(ControllerManager pControllerManager, int pEntityGroupID) {
		super(pControllerManager, CONTROLLER_NAME, pEntityGroupID);

	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void initialize(LintfordCore pCore) {
		final var lControllerManager = pCore.controllerManager();

		mLevelController = (LevelController) lControllerManager.getControllerByNameRequired(LevelController.CONTROLLER_NAME, entityGroupID());
		mMobController = (MobController) lControllerManager.getControllerByNameRequired(MobController.CONTROLLER_NAME, entityGroupID());
		mWaveController = (WaveController) lControllerManager.getControllerByNameRequired(WaveController.CONTROLLER_NAME, entityGroupID());
	}

	@Override
	public void unload() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(LintfordCore pCore) {
		super.update(pCore);

		if (!mHasGameStarted || mHasGameEnded)
			return;

		final var lPlayerSub = mMobController.mobManager().playerSubmarine;

		// check for a win condition
		if (mWaveController.hasWaveFinished()) {
			mHasGameEnded = true;
			mGameEndCondition = END_CONDITION_WON;
			return;
		}

		// Check for a lose condition
		if (lPlayerSub.health <= 0.f) {
			mHasGameEnded = true;
			mGameEndCondition = END_CONDITION_HEALTH;
		}
		if (lPlayerSub.coolant <= 0.f) {
			mHasGameEnded = true;
			mGameEndCondition = END_CONDITION_COOLANT;
		}

	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	public void startGame() {
		mHasGameStarted = true;
		mHasGameEnded = false;

		mWaveController.startNewGame();

	}
}
