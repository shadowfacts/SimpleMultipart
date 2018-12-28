package net.shadowfacts.simplemultipart.client;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.util.math.Direction;
import net.shadowfacts.simplemultipart.multipart.MultipartView;

import java.util.List;
import java.util.Random;

/**
 * An extension of the {@link BakedModel} interface for custom code-based multipart models.
 *
 * Note: Currently there is no proper way to register these. They can be registered by mixing in to the {@link net.minecraft.client.render.model.ModelLoader}
 * constructor and injecting them into the {@code bakedModels} map.
 *
 * @author shadowfacts
 * @since 0.1.0
 */
public interface MultipartBakedModel extends BakedModel {

	/**
	 * Retrieves the quads for a given side of the multipart.
	 *
	 * @param view The view of the multipart in the world.
	 *             {@code null} if the default {@link MultipartBakedModel#getQuads(BlockState, Direction, Random)} implementation
	 *             is invoked.
	 * @param side The side that quads are being requested for.
	 * @param random The position-specific random.
	 * @return The quads for the given side. The returned quads will be culled whenever the {@code side} is obscured.
	 */
	List<BakedQuad> getMultipartQuads(/*@Nullable*/ MultipartView view, Direction side, Random random);

	@Override
	default List<BakedQuad> getQuads(BlockState state, Direction side, Random random) {
		return getMultipartQuads(null, side, random);
	}

}
