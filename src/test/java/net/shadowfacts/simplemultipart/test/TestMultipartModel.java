package net.shadowfacts.simplemultipart.test;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.BakedQuadFactory;
import net.minecraft.client.render.model.ModelRotationContainer;
import net.minecraft.client.render.model.json.ModelElementFace;
import net.minecraft.client.render.model.json.ModelElementTexture;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.sortme.Vector3f;
import net.minecraft.util.math.Direction;
import net.shadowfacts.simplemultipart.client.MultipartBakedModel;
import net.shadowfacts.simplemultipart.multipart.MultipartSlot;
import net.shadowfacts.simplemultipart.multipart.MultipartState;

import java.util.List;
import java.util.Random;

/**
 * @author shadowfacts
 */
public class TestMultipartModel implements MultipartBakedModel {

	private static final BakedQuadFactory QUAD_FACTORY = new BakedQuadFactory();

	private boolean flag;

	public TestMultipartModel(boolean flag) {
		this.flag = flag;
	}

	@Override
	public List<BakedQuad> getQuads(MultipartState state, MultipartSlot slot, Direction side, Random random) {
		if (side == null) {
			return ImmutableList.of(createQuad());
		}
		return ImmutableList.of();
	}

	private BakedQuad createQuad() {
		ModelElementTexture elementTexture = new ModelElementTexture(new float[]{0, 0, 16, 16}, 0);
		ModelElementFace face;
		Vector3f from;
		Vector3f to;
		Direction side;
		if (flag) {
			from = new Vector3f(0, 0, 0);
			to = new Vector3f(16, 1, 16);
			side = Direction.UP;
			face = new ModelElementFace(null, 0, "block/iron_block", elementTexture);
		} else {
			from = new Vector3f(0, 0, 0);
			to = new Vector3f(16, 16, 1);
			side = Direction.SOUTH;
			face = new ModelElementFace(null, 0, "block/gold_block", elementTexture);
		}
		Sprite sprite = MinecraftClient.getInstance().getSpriteAtlas().getSprite(SpriteAtlasTexture.BLOCK_ATLAS_TEX);
		ModelRotationContainer rotationContainer = new ModelRotationContainer() {};
		return QUAD_FACTORY.bake(from, to, face, sprite, side, rotationContainer, null, false);
	}

}
