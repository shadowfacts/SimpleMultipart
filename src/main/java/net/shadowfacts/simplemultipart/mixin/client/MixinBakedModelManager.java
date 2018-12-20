package net.shadowfacts.simplemultipart.mixin.client;

import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import net.shadowfacts.simplemultipart.client.MultipartContainerBakedModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;

/**
 * @author shadowfacts
 */
@Mixin(BakedModelManager.class)
public class MixinBakedModelManager {

	@Redirect(method = "onResourceReload", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/model/ModelLoader;getBakedModelMap()Ljava/util/Map;"))
	public Map<Identifier, BakedModel> getBakedModelMap(ModelLoader loader) {
		Map<Identifier, BakedModel> map = loader.getBakedModelMap();
		map.put(new ModelIdentifier("simplemultipart:container#"), new MultipartContainerBakedModel());
		return map;
	}

}
