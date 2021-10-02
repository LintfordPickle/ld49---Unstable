package net.ld.unstable.controllers;

import net.ld.unstable.data.projectiles.modifiers.BarrelPhysicsModifier;
import net.lintford.library.controllers.BaseController;
import net.lintford.library.controllers.core.ControllerManager;
import net.lintford.library.controllers.core.particles.ParticleFrameworkController;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.maths.RandomNumbers;
import net.lintford.library.core.maths.Vector2f;
import net.lintford.library.core.particles.particlesystems.ParticleSystemInstance;
import net.lintford.library.core.particles.particlesystems.modifiers.ParticlePhysicsModifier;

public class ProjectileController extends BaseController {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String CONTROLLER_NAME = "Projectile Controller";

	private SubController mSubController;
	private ParticleFrameworkController mParticleFrameworkController;

	private ParticleSystemInstance mTorpedoeParticleSystem;
	private ParticleSystemInstance mBarrels;
	private ParticleSystemInstance mMuzzleFlash00;

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

	public ProjectileController(ControllerManager pControllerManager, int pEntityGroupUid) {
		super(pControllerManager, CONTROLLER_NAME, pEntityGroupUid);
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void initialize(LintfordCore pCore) {
		final var lControllerManager = pCore.controllerManager();
		mParticleFrameworkController = (ParticleFrameworkController) lControllerManager.getControllerByNameRequired(ParticleFrameworkController.CONTROLLER_NAME, entityGroupID());
		mSubController = (SubController) lControllerManager.getControllerByNameRequired(SubController.CONTROLLER_NAME, entityGroupID());

		mTorpedoeParticleSystem = mParticleFrameworkController.particleFrameworkData().particleSystemManager().getParticleSystemByName("PARTICLESYSTEM_TORPEDO");
		mBarrels = mParticleFrameworkController.particleFrameworkData().particleSystemManager().getParticleSystemByName("PARTICLESYSTEM_BARREL");

		mTorpedoeParticleSystem.addModifier(new ParticlePhysicsModifier());
		mBarrels.addModifier(new BarrelPhysicsModifier());

	}

	@Override
	public void unload() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(LintfordCore pCore) {
		super.update(pCore);

		checkCollisionsWithTorpedoes(pCore);

		// Handle projectile lifetime
	}

	private void checkCollisionsWithTorpedoes(LintfordCore pCore) {
		final float lTorpedoRadius = 10.f;

		final var lTorpedoes = mTorpedoeParticleSystem.particles();
		final int lNumTorpedoes = lTorpedoes.size();
		for (int i = 0; i < lNumTorpedoes; i++) {
			if (!lTorpedoes.get(i).isAssigned())
				continue;
			final var lTorpedo = lTorpedoes.get(i);

			// collisions only count afer .05 second of life
			if (lTorpedo.timeSinceStart > 50) {
				if (checkProjCollisionsWithSubmarines(lTorpedo.worldPositionX, lTorpedo.worldPositionY, lTorpedoRadius)) {
					lTorpedo.reset();
					// TODO: Minor Explosion
					continue;
				}
			}
		}
	}

	private boolean checkProjCollisionsWithSubmarines(float pProjX, float pProjY, float pProjRadius) {
		final var lMobs = mSubController.mobManager().mobs();
		final var lNumMobs = lMobs.size();
		for (int j = 0; j < lNumMobs; j++) {
			final var lMobInstance = lMobs.get(j);
			if (lMobInstance.invulnerabilityTimer > 0.f)
				continue;

			final float lMinMobCol = pProjRadius + lMobInstance.minCollisionDistance;

			if (Vector2f.distance2(lMobInstance.x, lMobInstance.y, pProjX, pProjY) > lMinMobCol * lMinMobCol)
				continue;

			final float lSubFrontX = lMobInstance.x + 25.f;
			final float lSubFrontY = lMobInstance.y;
			final float lMinMobCol2 = pProjRadius + 25.f;

			final float lSubRearX = lMobInstance.x - 25.f;
			final float lSubRearY = lMobInstance.y;
			final float lMinMobCol3 = pProjRadius + 25.f;

			final boolean lCollisionFront = (Vector2f.distance2(lSubFrontX, lSubFrontY, pProjX, pProjY) < lMinMobCol2 * lMinMobCol2);
			final boolean lCollisionRear = (Vector2f.distance2(lSubRearX, lSubRearY, pProjX, pProjY) < lMinMobCol3 * lMinMobCol3);

			if (lCollisionFront || lCollisionRear) {
				lMobInstance.invulnerabilityTimer = 100.f;
				lMobInstance.health -= 10.f;
				return true;
			}
		}

		return false;
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	public void shootTorpedo(float pStartX, float pStartY) {
		final float lOffsetY = RandomNumbers.random(-4.0f, 4.0f);

		final var lTorpedo = mTorpedoeParticleSystem.spawnParticle(pStartX, pStartY + lOffsetY, 1500.f, 0.f);
	}

	public void dropBarrel(float pStartX, float pStartY) {
		final float lOffsetY = RandomNumbers.random(-4.0f, 4.0f);

		final float lSignum = RandomNumbers.randomSign();
		final float lStr = RandomNumbers.random(-1.f, 1.f);
		final var lBarrel = mBarrels.spawnParticle(pStartX, pStartY + lOffsetY, 50.f * lSignum + lStr, 60.f);
	}

}
