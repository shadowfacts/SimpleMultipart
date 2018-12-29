package net.shadowfacts.simplemultipart.client;

import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.fabricmc.fabric.api.client.model.ModelVariantProvider;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.client.render.model.MultipartUnbakedModel;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.ModelVariantMap;
import net.minecraft.client.render.model.json.WeightedUnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.shadowfacts.simplemultipart.SimpleMultipart;
import net.shadowfacts.simplemultipart.multipart.Multipart;
import net.shadowfacts.simplemultipart.multipart.MultipartState;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author shadowfacts
 */
public class MultipartVariantProvider implements ModelVariantProvider  {

	private static final Set<ModelIdentifier> multipartModels = new HashSet<>();

	private final ResourceManager resourceManager;
	private final Map<ModelIdentifier, UnbakedModel> unbakedModels = new HashMap<>();

	static void registerMultipartModels(ResourceManager resourceManager, Consumer<ModelIdentifier> adder) {
		for (Multipart part : SimpleMultipart.MULTIPART) {
			Identifier partId = SimpleMultipart.MULTIPART.getId(part);
			for (MultipartState state : part.getStateFactory().getStates()) {
				String variant = BlockModels.propertyMapToString(state.getEntries());
				ModelIdentifier id = new ModelIdentifier(partId, variant);
				multipartModels.add(id);
				adder.accept(id);
			}

			// TODO: should multiparts be able to control this?
			multipartModels.add(new ModelIdentifier(partId, "inventory"));
		}
	}

	public MultipartVariantProvider(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

	@Override
	public UnbakedModel loadModelVariant(ModelIdentifier id, ModelProviderContext context) throws ModelProviderException {
		if (!multipartModels.contains(id)) {
			return null;
		}

		try {
			return getOrLoadPartModel(id);
		} catch (IOException e) {
			throw new ModelProviderException("Exception encountered while loading multipart model " + id, e);
		}
	}

	private UnbakedModel getOrLoadPartModel(ModelIdentifier modelId) throws ModelProviderException, IOException {
		UnbakedModel existing = unbakedModels.get(modelId);
		if (existing != null) {
			return existing;
		}

		return loadPartModel(modelId);
	}

	private UnbakedModel loadPartModel(ModelIdentifier modelId) throws ModelProviderException, IOException {
		Identifier partId = new Identifier(modelId.getNamespace(), modelId.getPath());
		Identifier partStateDefId = new Identifier(partId.getNamespace(), "multipartstates/" + partId.getPath() + ".json");

		Multipart part = SimpleMultipart.MULTIPART.get(partId);
		MultipartFakeBlock blockAdapter = new MultipartFakeBlock(part);

		ModelVariantMap variantMap = loadPartVariantMap(blockAdapter, partStateDefId);

		if (variantMap.hasMultipartModel()) {
			MultipartUnbakedModel multipartUnbakedModel = variantMap.getMultipartMdoel();
			part.getStateFactory().getStates().forEach(state -> {
				ModelIdentifier stateModelId = new ModelIdentifier(partId, BlockModels.propertyMapToString(state.getEntries()));
				unbakedModels.put(stateModelId, multipartUnbakedModel);
			});
		} else {
			Map<String, WeightedUnbakedModel> variants = variantMap.getVariantMap();
			variants.forEach((variant, model) -> {
				unbakedModels.put(new ModelIdentifier(partId, variant), model);
			});
		}

		UnbakedModel model = unbakedModels.get(modelId);
		if (model == null) {
			throw new ModelProviderException("Loaded multipart state " + partStateDefId + " for model " + modelId + " but still missing model");
		}
		return model;
	}

	private ModelVariantMap loadPartVariantMap(MultipartFakeBlock blockAdapter, Identifier partStateDefId) throws IOException {
		Resource resource = null;
		Reader reader = null;
		try {
			resource = resourceManager.getResource(partStateDefId);
			reader = new InputStreamReader(resource.getInputStream());

			ModelVariantMap.DeserializationContext context = new ModelVariantMap.DeserializationContext();
			context.setStateFactory(blockAdapter.getStateFactory());
			return ModelVariantMap.deserialize(context, reader);
		} finally {
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(resource);
		}
	}

}
