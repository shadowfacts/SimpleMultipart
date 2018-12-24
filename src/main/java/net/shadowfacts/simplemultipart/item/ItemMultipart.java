package net.shadowfacts.simplemultipart.item;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.shadowfacts.simplemultipart.SimpleMultipart;
import net.shadowfacts.simplemultipart.container.MultipartContainerBlockEntity;
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
		return place(new ItemPlacementContext(context));
	}

	protected ActionResult place(ItemPlacementContext context) {
		MultipartContainerBlockEntity container = getOrCreateContainer(context.getWorld(), context.getPos());
		if (container == null) {
			return ActionResult.FAILURE;
		}

		MultipartPlacementContext partContext = new MultipartPlacementContext(container, context);
		MultipartState state = part.getPlacementState(partContext);

		if (!container.canInsert(state)) {
//			container.destroyIfEmpty();
			return ActionResult.FAILURE;
		}

		container.insert(state);

		context.getItemStack().addAmount(-1);

		return ActionResult.SUCCESS;

//		MultipartSlot slot = getSlotForPlacement(container, context);
//		if (slot == null) {
//			return ActionResult.FAILURE;
//		}
//
//		MultipartState partState = part.getPlacementState(slot, container);
//		if (!container.canInsert(partState, slot)) {
//			return ActionResult.FAILURE;
//		}
//
//		container.insert(partState, slot);
//
//		context.getItemStack().addAmount(-1);
//
//		return ActionResult.SUCCESS;
	}

	protected MultipartContainerBlockEntity getOrCreateContainer(World world, BlockPos pos) {
		BlockState current = world.getBlockState(pos);
		if (current.getBlock() == SimpleMultipart.containerBlock) {
			return (MultipartContainerBlockEntity)world.getBlockEntity(pos);
		} else if (current.isAir()) {
			world.setBlockState(pos, SimpleMultipart.containerBlock.getDefaultState());
			return (MultipartContainerBlockEntity)world.getBlockEntity(pos);
		} else {
			return null;
		}
	}

//	protected MultipartSlot getSlotForPlacement(MultipartContainerBlockEntity container, ItemPlacementContext context) {
//		MultipartSlot slot = MultipartSlot.fromClickedSide(context.getFacing());
//		if (part.isValidSlot(slot) && !container.hasPartInSlot(slot)) {
//			return slot;
//		}
//		if (part.isValidSlot(MultipartSlot.CENTER) && !container.hasPartInSlot(MultipartSlot.CENTER)) {
//			return MultipartSlot.CENTER;
//		}
//		return null;
//	}

}
