package net.shadowfacts.simplemultipart.multipart.entity;

import net.minecraft.nbt.CompoundTag;
import net.shadowfacts.simplemultipart.api.MultipartContainer;

/**
 * @author shadowfacts
 */
public abstract class MultipartEntity {

	public MultipartContainer container;

	public MultipartEntity(MultipartContainer container) {
		this.container = container;
	}

	protected void scheduleSave() {
		container.schedulePartSave();
	}

	public CompoundTag toTag(CompoundTag tag) {
		return tag;
	}

	public void fromTag(CompoundTag tag) {
	}

}
