package net.shadowfacts.simplemultipart.container;

import com.google.common.collect.ImmutableSet;
import net.fabricmc.fabric.api.util.NbtType;
import net.fabricmc.fabric.block.entity.ClientSerializable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.shape.VoxelShape;
import net.shadowfacts.simplemultipart.SimpleMultipart;
import net.shadowfacts.simplemultipart.multipart.MultipartState;
import net.shadowfacts.simplemultipart.util.MultipartHelper;
import net.shadowfacts.simplemultipart.util.ShapeUtils;

import java.util.*;

/**
 * @author shadowfacts
 */
public class MultipartContainerBlockEntity extends BlockEntity implements ClientSerializable {

	private Set<MultipartState> parts = new HashSet<>();

	public MultipartContainerBlockEntity() {
		super(SimpleMultipart.containerBlockEntity);
	}

	public ImmutableSet<MultipartState> getParts() {
		return ImmutableSet.copyOf(parts);
	}

	public boolean canInsert(MultipartState partState) {
		VoxelShape newShape = partState.getBoundingShape(null);
		for (MultipartState existing : parts) {
			VoxelShape existingShape = existing.getBoundingShape(this);
			if (ShapeUtils.intersect(newShape, existingShape)) {
				return false;
			}
		}

		return true;
	}

	public void insert(MultipartState partState) {
		parts.add(partState);
		markDirty();
		world.scheduleBlockRender(pos);
	}

	public void remove(MultipartState partState) {
		parts.remove(partState);

		if (parts.isEmpty()) {
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
		}
	}

	public boolean breakPart(MultipartState partState) {
		if (world instanceof ServerWorld) {
			List<ItemStack> drops = MultipartHelper.getDroppedStacks(partState, (ServerWorld)world, pos);
			drops.forEach(stack -> Block.dropStack(world, pos, stack));
			// TODO: don't drop if player is creative
		}

		remove(partState);

		world.markDirty(pos, this);
		world.scheduleBlockRender(pos);
		BlockState blockState = world.getBlockState(pos);
		world.updateListeners(pos, blockState, blockState, 3);

		return true;
	}

	private ListTag partsToTag() {
		ListTag list = new ListTag();
		parts.forEach(state -> {
			if (state != null) {
				CompoundTag partStateTag = MultipartHelper.serializeMultipartState(state);
				list.add(partStateTag);
			}
		});
		return list;
	}

	private void partsFromTag(ListTag list) {
		parts.clear();
		for (Tag tag : list) {
			MultipartState state = MultipartHelper.deserializeMultipartState((CompoundTag)tag);
			parts.add(state);
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.put("parts", partsToTag());
		return super.toTag(tag);
	}

	@Override
	public void fromTag(CompoundTag tag) {
		super.fromTag(tag);
		ListTag list = tag.getList("parts", NbtType.COMPOUND);
		partsFromTag(list);
	}

	@Override
	public CompoundTag toClientTag(CompoundTag tag) {
		tag.put("parts", partsToTag());
		return tag;
	}

	@Override
	public void fromClientTag(CompoundTag tag) {
		ListTag list = tag.getList("parts", NbtType.COMPOUND);
		partsFromTag(list);
		world.scheduleBlockRender(pos);
	}
}
