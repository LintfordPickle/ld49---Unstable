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
		final boolean isUnderWater = lPlayerSub.worldPositionY > mLevelController.seaLevel();

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

		if (pCore.input().keyboard().isKeyDown(GLFW.GLFW_KEY_UP)) {
			if (lPlayerSub.missileTimer <= 0.f) {
				mProjectileController.shootMissile(ProjectileController.PLAYER_BULLETS_UID, lPlayerSub.worldPositionX, lPlayerSub.worldPositionY, 0, -1);
				lPlayerSub.missileTimer = 300;

			}
		}
		if (pCore.input().keyboard().isKeyDown(GLFW.GLFW_KEY_RIGHT)) {
			if (lPlayerSub.shootTimer <= 0.f) {
				mProjectileController.shootTorpedo(ProjectileController.PLAYER_BULLETS_UID, lPlayerSub.worldPositionX, lPlayerSub.worldPositionY, 1, 0);
				lPlayerSub.shootTimer = 150;

			}
		}
		if (pCore.input().keyboard().isKeyDown(GLFW.GLFW_KEY_DOWN)) {
			if (lPlayerSub.barrelTimer <= 0.f) {
				mProjectileController.dropBarrel(ProjectileController.PLAYER_BULLETS_UID, lPlayerSub.worldPositionX, lPlayerSub.worldPositionY);
				lPlayerSub.barrelTimer = 300;

			}
		}

		return super.handleInput(pCore);
	}

	@Override
	public void update(LintfordCore pCore) {
		super.update(pCore);

		if (mMobManager.playerSubmarine == null)
			return;

		final var lPlayerSubmarine = mMobManager.playerSubmarine;
		final boolean isUnderWater = lPlayerSubmarine.worldPositionY > mLevelController.seaLevel();

		// Keep the sub on screen
		final float lWorldPositionX = mLevelController.worldPositionX();

		final float lTolerance = 20.0f;
		if (lPlayerSubmarine.worldPositionX - lTolerance < lWorldPositionX - pCore.gameCamera().boundingRectangle().w() * .5f) {
			mAcceleration.x += 0.5f;
		}

		mVelocity.x += mAcceleration.x;
		mVelocity.y += mAcceleration.y;

		if (lPlayerSubmarine.worldPositionY < mLevelController.seaLevel()) {
			mVelocity.y += .12f;
		}

		lPlayerSubmarine.worldPositionX += mVelocity.x;
		lPlayerSubmarine.worldPositionY += mVelocity.y;

		if (isUnderWater)
			mVelocity.x *= .958f;

		mVelocity.y *= .958f;
		mAcceleration.set(0, 0);
	}
}
