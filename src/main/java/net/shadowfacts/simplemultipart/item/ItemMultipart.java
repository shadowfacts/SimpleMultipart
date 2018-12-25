package net.shadowfacts.simplemultipart.item;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.shadowfacts.simplemultipart.SimpleMultipart;
import net.shadowfacts.simplemultipart.api.MultipartContainer;
import net.shadowfacts.simplemultipart.multipart.Multipart;
import net.shadowfacts.simplemultipart.multipart.MultipartState;
import net.shadowfacts.simplemultipart.util.MultipartPlacementContext;

/**
 * @author shadowfacts
 */
public class ItemMultipart extends Item {

	protected Multipart part;

	public ItemMultipart(Multipart part) {
		super(new Settings());
		this.part = part;
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		return tryPlace(context);
	}

	protected ActionResult tryPlace(ItemUsageContext context) {
		// If a multipart inside an existing container was clicked, try inserting into that
		MultipartContainer hitContainer = getContainer(context);
		if (hitContainer != null && tryPlace(new MultipartPlacementContext(hitContainer, context))) {
			return ActionResult.SUCCESS;
		}

		// Otherwise, get or create a new container and try inserting into that
		ItemUsageContext offsetContext = new ItemUsageContext(context.getPlayer(), context.getItemStack(), context.getPos().offset(context.getFacing()), context.getFacing(), context.getHitX(), context.getHitY(), context.getHitZ());
		MultipartContainer offsetContainer = getOrCreateContainer(offsetContext);
		if (offsetContainer != null && tryPlace(new MultipartPlacementContext(offsetContainer, offsetContext))) {
			return ActionResult.SUCCESS;
		}

		return ActionResult.FAILURE;
	}

	protected MultipartContainer getContainer(ItemUsageContext context) {
		BlockState state = context.getWorld().getBlockState(context.getPos());
		if (state.getBlock() == SimpleMultipart.containerBlock) {
			return (MultipartContainer)context.getWorld().getBlockEntity(context.getPos());
		} else {
			return null;
		}
	}

	protected MultipartContainer getOrCreateContainer(ItemUsageContext context) {
		MultipartContainer container = getContainer(context);
		if (container == null) {
			BlockState existing = context.getWorld().getBlockState(context.getPos());
			if (existing.isAir()) { // TODO: should check is replaceable (might not be mapped?)
				context.getWorld().setBlockState(context.getPos(), SimpleMultipart.containerBlock.getDefaultState());
				container = (MultipartContainer)context.getWorld().getBlockEntity(context.getPos());
			}
		}
		return container;
	}

	protected boolean tryPlace(MultipartPlacementContext context) {
		MultipartState placementState = part.getPlacementState(context);

		if (context.getContainer().canInsert(placementState)) {
			context.getContainer().insert(placementState);
			context.getItemStack().addAmount(-1);
			return true;
		}
		return false;
	}

}
