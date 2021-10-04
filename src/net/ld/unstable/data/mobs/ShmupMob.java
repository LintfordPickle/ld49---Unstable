package net.ld.unstable.data.mobs;

import net.ld.unstable.data.mobs.definitions.MobDefinition;
import net.ld.unstable.data.mobs.movementpatterns.MovingDefBase;
import net.ld.unstable.data.mobs.shootprofiles.ShootingDefBase;
import net.lintford.library.core.geometry.spritegraph.AnimatedSpriteGraphListener;
import net.lintford.library.core.geometry.spritegraph.instance.SpriteGraphInstance;
import net.lintford.library.core.graphics.sprites.SpriteDefinition;
import net.lintford.library.core.maths.Vector2f;
import net.lintford.library.core.particles.particleemitters.ParticleEmitterInstance;

public class ShmupMob implements AnimatedSpriteGraphListener {

	public boolean collides(float pPointX, float pPointY, float pPointRadius) {
		if (mobDefinition == null)
			return false;

		final float lMinMobCol = pPointRadius + minColDistance();

		if (Vector2f.distance2(worldPositionX, worldPositionY, pPointX, pPointY) > lMinMobCol * lMinMobCol)
			return false;

		if (mobDefinition.largeCollisionEntity == false)
			return true;

		final float lSubFrontX = worldPositionX + pPointRadius;
		final float lSubFrontY = worldPositionY;
		final float lMinMobCol2 = pPointRadius + 25.f;

		final float lSubRearX = worldPositionX - pPointRadius;
		final float lSubRearY = worldPositionY;
		final float lMinMobCol3 = pPointRadius + 25.f;

		final boolean lCollisionFront = (Vector2f.distance2(lSubFrontX, lSubFrontY, pPointX, pPointY) < lMinMobCol2 * lMinMobCol2);
		final boolean lCollisionRear = (Vector2f.distance2(lSubRearX, lSubRearY, pPointX, pPointY) < lMinMobCol3 * lMinMobCol3);

		return lCollisionFront || lCollisionRear;

	}

	public float minColDistance() {
		if (mobDefinition == null)
			return 0.f;
		if (mobDefinition.largeCollisionEntity) {
			return mobDefinition.collisionRadius * 2.f;
		}
		return mobDefinition.collisionRadius;
	}

	// --------------------------------------
	// Variables
	// --------------------------------------

	public MovingDefBase movementPattern;
	public ShootingDefBase shootingPattern;

	public int shooterUid;

	public boolean isAlive;

	public float dx;
	public float dy;

	public float worldPositionX;
	public float worldPositionY;

	public float baseWorldPositionX;
	public float baseWorldPositionY;

	public boolean isPlayerControlled;

	public int health;
	public int coolant;
	public float coolantTimer;

	public float invulnerabilityTimer;
	public float flashTimer;
	public boolean flashOn;

	public float shootTimer;
	public float barrelTimer;
	public float missileTimer;

	public float eventTimer00;
	public float eventTimer01;
	public float eventTimer02;

	public float timeSinceStart;

	public boolean spriteGraphDirty;
	private transient SpriteGraphInstance mSpriteGraphInstance;

	public transient ParticleEmitterInstance bubbleEmitter;

	public MobDefinition mobDefinition;

	// --------------------------------------
	// Properties
	// --------------------------------------

	public SpriteGraphInstance spriteGraphInstance() {
		return mSpriteGraphInstance;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public ShmupMob() {
		reset();
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	public void init(int pShooterUid, MobDefinition pMobDefinition) {
		shooterUid = pShooterUid;
		isAlive = true;
		mobDefinition = pMobDefinition;
	}

	public void dealDamage(int pDamage) {
		health -= pDamage;
		coolant -= pDamage * 2;
	}

	public void reset() {
		isAlive = false;
		shooterUid = -1;
		worldPositionX = -500;
		worldPositionY = -500;
		worldPositionX = -500;
		baseWorldPositionX = -500;
		baseWorldPositionY = -500;
		timeSinceStart = 0;
		health = -1;
		movementPattern = null;
		mSpriteGraphInstance = null;
		spriteGraphDirty = true;
		mobDefinition = null;
	}

	public void kill() {
		reset();
	}

	public void setSpriteGraphInstance(SpriteGraphInstance pSpriteGraphInstance) {
		mSpriteGraphInstance = pSpriteGraphInstance;
	}

	@Override
	public void onSpriteAnimationStarted(SpriteGraphInstance pSender, SpriteDefinition pSpriteDefinition) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSpriteAnimationLooped(SpriteGraphInstance pSender, SpriteDefinition pSpriteDefinition) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSpriteAnimationStopped(SpriteGraphInstance pSender, SpriteDefinition pSpriteDefinition) {
		// TODO Auto-generated method stub

	}

}
