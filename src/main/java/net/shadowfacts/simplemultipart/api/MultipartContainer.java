package net.shadowfacts.simplemultipart.api;

import net.shadowfacts.simplemultipart.multipart.MultipartState;

import java.util.Set;

/**
 * @author shadowfacts
 */
public interface MultipartContainer {

	Set<MultipartView> getParts();

	boolean canInsert(MultipartState state);

	void insert(MultipartState state);

	void remove(MultipartState state);

	boolean breakPart(MultipartState state);

}
