package net.shadowfacts.simplemultipart.mixin.client;

import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.shadowfacts.simplemultipart.client.MultipartContainerBakedModel;
import net.shadowfacts.simplemultipart.client.SimpleMultipartClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

/**
 * @author shadowfacts
 */
@Mixin(ModelLoader.class)
public abstract class MixinModelLoader {

	@Shadow
	private Map<Identifier, BakedModel> bakedModels;

	@Shadow
	public abstract void addModel(ModelIdentifier id);

	@Inject(method = "<init>", at = @At("RETURN"))
	public void addMultipartModel(ResourceManager manager, SpriteAtlasTexture texture, CallbackInfo info) {
		bakedModels.put(new ModelIdentifier("simplemultipart:container#"), new MultipartContainerBakedModel());
	}

//	// temporary workaround until fabric-api #39 is merged
//	@Inject(method = "addModel", at = @At("HEAD"))
//	public void injectMultipartModels(ModelIdentifier id, CallbackInfo info) {
//		if (id == ModelLoader.MISSING) {
//			SimpleMultipartClient.getMultipartModelIds().forEach(this::addModel);
//		}
//	}

}
