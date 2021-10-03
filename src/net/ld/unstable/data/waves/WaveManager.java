package net.ld.unstable.data.waves;

import net.lintford.library.core.entity.instances.PoolInstanceManager;

public class WaveManager extends PoolInstanceManager<Wave> {

	// --------------------------------------
	// Constants
	// --------------------------------------

	private static final long serialVersionUID = -490144760973042960L;

	// --------------------------------------
	// Variables
	// --------------------------------------

	private final WaveSpawner mWaveSpawner;
	private int mPoolCounter = 0;

	// --------------------------------------
	// Properties
	// --------------------------------------

	public WaveSpawner waveSpawner() {
		return mWaveSpawner;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public WaveManager() {
		mWaveSpawner = new WaveSpawner();
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	@Override
	protected Wave createPoolObjectInstance() {
		return new Wave(mPoolCounter++);
	}
}
