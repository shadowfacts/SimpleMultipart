package net.shadowfacts.simplemultipart.multipart.entity;

import net.shadowfacts.simplemultipart.api.MultipartContainer;
import net.shadowfacts.simplemultipart.multipart.MultipartState;

/**
 * @author shadowfacts
 */
public interface MultipartEntityProvider {

	/*@Nullable*/
	MultipartEntity createMultipartEntity(MultipartState state, MultipartContainer container);

}
