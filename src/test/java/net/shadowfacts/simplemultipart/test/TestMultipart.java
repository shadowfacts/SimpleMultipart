package net.shadowfacts.simplemultipart.test;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
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
public class TestMultipart extends Multipart {

	public TestMultipart() {
		setDefaultState(getDefaultState().with(Properties.FACING, Direction.DOWN));
	}

	@Override
	protected void appendProperties(StateFactory.Builder<Multipart, MultipartState> builder) {
		super.appendProperties(builder);
		builder.with(Properties.FACING);
	}

	@Override
	public MultipartState getPlacementState(MultipartPlacementContext context) {
		Direction hitSide = context.getFacing();
		return getDefaultState().with(Properties.FACING, hitSide.getOpposite());
	}

	@Override
	@Deprecated
	public VoxelShape getBoundingShape(MultipartState state, MultipartView view) {
		Direction side = state.get(Properties.FACING);
		switch (side) {
			case UP:
				return VoxelShapes.cube(0, 15/16f, 0, 1, 1, 1);
			case DOWN:
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
	public boolean activate(MultipartView view, PlayerEntity player, Hand hand) {
		Direction side = view.getState().get(Properties.FACING);
		System.out.println("part activated on " + side);
		return true;
	}

}
