package net.ld.unstable.controllers;

import net.lintford.library.controllers.BaseController;
import net.lintford.library.controllers.core.ControllerManager;
import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.audio.AudioFireAndForgetManager;
import net.lintford.library.core.audio.AudioManager;
import net.lintford.library.core.maths.RandomNumbers;

public class SoundFxController extends BaseController {

	// ---------------------------------------------
	// Constants
	// ---------------------------------------------

	public static final String CONTROLLER_NAME = "Sound Fx Controller";

	public static final String AUDIO_NAME_EMPTY = "";
	public static final String AUDIO_NAME_EXPLOSION_00 = "AUDIO_EXPLOSION_00";
	public static final String AUDIO_NAME_EXPLOSION_01 = "AUDIO_EXPLOSION_01";
	public static final String AUDIO_NAME_EXPLOSION_02 = "AUDIO_EXPLOSION_02";

	public static final String AUDIO_NAME_SURFACE_00 = "AUDIO_SURFACE_00";
	public static final String AUDIO_NAME_SURFACE_01 = "AUDIO_SURFACE_01";

	public static final String AUDIO_NAME_SHOOT_00 = "AUDIO_SHOOT_00";
	public static final String AUDIO_NAME_SHOOT_01 = "AUDIO_SHOOT_01";

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	private AudioFireAndForgetManager mAudioFireAndForgetManager;

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	@Override
	public boolean isInitialized() {
		return mAudioFireAndForgetManager != null;
	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public SoundFxController(ControllerManager pControllerManager, AudioManager pAudioManager, int pEntityGroupID) {
		super(pControllerManager, CONTROLLER_NAME, pEntityGroupID);

		mAudioFireAndForgetManager = new AudioFireAndForgetManager(pAudioManager);

	}

	// ---------------------------------------------
	// Core-Methods
	// ---------------------------------------------

	@Override
	public void initialize(LintfordCore pCore) {
		mAudioFireAndForgetManager.acquireAudioSources(6);

	}

	@Override
	public void unload() {
		// TODO Auto-generated method stub

	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	public void playSound(String pSoundFxName) {
		mAudioFireAndForgetManager.play(pSoundFxName);

	}

	// ---------------------------------------------

	public void playExplosionSound() {
		switch (RandomNumbers.random(0, 3)) {
		case 0:
			playSound(SoundFxController.AUDIO_NAME_EXPLOSION_00);
			return;
		case 1:
			playSound(SoundFxController.AUDIO_NAME_EXPLOSION_01);
			return;
		case 2:
			playSound(SoundFxController.AUDIO_NAME_EXPLOSION_02);
			return;
		}
	}

	public void playShootingSound() {
		switch (RandomNumbers.random(0, 2)) {
		case 0:
			playSound(SoundFxController.AUDIO_NAME_SHOOT_00);
			return;
		case 1:
			playSound(SoundFxController.AUDIO_NAME_SHOOT_01);
			return;
		}
	}

	public void playSurfacingSound() {
		switch (RandomNumbers.random(0, 2)) {
		case 0:
			playSound(SoundFxController.AUDIO_NAME_SURFACE_00);
			return;
		case 1:
			playSound(SoundFxController.AUDIO_NAME_SURFACE_01);
			return;
		}
	}

}
