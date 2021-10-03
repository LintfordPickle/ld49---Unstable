package net.ld.unstable.data.waves;

import java.util.ArrayList;
import java.util.List;

import net.ld.unstable.data.mobs.ShmupMob;
import net.lintford.library.core.entity.instances.PooledBaseData;

public class Wave extends PooledBaseData {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	private static final long serialVersionUID = 8364190178780026740L;

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	public final List<ShmupMob> waveMembers = new ArrayList<>();
	private boolean mWaveCompleted;

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	public boolean isWaveCompleted() {
		return mWaveCompleted;
	}

	public boolean isWaveComplete() {
		return waveMembers.size() == 0;
	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public Wave(int pPoolUid) {
		super(pPoolUid);
	}

	// ---------------------------------------------
	// Core-Method
	// ---------------------------------------------

	public void startWave() {
		// TODO:
		for (int i = 0; i < 10; i++) {

		}
	}

	// ---------------------------------------------
	// Method
	// ---------------------------------------------

	@Override
	public void reset() {
		super.reset();

		mWaveCompleted = false;
	}
}
