package net.shadowfacts.simplemultipart.client;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelItemPropertyOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.Direction;
import net.shadowfacts.simplemultipart.container.MultipartContainerBlockState;
import net.shadowfacts.simplemultipart.multipart.MultipartSlot;
import net.shadowfacts.simplemultipart.multipart.MultipartState;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author shadowfacts
 */
public class MultipartContainerBakedModel implements BakedModel {

	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction side, Random random) {
		if (!(state instanceof MultipartContainerBlockState)) {
			return null;
		}
		MultipartContainerBlockState containerState = (MultipartContainerBlockState)state;
		Map<MultipartSlot, MultipartState> parts = containerState.getParts();
		MultipartModels models = SimpleMultipartClient.getMultipartModels();
		// TODO: would manually building the list be more efficient?
		return parts.entrySet().stream()
				.flatMap(entry -> {
					MultipartBakedModel partModel = models.getModel(entry.getValue());
					return partModel.getQuads(entry.getValue(), entry.getKey(), side, random).stream();
				})
				.collect(Collectors.toList());
	}

	@Override
	public boolean useAmbientOcclusion() {
		return true;
	}

	@Override
	public boolean hasDepthInGui() {
		return false;
	}

	@Override
	public boolean isBuiltin() {
		return false;
	}

	@Override
	public Sprite getSprite() {
		return MinecraftClient.getInstance().getSpriteAtlas().getSprite("blocks/stone");
	}

	@Override
	public ModelTransformation getTransformations() {
		return ModelTransformation.ORIGIN;
	}

	@Override
	public ModelItemPropertyOverrideList getItemPropertyOverrides() {
		return ModelItemPropertyOverrideList.ORIGIN;
	}
}
