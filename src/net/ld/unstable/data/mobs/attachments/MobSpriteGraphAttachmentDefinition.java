package net.ld.unstable.data.mobs.attachments;

import net.lintford.library.core.geometry.spritegraph.attachment.ISpriteGraphNodeAttachment;
import net.lintford.library.core.graphics.sprites.spritesheet.SpriteSheetDefinition;

public class MobSpriteGraphAttachmentDefinition implements ISpriteGraphNodeAttachment {

	// ---------------------------------------------
	// Variables
	// ---------------------------------------------

	protected String spriteSheetName;
	protected boolean isRemovable;
	protected int relativeZDepth;
	protected int attachmentCategory;
	private transient SpriteSheetDefinition mSpriteGraphSpriteSheetDefinition;

	protected int mColorTint = 0xFFFFFFFF;

	// ---------------------------------------------
	// Properties
	// ---------------------------------------------

	@Override
	public int getColorTint() {
		return mColorTint;
	}

	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------

	public MobSpriteGraphAttachmentDefinition() {

	}

	// ---------------------------------------------
	// Methods
	// ---------------------------------------------

	@Override
	public SpriteSheetDefinition spriteSheetDefinition() {
		return mSpriteGraphSpriteSheetDefinition;
	}

	@Override
	public void spriteSheetDefinition(SpriteSheetDefinition pSpriteSheetDefinition) {
		mSpriteGraphSpriteSheetDefinition = pSpriteSheetDefinition;
	}

	@Override
	public String spriteGraphSpriteSheetName() {
		return spriteSheetName;
	}

	@Override
	public boolean isRemovable() {
		return isRemovable;
	}

	@Override
	public int relativeZDepth() {
		return relativeZDepth;
	}

	@Override
	public int attachmentCategory() {
		return attachmentCategory;
	}

	@Override
	public String defaultSpriteName() {
		return null;
	}

}