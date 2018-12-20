package net.shadowfacts.simplemultipart.client;

import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.util.math.Direction;
import net.shadowfacts.simplemultipart.multipart.MultipartSlot;
import net.shadowfacts.simplemultipart.multipart.MultipartState;

import java.util.List;
import java.util.Random;

/**
 * @author shadowfacts
 */
public interface MultipartBakedModel {

	List<BakedQuad> getQuads(MultipartState state, MultipartSlot slot, Direction side, Random random);

}
