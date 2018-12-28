package net.shadowfacts.simplemultipart.container;

import net.minecraft.block.BlockState;
import net.shadowfacts.simplemultipart.multipart.MultipartView;

import java.util.Set;

/**
 * @author shadowfacts
 */
public class ContainerBlockState extends BlockState {

	private Set<MultipartView> parts;

	public ContainerBlockState(BlockState delegate, Set<MultipartView> parts) {
		super(delegate.getBlock(), delegate.getEntries());
		this.parts = parts;
	}

	public Set<MultipartView> getParts() {
		return parts;
	}
}
