package net.shadowfacts.simplemultipart.mixin.client;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ExtendedBlockView;
import net.shadowfacts.simplemultipart.client.util.RenderStateProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

/**
 * @author shadowfacts
 */
@Mixin(BlockRenderManager.class)
public abstract class MixinBlockRenderManager {

	@Shadow
	private BlockModelRenderer renderer;

	@Shadow
	public abstract BakedModel getModel(BlockState var1);

	@Inject(at = @At("HEAD"), method = "tesselateBlock", cancellable = true)
	public void tesselateBlock(BlockState state, BlockPos pos, ExtendedBlockView world, BufferBuilder buffer, Random random, CallbackInfoReturnable<Boolean> info) {
		Block block = state.getBlock();
		if (state.getRenderType() == BlockRenderType.MODEL && block instanceof RenderStateProvider) {
			RenderStateProvider provider = (RenderStateProvider)block;
			BlockState renderState = provider.getStateForRendering(state, pos, world);

			BakedModel model = getModel(state);
			boolean result = renderer.tesselate(world, model, renderState, pos, buffer, true, random, state.getRenderingSeed(pos));
			info.setReturnValue(result);
			info.cancel();
		}
	}

}
