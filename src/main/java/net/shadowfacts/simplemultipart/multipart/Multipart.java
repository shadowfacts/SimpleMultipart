package net.shadowfacts.simplemultipart.multipart;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateFactory;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.loot.LootSupplier;
import net.minecraft.world.loot.LootTables;
import net.minecraft.world.loot.context.LootContext;
import net.shadowfacts.simplemultipart.SimpleMultipart;
import net.shadowfacts.simplemultipart.util.MultipartPlacementContext;
import net.shadowfacts.simplemultipart.api.MultipartView;

import java.util.List;

/**
 * @author shadowfacts
 */
public abstract class Multipart {

	private StateFactory<Multipart, MultipartState> stateFactory;
	private MultipartState defaultState;

	private Identifier dropTableId;

	public Multipart() {
		StateFactory.Builder<Multipart, MultipartState> builder = new StateFactory.Builder<>(this);
		appendProperties(builder);
		stateFactory = builder.build(MultipartState::new);
		defaultState = stateFactory.getDefaultState();
	}

	protected void appendProperties(StateFactory.Builder<Multipart, MultipartState> builder) {}

	public MultipartState getDefaultState() {
		return defaultState;
	}

	public void setDefaultState(MultipartState defaultState) {
		this.defaultState = defaultState;
	}

	public StateFactory<Multipart, MultipartState> getStateFactory() {
		return stateFactory;
	}

	public MultipartState getPlacementState(MultipartPlacementContext context) {
		return getDefaultState();
	}

	/**
	 * Can be overridden, should only be called via {@link MultipartState#getStateForRendering}
	 */
	@Deprecated
	public MultipartState getStateForRendering(MultipartState state, MultipartView view) {
		return state;
	}

	/**
	 * Can be overridden, should only be called via {@link MultipartState#getBoundingShape}
	 */
	@Deprecated
	public abstract VoxelShape getBoundingShape(/*@Nullable*/ MultipartState state, MultipartView view);

	public Identifier getDropTableId() {
		if (dropTableId == null) {
			Identifier id = SimpleMultipart.MULTIPART.getId(this);
			dropTableId = new Identifier(id.getNamespace(), "multiparts/" + id.getPath());
		}
		return dropTableId;
	}

	/**
	 * Can be overridden, should only be called via {@link MultipartState#getDroppedStacks)}
	 */
	@Deprecated
	public List<ItemStack> getDroppedStacks(MultipartState state, MultipartView view, LootContext.Builder builder) {
		Identifier dropTableId = getDropTableId();
		if (dropTableId == LootTables.EMPTY) {
			return ImmutableList.of();
		} else {
			LootContext context = builder.put(SimpleMultipart.MULTIPART_STATE_PARAMETER, state).build(SimpleMultipart.MULTIPART_LOOT_CONTEXT);
			ServerWorld world = context.getWorld();
			LootSupplier supplier = world.getServer().getLootManager().getSupplier(dropTableId);
			return supplier.getDrops(context);
		}
	}

	/**
	 * Can be overridden, should only be called via {@link MultipartState#activate}
	 */
	@Deprecated
	public boolean activate(MultipartState state, MultipartView view, PlayerEntity player, Hand hand) {
		return false;
	}

}
