package net.shadowfacts.simplemultipart.container;

import net.minecraft.block.BlockState;
import net.shadowfacts.simplemultipart.multipart.MultipartState;

import java.util.Map;
import java.util.Set;

/**
 * @author shadowfacts
 */
public class MultipartContainerBlockState extends BlockState {

	private Set<MultipartState> parts;

	public MultipartContainerBlockState(BlockState delegate, Set<MultipartState> parts) {
		super(delegate.getBlock(), delegate.getEntries());
		this.parts = parts;
	}

	public Set<MultipartState> getParts() {
		return parts;
	}
}
