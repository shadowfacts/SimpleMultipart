package net.shadowfacts.simplemultipart.client;

import net.fabricmc.fabric.api.client.model.ModelProvider;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.ModelVariantMap;
import net.minecraft.client.render.model.json.WeightedUnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shadowfacts
 */
public class MultipartModelProvider implements ModelProvider {

	private final Map<ModelIdentifier, UnbakedModel> unbakedModels = new HashMap<>();

	@Override
	public UnbakedModel load(Identifier id, Context context) throws ModelProviderException {
		if (!(id instanceof ModelIdentifier)) {
			return null;
		}
		ModelIdentifier modelId = (ModelIdentifier)id;


		if (!SimpleMultipartClient.multipartModels.contains(modelId)) {
			return null;
		}

		try {
			return getOrLoadPartModel(modelId);
		} catch (IOException e) {
			throw new ModelProviderException("Exception encountered while loading model " + id, e);
		}
	}

	private UnbakedModel getOrLoadPartModel(ModelIdentifier id) throws ModelProviderException, IOException {
		UnbakedModel existing = unbakedModels.get(id);
		if (existing != null) {
			return existing;
		}

		return loadModel(id);
	}

	private UnbakedModel loadModel(ModelIdentifier id) throws ModelProviderException, IOException {
		Identifier partStateId = new Identifier(id.getNamespace(), "multipartstates/" + id.getPath() + ".json");
		ModelVariantMap variantMap = loadVariantMap(partStateId);
		Map<String, WeightedUnbakedModel> variants = variantMap.method_3423();
		variants.forEach((variant, model) -> {
			unbakedModels.put(new ModelIdentifier(new Identifier(id.getNamespace(), id.getPath()), variant), model);
		});

		UnbakedModel model = unbakedModels.get(id);
		if (model == null) {
			throw new ModelProviderException("Loaded multipart state " + partStateId + " for model " + id + " but still missing model");
		}
		return model;
	}

	private ModelVariantMap loadVariantMap(Identifier id) throws IOException {
		Resource resource = null;
		Reader reader = null;
		try {
			resource = MinecraftClient.getInstance().getResourceManager().getResource(id);
			reader = new InputStreamReader(resource.getInputStream());

			ModelVariantMap.class_791 context = new ModelVariantMap.class_791();
			// context.stateFactory =
			// TODO: ^ blockstate translation
			return ModelVariantMap.method_3424(context, reader);
		} finally {
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(resource);
		}
	}

}
