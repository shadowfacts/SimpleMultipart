package net.shadowfacts.simplemultipart.client;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelItemPropertyOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.shadowfacts.simplemultipart.SimpleMultipart;
import net.shadowfacts.simplemultipart.container.ContainerBlockState;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author shadowfacts
 */
public class MultipartContainerBakedModel implements BakedModel {

	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction side, Random random) {
		if (!(state instanceof ContainerBlockState)) {
			return null;
		}
		ContainerBlockState containerState = (ContainerBlockState)state;
		// TODO: would manually building the list be more efficient?
		return containerState.getParts().stream()
				.flatMap(view -> {
					Identifier partId = SimpleMultipart.MULTIPART.getId(view.getMultipart());
					String variant = BlockModels.propertyMapToString(view.getState().getEntries());
					ModelIdentifier modelId = new ModelIdentifier(partId, variant);
					BakedModel model = MinecraftClient.getInstance().getBakedModelManager().getModel(modelId);
					if (model instanceof MultipartBakedModel) {
						return ((MultipartBakedModel)model).getMultipartQuads(view, side, random).stream();
					} else {
						BlockState fakeState = null;

						// Need to use the same fake block state as used when loading multi-models
						// otherwise MultipartBakedModel will return no quads for a null state
						MultipartFakeBlock fakeBlock = MultipartFakeBlock.fakeBlocks.get(partId);
						if (fakeBlock != null) {
							fakeState = fakeBlock.getFakeState(view.getState());
						}

						return model.getQuads(fakeState, side, random).stream();
					}
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
		return MinecraftClient.getInstance().getSpriteAtlas().getSprite("block/stone");
	}

	@Override
	public ModelTransformation getTransformation() {
		return ModelTransformation.ORIGIN;
	}

	@Override
	public ModelItemPropertyOverrideList getItemPropertyOverrides() {
		return ModelItemPropertyOverrideList.ORIGIN;
	}
}
