package net.shadowfacts.simplemultipart.client;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ExtendedBlockView;

/**
 * @author shadowfacts
 */
public interface RenderStateProvider {

	BlockState getStateForRendering(BlockState state, BlockPos pos, ExtendedBlockView world);

}
