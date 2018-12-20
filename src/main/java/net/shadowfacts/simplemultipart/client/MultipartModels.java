package net.shadowfacts.simplemultipart.client;

import net.shadowfacts.simplemultipart.multipart.MultipartState;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * @author shadowfacts
 */
public class MultipartModels {

	private final Map<MultipartState, MultipartBakedModel> models = new IdentityHashMap<>();
	private final MultipartBakedModel missingModel = new MissingMultipartBakedModel();

	public MultipartBakedModel getMissingModel() {
		return missingModel;
	}

	public MultipartBakedModel getModel(MultipartState state) {
		MultipartBakedModel model = models.get(state);
		if (model == null) {
			return getMissingModel();
		}
		return model;
	}

	public void register(MultipartState state, MultipartBakedModel model) {
		models.put(state, model);
	}
}
