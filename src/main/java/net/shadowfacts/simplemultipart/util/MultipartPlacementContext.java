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
	private final boolean isOffset;

	public MultipartPlacementContext(MultipartContainer container, boolean isOffset, PlayerEntity player, ItemStack stack, BlockPos pos, Direction side, float hitX, float hitY, float hitZ) {
		super(player, stack, pos, side, hitX, hitY, hitZ);
		this.container = container;
		this.isOffset = isOffset;
	}

	public MultipartPlacementContext(MultipartContainer container, boolean isOffset, ItemUsageContext context) {
		this(container, isOffset, context.getPlayer(), context.getItemStack(), context.getPos(), context.getFacing(), context.getHitX(), context.getHitY(), context.getHitZ());
	}

	/**
	 * @return The container that this multipart will be inserted into.
	 */
	public MultipartContainer getContainer() {
		return container;
	}

	/**
	 * @return {@code false} if this container is the one clicked, {@code true} if this container is a newly created one offset from the block clicked.
	 * @since 0.1.2
	 */
	public boolean isOffset() {
		return isOffset;
	}
}
