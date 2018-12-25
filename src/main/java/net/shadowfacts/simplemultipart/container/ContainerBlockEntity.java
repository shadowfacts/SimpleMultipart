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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.loot.context.LootContext;
import net.minecraft.world.loot.context.Parameters;
import net.shadowfacts.simplemultipart.SimpleMultipart;
import net.shadowfacts.simplemultipart.api.MultipartContainer;
import net.shadowfacts.simplemultipart.multipart.MultipartState;
import net.shadowfacts.simplemultipart.multipart.entity.MultipartEntity;
import net.shadowfacts.simplemultipart.multipart.entity.MultipartEntityProvider;
import net.shadowfacts.simplemultipart.util.MultipartHelper;
import net.shadowfacts.simplemultipart.api.MultipartView;
import net.shadowfacts.simplemultipart.util.ShapeUtils;

import java.util.*;

/**
 * @author shadowfacts
 */
public class ContainerBlockEntity extends BlockEntity implements MultipartContainer, ClientSerializable {

	private Set<Entry> parts = new HashSet<>();

	public ContainerBlockEntity() {
		super(SimpleMultipart.containerBlockEntity);
	}

	@Override
	public Set<MultipartView> getParts() {
		return ImmutableSet.copyOf(parts);
	}

	@Override
	public boolean canInsert(MultipartState partState) {
		VoxelShape newShape = partState.getBoundingShape(null);
		for (Entry e : parts) {
			VoxelShape existingShape = e.state.getBoundingShape(e);
			if (ShapeUtils.intersect(newShape, existingShape)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public void insert(MultipartState partState) {
		if (!canInsert(partState)) {
			return;
		}

		MultipartEntity entity = null;
		if (partState.getMultipart() instanceof MultipartEntityProvider) {
			entity = ((MultipartEntityProvider)partState.getMultipart()).createMultipartEntity(partState, this);
		}
		parts.add(new Entry(partState, entity));
		markDirty();
		world.scheduleBlockRender(pos);
	}

	@Override
	public void remove(MultipartState partState) {
		parts.removeIf(e -> e.state == partState);

		if (parts.isEmpty()) {
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
		}
	}

	@Override
	public boolean breakPart(MultipartState partState) {
		Optional<Entry> entry = parts.stream().filter(e -> e.state == partState).findFirst();
		if (!entry.isPresent()) {
			return false;
		}

		if (world instanceof ServerWorld) {
			List<ItemStack> drops = getDroppedStacks(entry.get(), (ServerWorld)world, pos);
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

	@Override
	public void schedulePartSave() {
		markDirty(); // see yarn #360
	}

	private List<ItemStack> getDroppedStacks(ContainerBlockEntity.Entry e, ServerWorld world, BlockPos pos) {
		LootContext.Builder builder = new LootContext.Builder(world);
		builder.setRandom(world.random);
		builder.put(SimpleMultipart.MULTIPART_STATE_PARAMETER, e.state);
		builder.put(Parameters.POSITION, pos);
		return e.state.getDroppedStacks(e, builder);
	}

	private ListTag partsToTag() {
		ListTag list = new ListTag();
		for (Entry e : parts) {
			CompoundTag tag = new CompoundTag();
			tag.put("part", MultipartHelper.serializeMultipartState(e.state));
			if (e.entity != null) {
				tag.put("entity", e.entity.toTag(new CompoundTag()));
			}
			list.add(tag);
		}
		return list;
	}

	private void partsFromTag(ListTag list) {
		parts.clear();
		for (Tag tag : list) {
			CompoundTag compound = (CompoundTag)tag;
			MultipartState state = MultipartHelper.deserializeMultipartState(compound.getCompound("part"));
			MultipartEntity entity = null;
			if (state.getMultipart() instanceof MultipartEntityProvider && compound.containsKey("entity", NbtType.COMPOUND)) {
				entity = ((MultipartEntityProvider)state.getMultipart()).createMultipartEntity(state, this);
				entity.fromTag(compound.getCompound("entity"));
			}
			parts.add(new Entry(state, entity));
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

	public class Entry implements MultipartView {
		public final MultipartState state;
		public final MultipartEntity entity;

		private Entry(MultipartState state, MultipartEntity entity) {
			this.state = state;
			this.entity = entity;
		}

		@Override
		public ContainerBlockEntity getContainer() {
			return ContainerBlockEntity.this; // TODO: is this bad?
		}

		@Override
		public MultipartState getState() {
			return state;
		}

		@Override
		public MultipartEntity getEntity() {
			return entity;
		}
	}

}
