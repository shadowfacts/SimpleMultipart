package net.shadowfacts.simplemultipart.multipart.entity;

import net.shadowfacts.simplemultipart.container.MultipartContainer;
import net.shadowfacts.simplemultipart.multipart.MultipartState;

/**
 * An interface to be applied to {@link net.shadowfacts.simplemultipart.multipart.Multipart}s that have entities.
 *
 * Analogous to {@link net.minecraft.block.BlockEntityProvider}.
 *
 * @author shadowfacts
 * @since 0.1.0
 */
public interface MultipartEntityProvider {

	/**
	 * Creates a new multipart entity for this part.
	 *
	 * @param state The state of this part.
	 * @param container The container this part is in.
	 * @return The new entity. {@code null} if there should not be an entity in this instance.
	 */
	/*@Nullable*/
	MultipartEntity createMultipartEntity(MultipartState state, MultipartContainer container);

}
