package net.shadowfacts.simplemultipart.mixin.client;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.state.property.Property;
import net.minecraft.util.BlockHitResult;
import net.minecraft.util.HitResult;
import net.shadowfacts.simplemultipart.SimpleMultipart;
import net.shadowfacts.simplemultipart.container.MultipartContainer;
import net.shadowfacts.simplemultipart.multipart.Multipart;
import net.shadowfacts.simplemultipart.multipart.MultipartState;
import net.shadowfacts.simplemultipart.util.MultipartHelper;
import net.shadowfacts.simplemultipart.util.MultipartHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;

/**
 * @author shadowfacts
 */
@Mixin(DebugHud.class)
public abstract class MixinDebugHud {

	@Shadow
	private MinecraftClient client;
	@Shadow
	private HitResult blockHit;

	@Shadow
	public abstract String method_1845(Map.Entry<Property<?>, Comparable<?>> map$Entry_1);

	@Inject(method = "getRightText", at = @At("RETURN"))
	public void getRightText(CallbackInfoReturnable<List<String>> info) {
		if (!client.hasReducedDebugInfo() && blockHit != null && blockHit.getType() == HitResult.Type.BLOCK) {
			BlockHitResult hitResult = (BlockHitResult)blockHit;
			BlockEntity entity = client.world.getBlockEntity(hitResult.getBlockPos());
			if (entity instanceof MultipartContainer) {
				MultipartContainer container = (MultipartContainer)entity;
				MultipartHitResult result = MultipartHelper.rayTrace(container, client.world, hitResult.getBlockPos(), client.player);
				if (result != null && result.view != null) {
					info.getReturnValue().add("");
					info.getReturnValue().add("Targeted Multipart");
					MultipartState state = result.view.getState();
					Multipart part = state.getMultipart();
					info.getReturnValue().add(String.valueOf(SimpleMultipart.MULTIPART.getId(part)));
					for (Map.Entry<Property<?>, Comparable<?>> e : state.getEntries().entrySet()) {
						info.getReturnValue().add(method_1845(e));
					}
				}
			}
		}
	}

}
