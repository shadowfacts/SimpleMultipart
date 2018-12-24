package net.shadowfacts.simplemultipart.client;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import net.shadowfacts.simplemultipart.SimpleMultipart;
import net.shadowfacts.simplemultipart.multipart.Multipart;
import net.shadowfacts.simplemultipart.multipart.MultipartState;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shadowfacts
 */
public class MultipartFakeBlock extends Block {

	public static final Map<Identifier, MultipartFakeBlock> fakeBlocks = new HashMap<>();

	private final Multipart multipart;
	private final StateFactory<Block, BlockState> fakeStateFactory;

	public MultipartFakeBlock(Multipart multipart) {
		super(Settings.of(Material.AIR));

		this.multipart = multipart;
		this.fakeStateFactory = createFakeStateFactory();

		setDefaultState(fakeStateFactory.getDefaultState());

		Identifier partId = SimpleMultipart.MULTIPART.getId(multipart);
		fakeBlocks.put(partId, this);
	}

	private StateFactory<Block, BlockState> createFakeStateFactory() {
		StateFactory.Builder<Block, BlockState> builder = new StateFactory.Builder<>(this);
		multipart.getStateFactory().getProperties().forEach(builder::with);
		return builder.build(BlockState::new);
	}

	@Override
	public StateFactory<Block, BlockState> getStateFactory() {
		return fakeStateFactory;
	}

	public BlockState getFakeState(MultipartState state) {
		BlockState fakeState = getDefaultState();
		for (Map.Entry<Property<?>, Comparable<?>> e : state.getEntries().entrySet()) {
			fakeState = with(fakeState, e.getKey(), e.getValue());
		}
		return fakeState;
	}

	private static BlockState with(BlockState state, Property prop, Comparable value) {
		return state.with(prop, value);
	}

}
