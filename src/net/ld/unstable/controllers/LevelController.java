package net.ld.unstable.controllers;

import net.lintford.library.controllers.BaseController;
import net.lintford.library.controllers.core.ControllerManager;
import net.lintford.library.core.LintfordCore;

public class LevelController extends BaseController {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String CONTROLLER_NAME = "Level Controller";

	public static final float SCROLL_SPEED = 0.3f;

	// --------------------------------------
	// Variables
	// --------------------------------------

	private float mSeaLevel = -145.f;
	private float mWorldPositionX = 0;

	// --------------------------------------
	// Properties
	// --------------------------------------

	public float seaLevel() {
		return mSeaLevel;
	}

	public float worldPositionX() {
		return mWorldPositionX;
	}

	public float worldPositionY() {
		return 0.f;
	}

	@Override
	public boolean isInitialized() {
		return false;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public LevelController(ControllerManager pControllerManager, int pEntityGroupID) {
		super(pControllerManager, CONTROLLER_NAME, pEntityGroupID);

		mWorldPositionX = 0.f;
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void initialize(LintfordCore pCore) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unload() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(LintfordCore pCore) {
		super.update(pCore);

		final var lGameCamera = pCore.gameCamera();

		mWorldPositionX += SCROLL_SPEED;

		final var lScreenPositionX = mWorldPositionX;

		lGameCamera.setPosition(lScreenPositionX, 0);
	}
}
