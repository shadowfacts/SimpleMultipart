package net.shadowfacts.simplemultipart.client;

import net.fabricmc.api.ClientModInitializer;
import net.shadowfacts.simplemultipart.multipart.MultipartState;

/**
 * @author shadowfacts
 */
public class SimpleMultipartClient implements ClientModInitializer {

	private static MultipartModels multipartModels = new MultipartModels();

	@Override
	public void onInitializeClient() {
	}

	public static MultipartModels getMultipartModels() {
		return multipartModels;
	}

	public static void registerModel(MultipartState state, MultipartBakedModel model) {
		getMultipartModels().register(state, model);
	}

}
