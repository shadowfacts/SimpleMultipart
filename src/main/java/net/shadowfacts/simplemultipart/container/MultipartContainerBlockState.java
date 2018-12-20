package net.shadowfacts.simplemultipart.container;

import net.minecraft.block.BlockState;
import net.shadowfacts.simplemultipart.multipart.MultipartSlot;
import net.shadowfacts.simplemultipart.multipart.MultipartState;

import java.util.Map;

/**
 * @author shadowfacts
 */
public class MultipartContainerBlockState extends BlockState {

	private Map<MultipartSlot, MultipartState> parts;

	public MultipartContainerBlockState(BlockState delegate, Map<MultipartSlot, MultipartState> parts) {
		super(delegate.getBlock(), delegate.getEntries());
		this.parts = parts;
	}

	public Map<MultipartSlot, MultipartState> getParts() {
		return parts;
	}
}
