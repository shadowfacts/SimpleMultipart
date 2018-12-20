package net.shadowfacts.simplemultipart.test;

import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.shadowfacts.simplemultipart.container.MultipartContainerBlockEntity;
import net.shadowfacts.simplemultipart.multipart.Multipart;
import net.shadowfacts.simplemultipart.multipart.MultipartSlot;
import net.shadowfacts.simplemultipart.multipart.MultipartState;

/**
 * @author shadowfacts
 */
public class RedMultipart extends Multipart {

	@Override
	public boolean isValidSlot(MultipartSlot slot) {
		return slot == MultipartSlot.BOTTOM;
	}

	@Override
	@Deprecated
	public VoxelShape getBoundingShape(MultipartState state, MultipartSlot slot, MultipartContainerBlockEntity container) {
		return VoxelShapes.cube(0, 0, 0, 1, 1/16f, 1);
	}
}
