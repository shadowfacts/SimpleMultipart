package net.shadowfacts.simplemultipart.multipart.entity;

import net.minecraft.nbt.CompoundTag;
import net.shadowfacts.simplemultipart.container.MultipartContainerBlockEntity;

/**
 * @author shadowfacts
 */
public abstract class MultipartEntity {

	public MultipartContainerBlockEntity container;

	public MultipartEntity(MultipartContainerBlockEntity container) {
		this.container = container;
	}

	protected void scheduleSave() {
		container.markDirty(); // see yarn #360
	}

	public CompoundTag toTag(CompoundTag tag) {
		return tag;
	}

	public void fromTag(CompoundTag tag) {
	}

}
