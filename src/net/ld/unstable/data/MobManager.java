package net.ld.unstable.data;

import java.util.ArrayList;
import java.util.List;

public class MobManager {

	private ShmupEntity mPlayerSubmarine;
	private final List<ShmupEntity> mMobEntities = new ArrayList<>();

	public ShmupEntity getPlayerSubmarine() {
		return mPlayerSubmarine;
	}

	public ShmupEntity getMobByIndex(int pIndex) {
		if (pIndex < 0 || pIndex >= mMobEntities.size()) {
			return null;
		}

		return mMobEntities.get(pIndex);
	}

	public void addNewPlayerSub(float x, float y) {
		mPlayerSubmarine = new ShmupEntity();
	}

	public void addNewMob(float x, float y, int type) {

	}

}
