package net.shadowfacts.simplemultipart.container;

import net.fabricmc.fabric.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.ExtendedBlockView;
import net.minecraft.world.World;
import net.shadowfacts.simplemultipart.client.util.RenderStateProvider;
import net.shadowfacts.simplemultipart.multipart.MultipartSlot;
import net.shadowfacts.simplemultipart.multipart.MultipartState;
import net.shadowfacts.simplemultipart.util.MultipartHelper;
import net.shadowfacts.simplemultipart.util.MultipartHitResult;

import java.util.Map;

/**
 * @author shadowfacts
 */
public class MultipartContainerBlock extends Block implements BlockEntityProvider, RenderStateProvider {

	public MultipartContainerBlock() {
		super(FabricBlockSettings.of(Material.STONE).build());
	}

	@Override
	public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, Direction side, float hitX, float hitY, float hitZ) {
//		if (player.isSneaking()) {
//			MultipartContainerBlockEntity container = (MultipartContainerBlockEntity)world.getBlockEntity(pos);
//			System.out.println(container.getParts());
//			return true;
//		} else {
//			return false;
//		}

		MultipartContainerBlockEntity container = (MultipartContainerBlockEntity)world.getBlockEntity(pos);
		if (container == null) {
			return false;
		}

		MultipartHitResult hit = MultipartHelper.rayTrace(container, world, pos, player);
		if (hit == null) {
			return false;
		}

		MultipartState partState = container.get(hit.partSlot);
		return partState.activate(hit.partSlot, container, player, hand);
	}

	@Override
	public BlockState getStateForRendering(BlockState state, BlockPos pos, ExtendedBlockView world) {
		MultipartContainerBlockEntity container = (MultipartContainerBlockEntity)world.getBlockEntity(pos);
		if (container == null) {
			return state;
		}

		Map<MultipartSlot, MultipartState> parts = container.getParts();
		return new MultipartContainerBlockState(state, parts);
	}

	@Override
	public VoxelShape getBoundingShape(BlockState state, BlockView world, BlockPos pos) {
		MultipartContainerBlockEntity container = (MultipartContainerBlockEntity)world.getBlockEntity(pos);
		if (container == null) {
			return VoxelShapes.empty();
		}

		VoxelShape shape = null;
		for (Map.Entry<MultipartSlot, MultipartState> e : container.getParts().entrySet()) {
			VoxelShape partShape = e.getValue().getBoundingShape(e.getKey(), container);
			shape = shape == null ? partShape : VoxelShapes.union(shape, partShape);
		}
		return shape == null ? VoxelShapes.empty() : shape;
	}

	@Override
	public MultipartContainerBlockEntity createBlockEntity(BlockView world) {
		return new MultipartContainerBlockEntity();
	}
}
