package net.shadowfacts.simplemultipart.container;

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
	 * Retrieves all the multiparts held by this container.
	 *
	 * @see MultipartView
	 * @return All multiparts held by this container.
	 */
	Set<MultipartView> getParts();

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
	boolean breakPart(MultipartView view);

	/**
	 * Indicates that something about a multipart in this container has changed and it should be saved to disk.
	 * Does not trigger a network update.
	 */
	void schedulePartSave();

}
