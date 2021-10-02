package net.ld.unstable.data.mobs;

import net.ld.unstable.data.mobs.patterns.MobMovementPattern;
import net.lintford.library.core.geometry.spritegraph.AnimatedSpriteGraphListener;
import net.lintford.library.core.geometry.spritegraph.instance.SpriteGraphInstance;
import net.lintford.library.core.graphics.sprites.SpriteDefinition;
import net.lintford.library.core.particles.particleemitters.ParticleEmitterInstance;

public class Submarine implements AnimatedSpriteGraphListener {

	// --------------------------------------
	// Variables
	// --------------------------------------

	public MobMovementPattern movementPattern;
	public int uid;

	public boolean isAlive;

	public float dx;
	public float dy;

	public float worldPositionX;
	public float worldPositionY;

	public float baseWorldPositionX;
	public float baseWorldPositionY;

	public float minCollisionDistance = 52.f; // 25.0 front and back + 25 radius + insecurity
	public boolean isPlayerControlled;

	public float health;
	public float coolant;

	public float invulnerabilityTimer;
	public float flashTimer;
	public boolean flashOn;

	public float shootTimer;
	public float barrelTimer;
	public float missileTimer;

	public float timeSinceStart;

	public boolean spriteGraphDirty;
	private transient SpriteGraphInstance mSpriteGraphInstance;
	public transient ParticleEmitterInstance bubbleEmitter;

	// --------------------------------------
	// Properties
	// --------------------------------------

	public SpriteGraphInstance spriteGraphInstance() {
		return mSpriteGraphInstance;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public Submarine() {
		reset();
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	public void init(int pUid) {
		uid = pUid;
		isAlive = true;
	}

	public void reset() {
		isAlive = false;
		uid = -1;
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
