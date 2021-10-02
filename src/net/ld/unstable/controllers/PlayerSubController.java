package net.ld.unstable.controllers;

import org.lwjgl.glfw.GLFW;

import net.ld.unstable.data.MobManager;
import net.ld.unstable.data.ShmupEntity;
import net.lintford.library.controllers.BaseController;
import net.lintford.library.controllers.core.ControllerManager;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.maths.Vector2f;

public class PlayerSubController extends BaseController {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String CONTROLLER_NAME = "Player Submarine Controller";

	// --------------------------------------
	// Variables
	// --------------------------------------

	private LevelController mLevelController;
	private final MobManager mMobManager;
	private final ShmupEntity mPlayerSubmarine;
	private final Vector2f mAcceleration = new Vector2f();
	private final Vector2f mVelocity = new Vector2f();

	// --------------------------------------
	// Properties
	// --------------------------------------

	public ShmupEntity playerSubmarine() {
		return mPlayerSubmarine;
	}

	@Override
	public boolean isInitialized() {
		// TODO Auto-generated method stub
		return false;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public PlayerSubController(ControllerManager pControllerManager, MobManager pMobManager, int pEntityGroupID) {
		super(pControllerManager, CONTROLLER_NAME, pEntityGroupID);

		mMobManager = pMobManager;
		mPlayerSubmarine = mMobManager.getPlayerSubmarine();
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
	public void unload() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean handleInput(LintfordCore pCore) {

		final float lMovementSpeed = 0.25f;
		final boolean isUnderWater = mPlayerSubmarine.y > mLevelController.seaLevel();

		if (isUnderWater && pCore.input().keyboard().isKeyDown(GLFW.GLFW_KEY_W)) {
			mAcceleration.y -= lMovementSpeed;
		}

		if (pCore.input().keyboard().isKeyDown(GLFW.GLFW_KEY_S)) {
			mAcceleration.y += lMovementSpeed;
		}

		if (isUnderWater && pCore.input().keyboard().isKeyDown(GLFW.GLFW_KEY_A)) {
			mAcceleration.x -= lMovementSpeed;
		}

		if (isUnderWater && pCore.input().keyboard().isKeyDown(GLFW.GLFW_KEY_D)) {
			mAcceleration.x += lMovementSpeed;
		}

		return super.handleInput(pCore);
	}

	@Override
	public void update(LintfordCore pCore) {
		super.update(pCore);

		final boolean isUnderWater = mPlayerSubmarine.y > mLevelController.seaLevel();

		mVelocity.x += mAcceleration.x;
		mVelocity.y += mAcceleration.y;

		if (mPlayerSubmarine.y < mLevelController.seaLevel()) {
			mVelocity.y += .12f;
		}

		mPlayerSubmarine.x += mVelocity.x;
		mPlayerSubmarine.y += mVelocity.y;

		if (isUnderWater)
			mVelocity.x *= .958f;

		mVelocity.y *= .958f;
		mAcceleration.set(0, 0);
	}
}
