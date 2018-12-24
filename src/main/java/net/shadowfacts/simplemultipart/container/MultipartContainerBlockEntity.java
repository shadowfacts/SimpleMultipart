package net.shadowfacts.simplemultipart.container;

import com.google.common.collect.ImmutableMap;
import net.fabricmc.fabric.block.entity.ClientSerializable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.shadowfacts.simplemultipart.SimpleMultipart;
import net.shadowfacts.simplemultipart.multipart.MultipartSlot;
import net.shadowfacts.simplemultipart.multipart.MultipartState;
import net.shadowfacts.simplemultipart.util.MultipartHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shadowfacts
 */
public class MultipartContainerBlockEntity extends BlockEntity implements ClientSerializable {

	private Map<MultipartSlot, MultipartState> parts = new HashMap<>();

	public MultipartContainerBlockEntity() {
		super(SimpleMultipart.containerBlockEntity);
	}

	public ImmutableMap<MultipartSlot, MultipartState> getParts() {
		return ImmutableMap.copyOf(parts);
	}

	public boolean hasPartInSlot(MultipartSlot slot) {
		return parts.containsKey(slot);
	}

	public boolean canInsert(MultipartState partState, MultipartSlot slot) {
		if (hasPartInSlot(slot)) {
			return false;
		}

		// TODO: check bounding box intersections

		return true;
	}

	public void insert(MultipartState partState, MultipartSlot slot) {
		parts.put(slot, partState);
		markDirty();
		world.scheduleBlockRender(pos);
	}

	public MultipartState get(MultipartSlot slot) {
		return parts.get(slot);
	}

	public void remove(MultipartSlot slot) {
		parts.remove(slot);

		if (parts.isEmpty()) {
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
		}
	}

	public boolean breakPart(MultipartSlot slot) {
		MultipartState state = get(slot);
		if (state == null) {
			return false;
		}

		if (world instanceof ServerWorld) {
			List<ItemStack> drops = MultipartHelper.getDroppedStacks(state, (ServerWorld)world, pos);
			drops.forEach(stack -> Block.dropStack(world, pos, stack));
			// TODO: don't drop if player is creative
		}

		remove(slot);

		world.markDirty(pos, this);
		world.scheduleBlockRender(pos);
		BlockState blockState = world.getBlockState(pos);
		world.updateListeners(pos, blockState, blockState, 3);

		return true;
	}

	private CompoundTag partsToTag(CompoundTag tag) {
		parts.forEach((slot, state) -> {
			if (state != null) {
				CompoundTag partStateTag = MultipartHelper.serializeMultipartState(state);
				tag.put(slot.name(), partStateTag);
			}
		});
		return tag;
	}

	private void partsFromTag(CompoundTag tag) {
		parts.clear();
		for (MultipartSlot slot : MultipartSlot.values()) {
			if (!(tag.containsKey(slot.name(), 10))) {
				continue;
			}
			CompoundTag partStateTag = tag.getCompound(slot.name());
			MultipartState state = MultipartHelper.deserializeMultipartState(partStateTag);
			parts.put(slot, state);
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		partsToTag(tag);
		return super.toTag(tag);
	}

	@Override
	public void fromTag(CompoundTag tag) {
		super.fromTag(tag);
		partsFromTag(tag);
	}

	@Override
	public CompoundTag toClientTag(CompoundTag tag) {
		return partsToTag(tag);
	}

	@Override
	public void fromClientTag(CompoundTag tag) {
		partsFromTag(tag);
		world.scheduleBlockRender(pos);
	}
}
