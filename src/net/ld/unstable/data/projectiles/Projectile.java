package net.ld.unstable.data.projectiles;

import net.lintford.library.core.entity.instances.PreAllocatedInstanceData;
import net.lintford.library.core.graphics.Color;

public class Projectile extends PreAllocatedInstanceData {

	// --------------------------------------
	// Constants
	// --------------------------------------

	private static final long serialVersionUID = 820164057821427990L;

	public static final int DO_NOT_DESPAWN_LIFETIME = -1;
	public static final int NO_SHOOTER_UID = -1;

	// --------------------------------------
	// Variables
	// --------------------------------------

	private boolean mIsFree;
	public float timeSinceStart;
	private float mLifeTime;
	public int shooterUid;

	public float width;
	public float height;
	public float sx, sy, sw, sh; // The src tex rect

	public float rox;
	public float roy;

	public float odx, ody;
	public float dx, dy, dr;
	public final Color color = new Color();
	public float scale;

	public boolean emitSmokeTrail;
	public float baseWorldPositionX;
	public float baseWorldPositionY;

	public float worldPositionX;
	public float worldPositionY;

	public float rotationInRadians;

	// --------------------------------------
	// Properties
	// --------------------------------------

	public boolean isAssigned() {
		return !mIsFree;
	}

	/** Returns the amount of lifetime this particle was given when spawned */
	public float lifeTime() {
		return mLifeTime;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public Projectile() {
		reset();

	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	/** Setups the source texture area this particle will draw from, and the width/height of the particle. */
	public void setupSourceTexture(float pSX, float pSY, float pSW, float pSH) {
		sx = pSX;
		sy = pSY;
		sw = pSW;
		sh = pSH;

	}

	public void setupDestTexture(float pWidth, float pHeight) {
		width = pWidth;
		height = pHeight;

	}

	public void spawnParticle(float pWorldX, float pWorldY, float pVX, float pVY, float pLife) {
		mIsFree = false;
		mLifeTime = pLife;
		timeSinceStart = 0;

		sx = sy = 1;
		color.setRGBA(1.f, 1.f, 1.f, 1.f);

		odx = pVX;
		ody = pVY;
		baseWorldPositionX = pWorldX;
		baseWorldPositionY = pWorldY;

		worldPositionX = baseWorldPositionX;
		worldPositionY = baseWorldPositionY;
	}

	public void reset() {
		mIsFree = true;
		mLifeTime = 0;
		timeSinceStart = 0;
		scale = 1f;
		shooterUid = NO_SHOOTER_UID;
		baseWorldPositionX = 0;
		baseWorldPositionY = 0;
		worldPositionX = 0;
		worldPositionY = 0;

		odx = 0f;
		ody = 0f;
		dx = 0f;
		dy = 0f;
	}
}
