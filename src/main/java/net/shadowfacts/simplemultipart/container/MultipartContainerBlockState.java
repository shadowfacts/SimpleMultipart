package net.shadowfacts.simplemultipart.container;

import net.minecraft.block.BlockState;
import net.shadowfacts.simplemultipart.api.MultipartView;

import java.util.Set;

/**
 * @author shadowfacts
 */
public class MultipartContainerBlockState extends BlockState {

	private Set<MultipartView> parts;

	public MultipartContainerBlockState(BlockState delegate, Set<MultipartView> parts) {
		super(delegate.getBlock(), delegate.getEntries());
		this.parts = parts;
	}

	public Set<MultipartView> getParts() {
		return parts;
	}
}
