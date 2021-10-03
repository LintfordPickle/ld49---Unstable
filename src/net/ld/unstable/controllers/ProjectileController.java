package net.ld.unstable.controllers;

import java.util.ArrayList;
import java.util.List;

import net.ld.unstable.data.mobs.shootprofiles.ShootingDefEnemyBoat;
import net.ld.unstable.data.mobs.shootprofiles.ShootingDefEnemySubmarine;
import net.ld.unstable.data.mobs.shootprofiles.ShootingDefEnemyTurret;
import net.ld.unstable.data.mobs.shootprofiles.ShootingDefEnemyTurretBoat;
import net.ld.unstable.data.projectiles.Projectile;
import net.ld.unstable.data.projectiles.ProjectileManager;
import net.ld.unstable.data.projectiles.modifiers.BarrelPhysicsModifier;
import net.ld.unstable.data.projectiles.patterns.CosShooter;
import net.ld.unstable.data.projectiles.patterns.EnemyBulletStraightShooter;
import net.ld.unstable.data.projectiles.patterns.PlayerMissileShooter;
import net.ld.unstable.data.projectiles.patterns.PlayerTorpedoStraightShooter;
import net.ld.unstable.data.projectiles.patterns.ProjectilePattern;
import net.ld.unstable.data.projectiles.patterns.SinShooter;
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

	public static final int PLAYER_BULLETS_UID = 1;
	public static final int ENEMY_BULLETS_UID = 2;

	public static final ShootingDefEnemyBoat shootingProfileBoat = new ShootingDefEnemyBoat();
	public static final ShootingDefEnemyTurretBoat shootingProfileTurretBoat = new ShootingDefEnemyTurretBoat();
	public static final ShootingDefEnemySubmarine shootingProfileSubmarine = new ShootingDefEnemySubmarine();
	public static final ShootingDefEnemyTurret shootingProfileTurret = new ShootingDefEnemyTurret();

	// --------------------------------------
	// Variables
	// --------------------------------------

	private MobController mMobController;
	private ProjectileManager mProjectileManager;
	private ParticleFrameworkController mParticleFrameworkController;

	private ParticleSystemInstance mBubbleParticleSystem;
	private ParticleSystemInstance mBarrels;

	private final List<Projectile> projectileUpdateList = new ArrayList<>();
	private final List<ProjectilePattern> patterns = new ArrayList<>();

	PlayerMissileShooter missileShot;
	PlayerTorpedoStraightShooter strightShot;
	EnemyBulletStraightShooter straightShotEnemy;
	SinShooter sinShot;
	CosShooter cosShot;

	// --------------------------------------
	// Properties
	// --------------------------------------

	public ProjectileManager projectileManager() {
		return mProjectileManager;
	}

	@Override
	public boolean isInitialized() {
		// TODO Auto-generated method stub
		return false;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public ProjectileController(ControllerManager pControllerManager, ProjectileManager projectiles, int pEntityGroupUid) {
		super(pControllerManager, CONTROLLER_NAME, pEntityGroupUid);
		mProjectileManager = projectiles;
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void initialize(LintfordCore pCore) {
		final var lControllerManager = pCore.controllerManager();
		mParticleFrameworkController = (ParticleFrameworkController) lControllerManager.getControllerByNameRequired(ParticleFrameworkController.CONTROLLER_NAME, entityGroupID());
		mMobController = (MobController) lControllerManager.getControllerByNameRequired(MobController.CONTROLLER_NAME, entityGroupID());

		mBubbleParticleSystem = mParticleFrameworkController.particleFrameworkData().particleSystemManager().getParticleSystemByName("PARTICLESYSTEM_BUBBLE_SMALL");
		mBarrels = mParticleFrameworkController.particleFrameworkData().particleSystemManager().getParticleSystemByName("PARTICLESYSTEM_BARREL");

		mBubbleParticleSystem.addModifier(new ParticlePhysicsModifier());
		mBarrels.addModifier(new BarrelPhysicsModifier());

		var lLevelController = (LevelController) lControllerManager.getControllerByNameRequired(LevelController.CONTROLLER_NAME, entityGroupID());

		missileShot = new PlayerMissileShooter(lLevelController.seaLevel());
		strightShot = new PlayerTorpedoStraightShooter(lLevelController.seaLevel());
		straightShotEnemy = new EnemyBulletStraightShooter();
		sinShot = new SinShooter();
		cosShot = new CosShooter();

		// Patterns
		patterns.add(strightShot);
		patterns.add(missileShot);
		patterns.add(sinShot);
		patterns.add(cosShot);
		patterns.add(straightShotEnemy);
	}

	@Override
	public void unload() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(LintfordCore pCore) {
		super.update(pCore);

		checkCollisionsWithProjectiles(pCore);
		checkCollisionsWithBarrels(pCore);

		// Handle projectile lifetime
		updateProjectiles(pCore);

		updateProjectilePatterns(pCore);
	}

	private void updateProjectiles(LintfordCore pCore) {
		projectileUpdateList.clear();

		final var lProjectiles = projectileManager().projectiles();
		final int lNumProjectiles = lProjectiles.size();

		for (int i = 0; i < lNumProjectiles; i++) {
			projectileUpdateList.addAll(lProjectiles);
		}

		for (int i = 0; i < lNumProjectiles; i++) {
			final var lProjectile = projectileUpdateList.get(i);
			if (lProjectile.isAssigned() == false)
				continue;

			lProjectile.timeSinceStart += pCore.gameTime().elapsedTimeMilli();
			if (lProjectile.timeSinceStart > lProjectile.lifeTime()) {
				lProjectile.reset();
				continue;
			}

			if (RandomNumbers.getRandomChance(25f)) {
				mBubbleParticleSystem.spawnParticle(lProjectile.worldPositionX, lProjectile.worldPositionY, 0.f, 0.f);
			}
		}
	}

	private void updateProjectilePatterns(LintfordCore pCore) {
		final int lPatternCount = patterns.size();
		for (int i = 0; i < lPatternCount; i++) {
			final var lPattern = patterns.get(i);
			lPattern.update(pCore);
		}
	}

	// --------------------------------------

	private void checkCollisionsWithBarrels(LintfordCore pCore) {
		final float lTorpedoRadius = 10.f;

		final var lParticles = mBarrels.particles();
		final int lNumParticles = lParticles.size();
		for (int i = 0; i < lNumParticles; i++) {
			if (!lParticles.get(i).isAssigned())
				continue;
			final var lProjectile = lParticles.get(i);

			// collisions only count afer .05 second of life
			if (lProjectile.timeSinceStart > 1500) {
				if (checkProjectileCollisionsWithSubmarines(-1, lProjectile.worldPositionX, lProjectile.worldPositionY, lTorpedoRadius)) {
					lProjectile.reset();
					// TODO: Minor Explosion
					continue;
				}
			}
		}
	}

	private void checkCollisionsWithProjectiles(LintfordCore pCore) {
		final float lTorpedoRadius = 10.f;

		final var lProjectiles = mProjectileManager.projectiles();
		final int lNumProjectiles = lProjectiles.size();
		for (int i = 0; i < lNumProjectiles; i++) {
			if (!lProjectiles.get(i).isAssigned())
				continue;
			final var lProjectile = lProjectiles.get(i);

			// collisions only count afer .05 second of life
			if (lProjectile.timeSinceStart > 50) {
				if (checkProjectileCollisionsWithSubmarines(lProjectile.shooterUid, lProjectile.worldPositionX, lProjectile.worldPositionY, lTorpedoRadius)) {
					lProjectile.reset();
					// TODO: Minor Explosion
					continue;
				}
			}
		}
	}

	private boolean checkProjectileCollisionsWithSubmarines(int pProjShooterUid, float pProjX, float pProjY, float pProjRadius) {
		final var lMobs = mMobController.mobManager().mobs();
		final var lNumMobs = lMobs.size();
		for (int j = 0; j < lNumMobs; j++) {
			final var lMobInstance = lMobs.get(j);
			if (lMobInstance.invulnerabilityTimer > 0.f || lMobInstance.shooterUid == pProjShooterUid)
				continue;

			final float lMinMobCol = pProjRadius + lMobInstance.minCollisionDistance;

			if (Vector2f.distance2(lMobInstance.worldPositionX, lMobInstance.worldPositionY, pProjX, pProjY) > lMinMobCol * lMinMobCol)
				continue;

			final float lSubFrontX = lMobInstance.worldPositionX + 25.f;
			final float lSubFrontY = lMobInstance.worldPositionY;
			final float lMinMobCol2 = pProjRadius + 25.f;

			final float lSubRearX = lMobInstance.worldPositionX - 25.f;
			final float lSubRearY = lMobInstance.worldPositionY;
			final float lMinMobCol3 = pProjRadius + 25.f;

			final boolean lCollisionFront = (Vector2f.distance2(lSubFrontX, lSubFrontY, pProjX, pProjY) < lMinMobCol2 * lMinMobCol2);
			final boolean lCollisionRear = (Vector2f.distance2(lSubRearX, lSubRearY, pProjX, pProjY) < lMinMobCol3 * lMinMobCol3);

			if (lCollisionFront || lCollisionRear) {
				lMobInstance.invulnerabilityTimer = 100.f;

				if (!lMobInstance.isPlayerControlled)
					lMobInstance.health -= 10.f;
				return true;
			}
		}

		return false;
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	public void shootMissile(int pShooterUid, float pStartX, float pStartY, float pVX, float pVY) {
		final float lOffsetY = RandomNumbers.random(-4.0f, 4.0f);
		final var lMissile = mProjectileManager.spawnParticle(pStartX + 25.f, pStartY + lOffsetY - 30.f, pVX, pVY, 2000.0f);
		lMissile.setupSourceTexture(48, 16, 46, 10);
		lMissile.shooterUid = pShooterUid;

		missileShot.addProjectile(lMissile);
	}

	public void shootTorpedo(int pShooterUid, float pStartX, float pStartY, float pVX, float pVY) {
		final float lOffsetY = RandomNumbers.random(-4.0f, 4.0f);
		final var lTorpedo = mProjectileManager.spawnParticle(pStartX + 25.f, pStartY + lOffsetY + 30.f, pVX, pVY, 2000.0f);
		lTorpedo.setupSourceTexture(0, 16, 46, 10);
		lTorpedo.shooterUid = pShooterUid;

		strightShot.addProjectile(lTorpedo);
	}

	public void shootEnemyBullet(int pShooterUid, float pStartX, float pStartY, float pVX, float pVY) {
		final float lOffsetY = RandomNumbers.random(-4.0f, 4.0f);
		final var lTorpedo = mProjectileManager.spawnParticle(pStartX, pStartY + lOffsetY, pVX, pVY, 8000.0f);
		lTorpedo.setupSourceTexture(32, 0, 16, 16);
		lTorpedo.shooterUid = pShooterUid;

		straightShotEnemy.addProjectile(lTorpedo);
	}

	public void dropBarrel(int pShooterUid, float pStartX, float pStartY) {
		final float lOffsetY = RandomNumbers.random(-4.0f, 4.0f);

		final float lSignum = RandomNumbers.randomSign();
		final float lStr = RandomNumbers.random(-1.f, 1.f);
		mBarrels.spawnParticle(pStartX, pStartY + lOffsetY, 50.f * lSignum + lStr, 60.f);
	}
}
