package net.shadowfacts.simplemultipart.test;

import net.minecraft.block.enums.BlockHalf;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.shadowfacts.simplemultipart.multipart.Multipart;
import net.shadowfacts.simplemultipart.multipart.MultipartState;
import net.shadowfacts.simplemultipart.util.MultipartPlacementContext;
import net.shadowfacts.simplemultipart.multipart.MultipartView;

/**
 * @author shadowfacts
 */
public class SlabMultipart extends Multipart {

	public static final EnumProperty<BlockHalf> HALF = EnumProperty.create("half", BlockHalf.class);

	private static final VoxelShape LOWER_BOX = VoxelShapes.cube(0, 0, 0, 1, 0.5f, 1);
	private static final VoxelShape UPPER_BOX = VoxelShapes.cube(0, 0.5f, 0, 1, 1, 1);

	public SlabMultipart() {
		setDefaultState(getDefaultState().with(HALF, BlockHalf.LOWER));
	}

	@Override
	protected void appendProperties(StateFactory.Builder<Multipart, MultipartState> builder) {
		super.appendProperties(builder);
		builder.with(HALF);
	}

	@Override
	public MultipartState getPlacementState(MultipartPlacementContext context) {
		Direction hitSide = context.getFacing();
		BlockHalf half;

		if (hitSide == Direction.DOWN) {
			half = context.getHitY() >= 0.5f ? BlockHalf.LOWER : BlockHalf.UPPER;
		} else if (hitSide == Direction.UP) {
			half = 0.5f <= context.getHitY() && context.getHitY() < 1 ? BlockHalf.UPPER : BlockHalf.LOWER;
		} else {
			half = context.getHitY() >= 0.5f ? BlockHalf.UPPER : BlockHalf.LOWER;
		}

//		if (hitSide == Direction.DOWN || (0.5f < context.getHitY() && context.getHitY() < 1)) {
//			half = BlockHalf.UPPER;
//		} else {
//			half = BlockHalf.LOWER;
//		}

//		switch (hitSide) {
//			case UP:
//				half = BlockHalf.LOWER;
//				break;
//			case DOWN:
//				half = BlockHalf.UPPER;
//				break;
//			default:
//				half = context.getHitY() > 0.5f ? BlockHalf.UPPER : BlockHalf.LOWER;
//		}
		return getDefaultState().with(HALF, half);
	}

	@Override
	public VoxelShape getBoundingShape(MultipartState state, MultipartView view) {
		return state.get(HALF) == BlockHalf.UPPER ? UPPER_BOX : LOWER_BOX;
	}
}
