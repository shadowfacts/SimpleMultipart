package net.shadowfacts.simplemultipart.api;

import net.shadowfacts.simplemultipart.container.MultipartContainerBlockEntity;
import net.shadowfacts.simplemultipart.multipart.Multipart;
import net.shadowfacts.simplemultipart.multipart.MultipartState;
import net.shadowfacts.simplemultipart.multipart.entity.MultipartEntity;

/**
 * @author shadowfacts
 */
// TODO: better name for this
public interface MultipartView {

	MultipartContainerBlockEntity getContainer();

	MultipartState getState();

	MultipartEntity getEntity();

	default Multipart getMultipart() {
		return getState().getMultipart();
	}

}
