package net.ld.unstable.controllers;

import org.lwjgl.glfw.GLFW;

import net.ld.unstable.data.MobManager;
import net.ld.unstable.data.ShmupEntity;
import net.lintford.library.controllers.BaseController;
import net.lintford.library.controllers.core.ControllerManager;
import net.lintford.library.core.LintfordCore;

public class PlayerSubController extends BaseController {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String CONTROLLER_NAME = "Player Submarine Controller";

	// --------------------------------------
	// Variables
	// --------------------------------------

	private final MobManager mMobManager;
	private final ShmupEntity mPlayerSubmarine;

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
		// TODO Auto-generated method stub

	}

	@Override
	public void unload() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean handleInput(LintfordCore pCore) {

		final float lMovementSpeed = 2.f;

		if (pCore.input().keyboard().isKeyDown(GLFW.GLFW_KEY_W)) {
			mPlayerSubmarine.y -= lMovementSpeed;
		}

		if (pCore.input().keyboard().isKeyDown(GLFW.GLFW_KEY_S)) {
			mPlayerSubmarine.y += lMovementSpeed;
		}

		if (pCore.input().keyboard().isKeyDown(GLFW.GLFW_KEY_A)) {
			mPlayerSubmarine.x -= lMovementSpeed;
		}

		if (pCore.input().keyboard().isKeyDown(GLFW.GLFW_KEY_D)) {
			mPlayerSubmarine.x += lMovementSpeed;
		}

		return super.handleInput(pCore);
	}

}
