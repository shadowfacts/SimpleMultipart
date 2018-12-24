package net.shadowfacts.simplemultipart.test;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateFactory;
import net.minecraft.util.Hand;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.shadowfacts.simplemultipart.container.MultipartContainerBlockEntity;
import net.shadowfacts.simplemultipart.multipart.Multipart;
import net.shadowfacts.simplemultipart.multipart.MultipartSlot;
import net.shadowfacts.simplemultipart.multipart.MultipartState;

/**
 * @author shadowfacts
 */
public class TestMultipart extends Multipart {

	public TestMultipart() {
		setDefaultState(getDefaultState().with(SLOT, MultipartSlot.BOTTOM));
	}

	@Override
	protected void appendProperties(StateFactory.Builder<Multipart, MultipartState> builder) {
		super.appendProperties(builder);
		builder.with(SLOT);
	}

	@Override
	public boolean isValidSlot(MultipartSlot slot) {
		return slot != MultipartSlot.CENTER;
	}

	@Override
	public MultipartState getPlacementState(MultipartSlot slot, MultipartContainerBlockEntity container) {
		return getDefaultState().with(SLOT, slot);
	}

	@Override
	@Deprecated
	public VoxelShape getBoundingShape(MultipartState state, MultipartSlot slot, MultipartContainerBlockEntity container) {
		switch (slot) {
			case TOP:
				return VoxelShapes.cube(0, 15/16f, 0, 1, 1, 1);
			case BOTTOM:
				return VoxelShapes.cube(0, 0, 0, 1, 1/16f, 1);
			case NORTH:
				return VoxelShapes.cube(0, 0, 0, 1, 1, 1/16f);
			case SOUTH:
				return VoxelShapes.cube(0, 0, 15/16f, 1, 1, 1);
			case WEST:
				return VoxelShapes.cube(0, 0, 0, 1/16f, 1, 1);
			case EAST:
				return VoxelShapes.cube(15/16f, 0, 0, 1, 1, 1);
		}
		return VoxelShapes.empty();
	}

	@Override
	@Deprecated
	public boolean activate(MultipartState state, MultipartSlot slot, MultipartContainerBlockEntity container, PlayerEntity player, Hand hand) {
		System.out.println("part activated: " + slot);
		return true;
	}
}
