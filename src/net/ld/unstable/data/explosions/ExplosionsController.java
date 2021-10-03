package net.ld.unstable.data.explosions;

import java.util.ArrayList;
import java.util.List;

import net.lintford.library.controllers.BaseController;
import net.lintford.library.controllers.core.ControllerManager;
import net.lintford.library.core.LintfordCore;

public class ExplosionsController extends BaseController {

	public static final String CONTROLLER_NAME = "Explosion Controller";

	public class Explosion {
		public Explosion(float pWorldX, float pWorldY) {
			worldX = pWorldX;
			worldY = pWorldY;
		}

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
		// TODO Auto-generated method stub

	}

	@Override
	public void unload() {
		// TODO Auto-generated method stub

	}

	public void addMinorExplosion(float pWorldX, float pWorldY) {
		explosions.add(new Explosion(pWorldX, pWorldY));
	}

	public void addMajorExplosion(float pWorldX, float pWorldY) {
		// TODO
		explosions.add(new Explosion(pWorldX, pWorldY));
	}
}
