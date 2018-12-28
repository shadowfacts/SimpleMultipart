package net.shadowfacts.simplemultipart.multipart;

import net.shadowfacts.simplemultipart.container.MultipartContainer;
import net.shadowfacts.simplemultipart.multipart.Multipart;
import net.shadowfacts.simplemultipart.multipart.MultipartState;
import net.shadowfacts.simplemultipart.multipart.entity.MultipartEntity;

/**
 * @author shadowfacts
 */
// TODO: better name for this
public interface MultipartView {

	MultipartContainer getContainer();

	MultipartState getState();

	void setState(MultipartState state);

	MultipartEntity getEntity();

	void setEntity(MultipartEntity entity);

	default Multipart getMultipart() {
		return getState().getMultipart();
	}

}
