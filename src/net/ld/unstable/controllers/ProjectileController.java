package net.ld.unstable.controllers;

import java.util.ArrayList;
import java.util.List;

import net.ld.unstable.ConstantsGame;
import net.ld.unstable.data.explosions.ExplosionsController;
import net.ld.unstable.data.mobs.shootprofiles.ShootingDefEnemyBoat;
import net.ld.unstable.data.mobs.shootprofiles.ShootingDefEnemySubmarine;
import net.ld.unstable.data.mobs.shootprofiles.ShootingDefEnemyTurret;
import net.ld.unstable.data.mobs.shootprofiles.ShootingDefEnemyTurretBoat;
import net.ld.unstable.data.projectiles.Projectile;
import net.ld.unstable.data.projectiles.ProjectileManager;
import net.ld.unstable.data.projectiles.patterns.BarrelShooter;
import net.ld.unstable.data.projectiles.patterns.CosShooter;
import net.ld.unstable.data.projectiles.patterns.EnemyBulletStraightShooter;
import net.ld.unstable.data.projectiles.patterns.PlayerMissileShooter;
import net.ld.unstable.data.projectiles.patterns.PlayerTorpedoStraightShooter;
import net.ld.unstable.data.projectiles.patterns.ProjectilePattern;
import net.ld.unstable.data.projectiles.patterns.SinShooter;
import net.lintford.library.controllers.BaseController;
import net.lintford.library.controllers.core.ControllerManager;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.maths.RandomNumbers;

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

	private SoundFxController mSoundFxController;
	private LevelController mLevelController;
	private MobController mMobController;
	private ProjectileManager mProjectileManager;
	private ExplosionsController mExplosionController;
	private ScreenShakeController mScreenShakeController;

	private final List<Projectile> projectileUpdateList = new ArrayList<>();
	private final List<ProjectilePattern> patterns = new ArrayList<>();

	PlayerMissileShooter missileShot;
	PlayerTorpedoStraightShooter strightShot;
	EnemyBulletStraightShooter straightShotEnemy;
	SinShooter sinShot;
	CosShooter cosShot;
	BarrelShooter barrelShot;

	private float mSeaLevel;

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
		mMobController = (MobController) lControllerManager.getControllerByNameRequired(MobController.CONTROLLER_NAME, entityGroupID());
		mExplosionController = (ExplosionsController) lControllerManager.getControllerByNameRequired(ExplosionsController.CONTROLLER_NAME, entityGroupID());
		mLevelController = (LevelController) lControllerManager.getControllerByNameRequired(LevelController.CONTROLLER_NAME, entityGroupID());
		mScreenShakeController = (ScreenShakeController) lControllerManager.getControllerByNameRequired(ScreenShakeController.CONTROLLER_NAME, entityGroupID());
		mSoundFxController = (SoundFxController) lControllerManager.getControllerByNameRequired(SoundFxController.CONTROLLER_NAME, LintfordCore.CORE_ENTITY_GROUP_ID);

		mSeaLevel = mLevelController.seaLevel();

		var lLevelController = (LevelController) lControllerManager.getControllerByNameRequired(LevelController.CONTROLLER_NAME, entityGroupID());

		missileShot = new PlayerMissileShooter(lLevelController.seaLevel());
		strightShot = new PlayerTorpedoStraightShooter(lLevelController.seaLevel());
		straightShotEnemy = new EnemyBulletStraightShooter();
		sinShot = new SinShooter();
		cosShot = new CosShooter();
		barrelShot = new BarrelShooter();

		// Patterns
		patterns.add(strightShot);
		patterns.add(missileShot);
		patterns.add(sinShot);
		patterns.add(cosShot);
		patterns.add(straightShotEnemy);
		patterns.add(barrelShot);
	}

	@Override
	public void unload() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(LintfordCore pCore) {
		super.update(pCore);

		checkCollisionsWithProjectiles(pCore);

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

			boolean tempFlag = lProjectile.underWater;
			lProjectile.underWater = lProjectile.baseWorldPositionY > mSeaLevel;

			if (tempFlag != lProjectile.underWater && lProjectile.emitSurfacingParticles) {
				mSoundFxController.playSurfacingSound();
				mExplosionController.addSurfaceExplosion(lProjectile.baseWorldPositionX + lProjectile.colRadius, lProjectile.baseWorldPositionY);
			}

			if (lProjectile.emitSmokeBubbles && (lProjectile.timeSinceStart % 10) == 0) {
				mExplosionController.addSmallSmokeParticles(lProjectile.baseWorldPositionX - 25, lProjectile.baseWorldPositionY - 15);
			}

			if (lProjectile.emitSmokeTrail && (lProjectile.timeSinceStart % 3) == 0) {
				if (RandomNumbers.getRandomChance(75))
					mExplosionController.addSmallSmokeParticles(lProjectile.baseWorldPositionX - 25, lProjectile.baseWorldPositionY - 15);
				else
					mExplosionController.addSmokeParticles(lProjectile.baseWorldPositionX - 25, lProjectile.baseWorldPositionY - 15);
			}

			lProjectile.timeSinceStart += pCore.gameTime().elapsedTimeMilli();
			if (lProjectile.timeSinceStart > lProjectile.lifeTime()) {
				lProjectile.reset();
				continue;
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

	private void checkCollisionsWithProjectiles(LintfordCore pCore) {
		final var lProjectiles = mProjectileManager.projectiles();
		final int lNumProjectiles = lProjectiles.size();
		for (int i = 0; i < lNumProjectiles; i++) {
			if (!lProjectiles.get(i).isAssigned())
				continue;
			final var lProjectile = lProjectiles.get(i);

			if (lProjectile.isAssigned() == false || lProjectile.colRadius <= 0.f)
				continue;

			// collisions only count afer .05 second of life
			if (lProjectile.timeSinceStart > 50) {
				if (checkProjectileCollisionsWithSubmarines(lProjectile)) {
					mExplosionController.addMajorExplosion(lProjectile.baseWorldPositionX, lProjectile.baseWorldPositionY);

					lProjectile.reset();
					continue;
				}
			}
		}
	}

	private boolean checkProjectileCollisionsWithSubmarines(Projectile pProjectile) {
		if (pProjectile == null || pProjectile.isAssigned() == false)
			return false;

		final var lMobs = mMobController.mobManager().mobs();
		final var lNumMobs = lMobs.size();
		for (int j = 0; j < lNumMobs; j++) {
			final var lMobInstance = lMobs.get(j);

			if (lMobInstance.invulnerabilityTimer > 0.f || lMobInstance.shooterUid == pProjectile.shooterUid)
				continue;

			if (lMobInstance.collides(pProjectile.worldPositionX, pProjectile.worldPositionY, pProjectile.colRadius)) {
				lMobInstance.invulnerabilityTimer = 100.f;

				if (lMobInstance.isPlayerControlled) {
					mScreenShakeController.shakeCamera(250.f, 10.f);

					if (ConstantsGame.DEBUG_GOD_MODE)
						return true;
				}

				lMobInstance.dealDamage(pProjectile.damage, pProjectile.coolantDamage);
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
		lMissile.setupSourceTexture(52, 14, 46, 14);
		lMissile.shooterUid = pShooterUid;
		lMissile.emitSmokeTrail = true;
		lMissile.emitSmokeBubbles = false;
		lMissile.underWater = true;
		lMissile.colRadius = 15.f;
		lMissile.damage = 10;
		lMissile.coolantDamage = 25;
		lMissile.emitSurfacingParticles = true;
		mExplosionController.addSmokeParticles(pStartX + 25.f, pStartY + lOffsetY - 30.f);
		missileShot.addProjectile(lMissile);
		mSoundFxController.playShootingSound();
	}

	public void shootTorpedo(int pShooterUid, float pStartX, float pStartY, float pVX, float pVY) {
		final float lOffsetY = RandomNumbers.random(-4.0f, 4.0f);
		final var lTorpedo = mProjectileManager.spawnParticle(pStartX + 25.f, pStartY + lOffsetY + 30.f, pVX, pVY, 2000.0f);
		lTorpedo.setupSourceTexture(0, 59, 50, 18);
		lTorpedo.shooterUid = pShooterUid;
		lTorpedo.emitSmokeTrail = true;
		lTorpedo.emitSmokeBubbles = false;
		lTorpedo.underWater = true;
		lTorpedo.colRadius = 15.f;
		lTorpedo.damage = 10;
		lTorpedo.coolantDamage = 25;
		lTorpedo.emitSurfacingParticles = true;
		mExplosionController.addSmokeParticles(pStartX, pStartY + lOffsetY);
		strightShot.addProjectile(lTorpedo);
		mSoundFxController.playShootingSound();
	}

	public void shootEnemyBullet(int pShooterUid, float pStartX, float pStartY, float pVX, float pVY) {
		final float lOffsetY = RandomNumbers.random(-4.0f, 4.0f);
		final var lBullet = mProjectileManager.spawnParticle(pStartX, pStartY + lOffsetY, pVX, pVY, 8000.0f);
		lBullet.setupSourceTexture(32, 0, 16, 16);
		lBullet.shooterUid = pShooterUid;
		lBullet.emitSmokeTrail = false;
		lBullet.emitSmokeBubbles = false;
		lBullet.underWater = true;
		lBullet.colRadius = 6;
		lBullet.damage = 10;
		lBullet.coolantDamage = 25;
		lBullet.emitSurfacingParticles = false;
		straightShotEnemy.addProjectile(lBullet);
	}

	public void dropBarrel(int pShooterUid, float pStartX, float pStartY) {
		final float lOffsetY = RandomNumbers.random(-4.0f, 4.0f);
		final float lSignum = RandomNumbers.randomSign();
		final float lStr = RandomNumbers.random(-1.f, 1.f);
		final var lBarrel = mProjectileManager.spawnParticle(pStartX, pStartY + lOffsetY, 50.f * lSignum + lStr, 60f, 8000.0f);
		lBarrel.setupSourceTexture(0, 26, 32, 32);
		lBarrel.shooterUid = pShooterUid;
		lBarrel.emitSmokeBubbles = true;
		lBarrel.emitSmokeTrail = false;
		lBarrel.underWater = true;
		lBarrel.colRadius = 6;
		lBarrel.damage = 25;
		lBarrel.coolantDamage = 50;
		lBarrel.emitSurfacingParticles = false;
		lBarrel.dr = RandomNumbers.random(-120.f, 120.f);
		barrelShot.addProjectile(lBarrel);
	}
}
