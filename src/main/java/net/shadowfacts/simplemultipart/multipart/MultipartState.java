package net.shadowfacts.simplemultipart.multipart;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.AbstractPropertyContainer;
import net.minecraft.state.property.Property;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.loot.context.LootContext;

import java.util.List;

/**
 * A container for a {@link Multipart} and its associated properties/values.
 *
 * Analogous to {@link net.minecraft.block.BlockState}.
 *
 * @author shadowfacts
 * @since 0.1.0
 */
public class MultipartState extends AbstractPropertyContainer<Multipart, MultipartState> {

	public MultipartState(Multipart part, ImmutableMap<Property<?>, Comparable<?>> properties) {
		super(part, properties);
	}

	/**
	 * @return The multipart object for this state.
	 */
	public Multipart getMultipart() {
		return owner;
	}

	/**
	 * @see Multipart#getBoundingShape(MultipartState, MultipartView)
	 */
	public VoxelShape getBoundingShape(/*@Nullable*/ MultipartView view) {
		//noinspection deprecation
		return owner.getBoundingShape(this, view);
	}

	/**
	 * @see Multipart#getDroppedStacks(MultipartView, LootContext.Builder)
	 */
	public List<ItemStack> getDroppedStacks(MultipartView view, LootContext.Builder builder) {
		//noinspection deprecated
		return owner.getDroppedStacks(view, builder);
	}

	/**
	 * @see Multipart#activate(MultipartView, Direction, PlayerEntity, Hand)
	 */
	public boolean activate(MultipartView view, Direction side, PlayerEntity player, Hand hand) {
		//noinspection deprecated
		return owner.activate(view, side, player, hand);
	}

}
