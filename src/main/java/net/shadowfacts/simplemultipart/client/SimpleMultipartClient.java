package net.shadowfacts.simplemultipart.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.impl.client.model.ModelLoadingRegistryImpl;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.shadowfacts.simplemultipart.SimpleMultipart;
import net.shadowfacts.simplemultipart.multipart.Multipart;
import net.shadowfacts.simplemultipart.multipart.MultipartState;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author shadowfacts
 */
public class SimpleMultipartClient implements ClientModInitializer {

	public static final Set<ModelIdentifier> multipartModels = new HashSet<>();

	@Override
	public void onInitializeClient() {
		ModelLoadingRegistryImpl.INSTANCE.registerAppender(SimpleMultipartClient::registerMultipartModels);
		ModelLoadingRegistryImpl.INSTANCE.registerProvider(resourceManager -> new MultipartModelProvider());
	}

	private static void registerMultipartModels(ResourceManager resourceManager, Consumer<ModelIdentifier> adder) {
		for (Multipart part : SimpleMultipart.MULTIPART) {
			Identifier partId = SimpleMultipart.MULTIPART.getId(part);
			for (MultipartState state : part.getStateFactory().getStates()) {
				String variant = BlockModels.propertyMapToString(state.getEntries());
				ModelIdentifier id = new ModelIdentifier(partId, variant);
				multipartModels.add(id);
				adder.accept(id);
			}
		}
	}

}
