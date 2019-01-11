package net.shadowfacts.simplemultipart.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.shadowfacts.simplemultipart.multipart.Multipart;
import net.shadowfacts.simplemultipart.multipart.MultipartView;
import net.shadowfacts.simplemultipart.multipart.MultipartState;

import java.util.Set;

/**
 * An interface for an object that contains multiparts.
 * Usually a {@link net.minecraft.block.entity.BlockEntity}; default implementation is {@link AbstractContainerBlockEntity}.
 *
 * @author shadowfacts
 * @since 0.1.0
 */
public interface MultipartContainer {

	/**
	 * @return The world that this container is in.
	 */
	World getContainerWorld();

	/**
	 * @return The position of this container in the world.
	 */
	BlockPos getContainerPos();

	/**
	 * Retrieves all the multiparts held by this container.
	 *
	 * @see MultipartView
	 * @return All multiparts held by this container.
	 */
	Set<MultipartView> getParts();

	/**
	 * @return If this container has any parts in it.
	 */
	boolean hasParts();

	/**
	 * Gets all the multiparts in this container of the given type.
	 *
	 * @param type The multipart instance representing the multipart type.
	 * @return The set of all the multiparts in this container that are of that type.
	 */
	Set<MultipartView> getParts(Multipart type);

	/**
	 * Gets the part on the given side.
	 *
	 * Will return the part with the greatest/least min/max coordinate based on the direction's axis.
	 * For example, getting the part on the {@code NORTH} side will return the part with the smallest minimum Z coordinate.
	 * If multiple parts have the same min/max coordinate, which one will be returned is undefined.
	 *
	 * @param side The side to determine the part for.
	 * @return The part on the given side.
	 */
	MultipartView getPart(Direction side);

	/**
	 * Containers store a cache of which part is on which side, calculated using the bounding box.
	 *
	 * If anything changes in your part that changes its bounding shape, this method should be called.
	 * The container {@code insert}, {@code breakPart}, and {@code remove} methods automatically call this.
	 */
	void invalidateSidePartCache();

	/**
	 * Determines whether the given multipart state can be inserted into this container.
	 * Checks that the bounding box of the new part does not intersect with any existing ones.
	 *
	 * @param state The new multipart state.
	 * @return If the part can be inserted.
	 */
	boolean canInsert(MultipartState state);

	/**
	 * Performs the insertion of the given multipart into this container.
	 *
	 * @param state The new multipart state.
	 */
	void insert(MultipartState state);

	/**
	 * Removes the given multipart from this container.
	 *
	 * @see MultipartView
	 * @param view The part to remove
	 */
	void remove(MultipartView view);

	/**
	 * Breaks the given multipart. Removes it from this container and drops the part as an item.
	 *
	 * @param view The part to break.
	 * @return If the part has been successfully broken.
	 */
	boolean breakPart(MultipartView view, PlayerEntity player);

	/**
	 * Indicates that something about a multipart in this container has changed and it should be saved to disk.
	 * Does not trigger a network update.
	 */
	void schedulePartSave();

}
