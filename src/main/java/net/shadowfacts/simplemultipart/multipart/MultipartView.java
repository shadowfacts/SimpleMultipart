package net.shadowfacts.simplemultipart.multipart;

import net.shadowfacts.simplemultipart.container.MultipartContainer;
import net.shadowfacts.simplemultipart.multipart.Multipart;
import net.shadowfacts.simplemultipart.multipart.MultipartState;
import net.shadowfacts.simplemultipart.multipart.entity.MultipartEntity;

/**
 * A view of a multipart and its (optional) associated entity.
 *
 * @author shadowfacts
 * @since 0.1.0
 */
// TODO: better name for this
public interface MultipartView {

	/**
	 * @return The container holding this part.
	 */
	MultipartContainer getContainer();

	/**
	 * @return The current state of this multipart.
	 */
	MultipartState getState();

	/**
	 * Sets the new state of this multipart.
	 *
	 * Note: This should only be used to change the state of the current {@link Multipart}.
	 * Changing to a different multipart should be done by removing/breaking the part in the container and then
	 * inserting the new one.
	 *
	 * Setting the state of a multipart will <b>not</b> cause the associated entity to change.
	 *
	 * @param state The new state.
	 */
	void setState(MultipartState state);

	/**
	 * @return The entity associated with this part.
	 */
	/*@Nullable*/ MultipartEntity getEntity();

	/**
	 * Sets the entity associated with this part.
	 *
	 * This should not usually be called. The associated multipart entity will be created/set by {@link MultipartContainer#insert(MultipartState)}
	 *
	 * @param entity The new entity.
	 */
	void setEntity(MultipartEntity entity);

	/**
	 * Helper method for retrieving the multipart object for the current state.
	 * @return The current multipart.
	 */
	default Multipart getMultipart() {
		return getState().getMultipart();
	}

}
