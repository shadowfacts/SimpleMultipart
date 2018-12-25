package net.shadowfacts.simplemultipart.client;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.util.math.Direction;
import net.shadowfacts.simplemultipart.api.MultipartView;

import java.util.List;
import java.util.Random;

/**
 * @author shadowfacts
 */
public interface MultipartBakedModel extends BakedModel {

	List<BakedQuad> getMultipartQuads(MultipartView view, Direction side, Random random);

	@Override
	default List<BakedQuad> getQuads(BlockState state, Direction side, Random random) {
		return getMultipartQuads(null, side, random);
	}

}
