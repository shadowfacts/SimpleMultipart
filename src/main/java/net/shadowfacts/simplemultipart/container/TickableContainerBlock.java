package net.shadowfacts.simplemultipart.container;

import net.minecraft.world.BlockView;

/**
 * @author shadowfacts
 */
public class TickableContainerBlock extends AbstractContainerBlock {

	@Override
	public AbstractContainerBlockEntity createBlockEntity(BlockView world) {
		return new TickableContainerBlockEntity();
	}

}
