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
		setDefaultState(getDefaultState().with(HALF, BlockHalf.BOTTOM));
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

		double absoluteHitY = context.method_17698().y; // method_17698 returns an absolutely position vector (i.e. in the world's coordinate space)
		double relativeHitY = absoluteHitY - Math.floor(absoluteHitY); // this converts it to the block's coordinate space (e.g. 4.5 - Math.floor(4.5) = 0.5)
		if (hitSide == Direction.DOWN) {
			half = relativeHitY >= 0.5f ? BlockHalf.BOTTOM : BlockHalf.TOP;
		} else if (hitSide == Direction.UP) {
			half = 0.5f <= relativeHitY && relativeHitY < 1 ? BlockHalf.TOP : BlockHalf.BOTTOM;
		} else {
			half = relativeHitY >= 0.5f ? BlockHalf.TOP : BlockHalf.BOTTOM;
		}

		return getDefaultState().with(HALF, half);
	}

	@Override
	@Deprecated
	public VoxelShape getBoundingShape(MultipartState state, MultipartView view) {
		return state.get(HALF) == BlockHalf.TOP ? UPPER_BOX : LOWER_BOX;
	}
}
