package net.shadowfacts.simplemultipart.mixin.client;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RenderTypeBlock;
import net.minecraft.client.render.VertexBuffer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ExtendedBlockView;
import net.shadowfacts.simplemultipart.client.RenderStateProvider;
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
	private BlockRenderer renderer;
	@Shadow
	private Random field_4169;

	@Shadow
	public abstract BakedModel getModel(BlockState var1);

	@Inject(at = @At("HEAD"), method = "method_3355", cancellable = true)
	public void method_3355(BlockState state, BlockPos pos, ExtendedBlockView world, VertexBuffer buffer, Random random, CallbackInfoReturnable<Boolean> info) {
		Block block = state.getBlock();
		if (state.getRenderType() == RenderTypeBlock.MODEL && block instanceof RenderStateProvider) {
			RenderStateProvider provider = (RenderStateProvider)block;
			BlockState renderState = provider.getStateForRendering(state, pos, world);

			BakedModel model = getModel(state);
			boolean result = renderer.tesselate(world, model, renderState, pos, buffer, true, random, state.getPosRandom(pos));
			info.setReturnValue(result);
			info.cancel();
		}
	}

}
