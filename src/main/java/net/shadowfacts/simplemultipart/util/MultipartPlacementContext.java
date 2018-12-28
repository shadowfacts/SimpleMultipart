package net.shadowfacts.simplemultipart.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.shadowfacts.simplemultipart.container.MultipartContainer;

/**
 * @author shadowfacts
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

	public MultipartContainer getContainer() {
		return container;
	}

}
