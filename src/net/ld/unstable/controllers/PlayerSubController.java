package net.ld.unstable.controllers;

import org.lwjgl.glfw.GLFW;

import net.ld.unstable.data.mobs.MobManager;
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

	private ProjectileController mProjectileController;
	private LevelController mLevelController;
	private final MobManager mMobManager;
	private final Vector2f mAcceleration = new Vector2f();
	private final Vector2f mVelocity = new Vector2f();

	// --------------------------------------
	// Properties
	// --------------------------------------

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
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void initialize(LintfordCore pCore) {
		final var lControllerManager = pCore.controllerManager();

		mLevelController = (LevelController) lControllerManager.getControllerByNameRequired(LevelController.CONTROLLER_NAME, entityGroupID());
		mProjectileController = (ProjectileController) lControllerManager.getControllerByNameRequired(ProjectileController.CONTROLLER_NAME, entityGroupID());
	}

	@Override
	public void unload() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean handleInput(LintfordCore pCore) {
		if (mMobManager.playerSubmarine == null)
			return false;

		final float lMovementSpeed = 0.25f;
		final var lPlayerSub = mMobManager.playerSubmarine;
		final boolean isUnderWater = lPlayerSub.y > mLevelController.seaLevel();

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

		if (isUnderWater && pCore.input().keyboard().isKeyDown(GLFW.GLFW_KEY_RIGHT)) {
			if(lPlayerSub.shootTimer <= 0.f) {
				mProjectileController.shootTorpedo(lPlayerSub.uid, lPlayerSub.x, lPlayerSub.y);
				lPlayerSub.shootTimer = 150;
				
			}
		}
		if (isUnderWater && pCore.input().keyboard().isKeyDown(GLFW.GLFW_KEY_DOWN)) {
			mProjectileController.dropBarrel(lPlayerSub.x, lPlayerSub.y);
		}

		return super.handleInput(pCore);
	}

	@Override
	public void update(LintfordCore pCore) {
		super.update(pCore);

		if (mMobManager.playerSubmarine == null)
			return;

//		{
//			// DEMO / DEBUG
//			final float lTimeMod = 0.001f;
//			final float sin = (float) Math.sin(pCore.gameTime().totalTimeMilli() * lTimeMod);
//			final float cos = (float) Math.cos(pCore.gameTime().totalTimeMilli() * lTimeMod);
//
//			mAcceleration.x = cos * .025f;
//			mAcceleration.y = sin * cos * .025f;
//
//		}

		final var lPlayerSubmarine = mMobManager.playerSubmarine;
		final boolean isUnderWater = lPlayerSubmarine.y > mLevelController.seaLevel();

		mVelocity.x += mAcceleration.x;
		mVelocity.y += mAcceleration.y;

		if (lPlayerSubmarine.y < mLevelController.seaLevel()) {
			mVelocity.y += .12f;
		}

		lPlayerSubmarine.x += mVelocity.x;
		lPlayerSubmarine.y += mVelocity.y;

//		mPlayerSubmarine.x = 0;
//		mPlayerSubmarine.y = 0;

		if (isUnderWater)
			mVelocity.x *= .958f;

		mVelocity.y *= .958f;
		mAcceleration.set(0, 0);
	}
}
