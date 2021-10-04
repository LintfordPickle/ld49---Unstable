package net.ld.unstable.data.waves;

import java.util.ArrayList;
import java.util.List;

import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.debug.Debug;

public class WaveSpawner {

	// --------------------------------------
	// Constants
	// --------------------------------------

	private static final int POOL_MAX_SIZE = 100;

	// --------------------------------------
	// Inner-Classes
	// --------------------------------------

	public class MobSpawnBlock {
		public float x;
		public float y;
		public String mobDefName;
		public float spawnTimerInc; // inc. spawn timer (ms)
		
		
		public void reset() {
			x = 0;
			y = 0;
			mobDefName = null;
			spawnTimerInc = 0;
		}
	}

	// --------------------------------------
	// Variables
	// --------------------------------------

	private final List<MobSpawnBlock> pool = new ArrayList<>();
	public final List<MobSpawnBlock> spawners = new ArrayList<>();
	public IMobSpawner mMobSpawner;
	public float mSpawnTimer;

	// --------------------------------------
	// Properties
	// --------------------------------------

	public boolean isFinishedSpawning() {
		return spawners.size() == 0;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public WaveSpawner() {
		for (int i = 0; i < POOL_MAX_SIZE; i++) {
			pool.add(new MobSpawnBlock());
		}
	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	public void update(LintfordCore pCore) {
		if (isFinishedSpawning())
			return;

		mSpawnTimer -= pCore.gameTime().elapsedTimeMilli();
		if (mSpawnTimer <= 0) {
			final var lSpawnBlock = spawners.remove(0);
			if (mMobSpawner != null) {
				final float lWorldX = lSpawnBlock.x;
				final float lWorldY = lSpawnBlock.y;
				final String lMobDefName = lSpawnBlock.mobDefName;

				if(lMobDefName != null)
					Debug.debugManager().logger().w(getClass().getSimpleName(), "Spawing mob: " + lMobDefName);
				
				mMobSpawner.spawnMob(lWorldX, lWorldY, lMobDefName);
			} else {
				Debug.debugManager().logger().w(getClass().getSimpleName(), "Mob Spawner not assigned");
			}

			mSpawnTimer += lSpawnBlock.spawnTimerInc;

			lSpawnBlock.reset();
			pool.add(lSpawnBlock);
		}
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	public void addSpawnItem(float pX, float pY, float pSpawnIncTimer, String pMobdefName) {
		final var lSpawnBlock = getFreeSpawnBlock();
		if (lSpawnBlock == null) {
			// wait ?
			return;
		}

		lSpawnBlock.x = pX;
		lSpawnBlock.y = pY;
		lSpawnBlock.mobDefName = pMobdefName;
		lSpawnBlock.spawnTimerInc = pSpawnIncTimer;

		spawners.add(lSpawnBlock);
	}

	// --------------------------------------

	protected MobSpawnBlock getFreeSpawnBlock() {
		if (pool.size() > 0)
			return pool.remove(0);
		return null;
	}

	protected void returnSpawnBlock(MobSpawnBlock p) {
		if (pool.contains(p) == false)
			pool.add(p);
	}

}
