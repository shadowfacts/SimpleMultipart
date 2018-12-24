package net.shadowfacts.simplemultipart.client;

import net.fabricmc.fabric.api.client.model.ModelProvider;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.BlockModels;
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
public class MultipartModelProvider implements ModelProvider {

	private static final Set<ModelIdentifier> multipartModels = new HashSet<>();

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

	@Override
	public UnbakedModel load(Identifier id, Context context) throws ModelProviderException {
		if (!(id instanceof ModelIdentifier)) {
			return null;
		}
		ModelIdentifier modelId = (ModelIdentifier)id;


		if (!multipartModels.contains(modelId)) {
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
