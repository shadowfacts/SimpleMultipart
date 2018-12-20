package net.shadowfacts.simplemultipart.multipart;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.AbstractPropertyContainer;
import net.minecraft.state.property.Property;
import net.minecraft.util.Hand;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.loot.context.LootContext;
import net.shadowfacts.simplemultipart.container.MultipartContainerBlockEntity;

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

	public MultipartState getStateForRendering(MultipartSlot slot, MultipartContainerBlockEntity container) {
		//noinspection deprecation
		return owner.getStateForRendering(this, slot, container);
	}

	public VoxelShape getBoundingShape(MultipartSlot slot, MultipartContainerBlockEntity container) {
		//noinspection deprecation
		return owner.getBoundingShape(this, slot, container);
	}

	public List<ItemStack> getDroppedStacks(LootContext.Builder builder) {
		//noinspection deprecated
		return owner.getDroppedStacks(this, builder);
	}

	public boolean activate(MultipartSlot slot, MultipartContainerBlockEntity container, PlayerEntity player, Hand hand) {
		//noinspection deprecated
		return owner.activate(this, slot, container, player, hand);
	}

}
