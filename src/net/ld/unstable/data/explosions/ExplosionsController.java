package net.ld.unstable.data.explosions;

import java.util.ArrayList;
import java.util.List;

import net.ld.unstable.controllers.SoundFxController;
import net.lintford.library.controllers.BaseController;
import net.lintford.library.controllers.core.ControllerManager;
import net.lintford.library.core.LintfordCore;

public class ExplosionsController extends BaseController {

	public static final String CONTROLLER_NAME = "Explosion Controller";

	private SoundFxController mSoundFxController;

	public class Explosion {
		public Explosion(float pWorldX, float pWorldY, String pAnimName) {
			worldX = pWorldX;
			worldY = pWorldY;
			animName = pAnimName;
		}

		public String animName;
		public float worldX;
		public float worldY;

	}

	public final List<Explosion> explosions = new ArrayList<>();

	public boolean hasUnprocessedExplosions() {
		return explosions.size() > 0;
	}

	public Explosion getExplosionToProcess() {
		return explosions.remove(0);
	}

	@Override
	public boolean isInitialized() {
		// TODO Auto-generated method stub
		return false;
	}

	public ExplosionsController(ControllerManager pControllerManager, int pEntityGroupUid) {
		super(pControllerManager, CONTROLLER_NAME, pEntityGroupUid);

	}

	@Override
	public void initialize(LintfordCore pCore) {
		final var lControllerManager = pCore.controllerManager();

		mSoundFxController = (SoundFxController) lControllerManager.getControllerByNameRequired(SoundFxController.CONTROLLER_NAME, LintfordCore.CORE_ENTITY_GROUP_ID);
	}

	@Override
	public void unload() {
		// TODO Auto-generated method stub

	}

	public void addSurfaceExplosion(float pWorldX, float pWorldY) {
		explosions.add(new Explosion(pWorldX - 64.f, pWorldY - 64.f, "surface"));
	}

	public void addSmallSmokeParticles(float pWorldX, float pWorldY) {
		explosions.add(new Explosion(pWorldX, pWorldY, "smoke_small"));
	}

	public void addSmokeParticles(float pWorldX, float pWorldY) {
		explosions.add(new Explosion(pWorldX, pWorldY, "smoke"));
	}

	public void addMajorExplosion(float pWorldX, float pWorldY) {
		mSoundFxController.playExplosionSound();
		explosions.add(new Explosion(pWorldX, pWorldY, "big"));
	}

	public void addWaterExplosion(float pWorldX, float pWorldY) {
		explosions.add(new Explosion(pWorldX, pWorldY, "water"));
	}
}
