package net.shadowfacts.simplemultipart.multipart;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.AbstractPropertyContainer;
import net.minecraft.state.property.Property;
import net.minecraft.util.Hand;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.loot.context.LootContext;

import java.util.List;

/**
 * @author shadowfacts
 */
public class MultipartState extends AbstractPropertyContainer<Multipart, MultipartState> {

	public MultipartState(Multipart part, ImmutableMap<Property<?>, Comparable<?>> properties) {
		super(part, properties);
	}

	public Multipart getMultipart() {
		return owner;
	}

	public MultipartState getStateForRendering(MultipartView view) {
		//noinspection deprecation
		return owner.getStateForRendering(this, view);
	}

	public VoxelShape getBoundingShape(/*@Nullable*/ MultipartView view) {
		//noinspection deprecation
		return owner.getBoundingShape(this, view);
	}

	public List<ItemStack> getDroppedStacks(MultipartView view, LootContext.Builder builder) {
		//noinspection deprecated
		return owner.getDroppedStacks(this, view, builder);
	}

	public boolean activate(MultipartView view, PlayerEntity player, Hand hand) {
		//noinspection deprecated
		return owner.activate(this, view, player, hand);
	}

}
