package net.shadowfacts.simplemultipart.client.util;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ExtendedBlockView;

/**
 * Temporary until fabric-api #45 is merged
 *
 * @author shadowfacts
 */
public interface RenderStateProvider {

	BlockState getStateForRendering(BlockState state, BlockPos pos, ExtendedBlockView world);

}
