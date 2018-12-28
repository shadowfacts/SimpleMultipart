package net.shadowfacts.simplemultipart.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.shadowfacts.simplemultipart.container.MultipartContainer;

/**
 * Contains information about the context in which a multipart is being placed into the world.
 *
 * Analogous to {@link net.minecraft.item.ItemPlacementContext}.
 *
 * @author shadowfacts
 * @since 0.1.0
 */
public class MultipartPlacementContext extends ItemUsageContext {

	private final MultipartContainer container;

	public MultipartPlacementContext(MultipartContainer container, PlayerEntity player, ItemStack stack, BlockPos pos, Direction side, float hitX, float hitY, float hitZ) {
		super(player, stack, pos, side, hitX, hitY, hitZ);
		this.container = container;
	}

	public MultipartPlacementContext(MultipartContainer container, ItemUsageContext context) {
		this(container, context.getPlayer(), context.getItemStack(), context.getPos(), context.getFacing(), context.getHitX(), context.getHitY(), context.getHitZ());
	}

	/**
	 * @return The container that this multipart will be inserted into.
	 */
	public MultipartContainer getContainer() {
		return container;
	}

}
