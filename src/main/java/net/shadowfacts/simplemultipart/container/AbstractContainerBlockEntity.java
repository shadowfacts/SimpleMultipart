package net.shadowfacts.simplemultipart.container;

import com.google.common.collect.ImmutableSet;
import net.fabricmc.fabric.api.util.NbtType;
import net.fabricmc.fabric.block.entity.ClientSerializable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import net.minecraft.world.loot.context.LootContext;
import net.minecraft.world.loot.context.Parameters;
import net.shadowfacts.simplemultipart.SimpleMultipart;
import net.shadowfacts.simplemultipart.multipart.Multipart;
import net.shadowfacts.simplemultipart.multipart.MultipartState;
import net.shadowfacts.simplemultipart.multipart.entity.MultipartEntity;
import net.shadowfacts.simplemultipart.multipart.entity.MultipartEntityProvider;
import net.shadowfacts.simplemultipart.util.MultipartHelper;
import net.shadowfacts.simplemultipart.multipart.MultipartView;
import net.shadowfacts.simplemultipart.util.ShapeUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author shadowfacts
 */
public abstract class AbstractContainerBlockEntity extends BlockEntity implements MultipartContainer, ClientSerializable {

	protected Set<Entry> parts = new HashSet<>();
	protected Map<Direction, Entry> sidePartCache = new WeakHashMap<>();

	public AbstractContainerBlockEntity(BlockEntityType<?> type) {
		super(type);
	}

	@Override
	public World getContainerWorld() {
		return world;
	}

	@Override
	public BlockPos getContainerPos() {
		return pos;
	}

	@Override
	public Set<MultipartView> getParts() {
		return ImmutableSet.copyOf(parts);
	}

	@Override
	public boolean hasParts() {
		return !parts.isEmpty();
	}

	@Override
	public Set<MultipartView> getParts(Multipart type) {
		return parts.stream()
				.filter(e -> e.getState().getMultipart() == type)
				.collect(Collectors.toSet());
	}

	@Override
	public MultipartView getPart(Direction side) {
		Entry existing = sidePartCache.get(side);
		if (existing != null) {
			return existing;
		}

		Optional<Entry> e = parts.stream()
				.min((a, b) -> {
					VoxelShape aShape = a.getState().getBoundingShape(a);
					VoxelShape bShape = b.getState().getBoundingShape(b);
					double aCoord = side.getDirection() == Direction.AxisDirection.POSITIVE ? aShape.getMaximum(side.getAxis()) : aShape.getMinimum(side.getAxis());
					double bCoord = side.getDirection() == Direction.AxisDirection.POSITIVE ? bShape.getMaximum(side.getAxis()) : bShape.getMinimum(side.getAxis());
					return Double.compare(bCoord, aCoord);
				});

		if (!e.isPresent()) {
			return null;
		}

		sidePartCache.put(side, e.get());

		return e.get();
	}

	@Override
	public void invalidateSidePartCache() {
		sidePartCache.clear();
	}

	@Override
	public boolean canInsert(MultipartState partState) {
		VoxelShape newShape = partState.getBoundingShape(null);
		for (Entry e : parts) {
			VoxelShape existingShape = e.state.getBoundingShape(e);
			if (ShapeUtils.intersect(newShape, existingShape) && !(partState.canIntersectWith(e.state) && e.state.canIntersectWith(partState))) {
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

		Entry e = new Entry(this, partState, null);
		if (partState.getMultipart() instanceof MultipartEntityProvider) {
			e.entity = ((MultipartEntityProvider)partState.getMultipart()).createMultipartEntity(partState, this);
			e.entity.view = e;
		}
		parts.add(e);

		partState.onPartAdded(e);

		invalidateSidePartCache();
		updateWorld();
	}

	@Override
	public void remove(MultipartView view) {
		if (view.getContainer() != this || !(view instanceof Entry)) {
			return;
		}

		parts.remove(view);
		view.getState().onPartRemoved(view);

		if (parts.isEmpty()) {
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
		} else {
			invalidateSidePartCache();
			updateWorld();
		}
	}

	@Override
	public boolean breakPart(MultipartView view, PlayerEntity player) {
		if (view.getContainer() != this || !(view instanceof Entry)) {
			return false;
		}

		Entry e = (Entry)view;

		if (world instanceof ServerWorld && !player.isCreative()) {
			List<ItemStack> drops = getDroppedStacks(e, (ServerWorld)world, pos);
			drops.forEach(stack -> Block.dropStack(world, pos, stack));
		}

		remove(e);

		updateWorld();

		return true;
	}

	@Override
	public void schedulePartSave() {
		markDirty(); // see yarn #360
	}

	private void updateWorld() {
		boolean hasTickableParts = parts.stream().anyMatch(e -> e.getEntity() != null && e.getEntity() instanceof Tickable);
		boolean currentlyTickable = this instanceof Tickable;
		if (hasTickableParts != currentlyTickable) {
			Block newBlock = hasTickableParts ? SimpleMultipart.tickableContainerBlock : SimpleMultipart.containerBlock;
			world.setBlockState(pos, newBlock.getDefaultState(), 3);
			AbstractContainerBlockEntity newContainer = (AbstractContainerBlockEntity)world.getBlockEntity(pos);
			newContainer.parts = parts.stream()
					.map(e -> new Entry(newContainer, e.state, e.entity))
					.collect(Collectors.toSet());
			newContainer.parts.stream().filter(e -> e.entity != null).forEach(e -> e.entity.view = e);
		}

		world.markDirty(pos, world.getBlockEntity(pos));
		world.scheduleBlockRender(pos);
		BlockState blockState = world.getBlockState(pos);
		world.updateListeners(pos, blockState, blockState, 3);
		world.updateNeighbors(pos, blockState.getBlock());
	}

	private List<ItemStack> getDroppedStacks(Entry e, ServerWorld world, BlockPos pos) {
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

			Entry e = new Entry(this, state, null);
			if (state.getMultipart() instanceof MultipartEntityProvider && compound.containsKey("entity", NbtType.COMPOUND)) {
				e.entity = ((MultipartEntityProvider)state.getMultipart()).createMultipartEntity(state, this);
				e.entity.view = e;
				e.entity.fromTag(compound.getCompound("entity"));
			}
			parts.add(e);
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
		updateWorld();
	}

	public static class Entry implements MultipartView {
		public final AbstractContainerBlockEntity container;
		public MultipartState state;
		public MultipartEntity entity;

		private Entry(AbstractContainerBlockEntity container, MultipartState state, MultipartEntity entity) {
			this.container = container;
			this.state = state;
			this.entity = entity;
		}

		@Override
		public AbstractContainerBlockEntity getContainer() {
			return container;
		}

		@Override
		public MultipartState getState() {
			return state;
		}

		@Override
		public void setState(MultipartState state) {
			this.state = state;
			container.invalidateSidePartCache();
		}

		@Override
		public MultipartEntity getEntity() {
			return entity;
		}

		@Override
		public void setEntity(MultipartEntity entity) {
			this.entity = entity;
			container.invalidateSidePartCache();
		}

		@Override
		public String toString() {
			return "Entry{" + state + "}";
		}
	}

}
