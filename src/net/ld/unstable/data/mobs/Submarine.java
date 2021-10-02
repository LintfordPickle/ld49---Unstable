package net.ld.unstable.data.mobs;

import net.lintford.library.core.geometry.spritegraph.AnimatedSpriteGraphListener;
import net.lintford.library.core.geometry.spritegraph.instance.SpriteGraphInstance;
import net.lintford.library.core.graphics.sprites.SpriteDefinition;

public class Submarine extends ShmupEntity implements AnimatedSpriteGraphListener {

	private transient SpriteGraphInstance mSpriteGraphInstance;

	public SpriteGraphInstance spriteGraphInstance() {
		return mSpriteGraphInstance;
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
