package net.shadowfacts.simplemultipart.mixin.client;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.VerticalEntityPosition;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.shadowfacts.simplemultipart.SimpleMultipart;
import net.shadowfacts.simplemultipart.container.MultipartContainer;
import net.shadowfacts.simplemultipart.util.MultipartHelper;
import net.shadowfacts.simplemultipart.util.MultipartHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * @author shadowfacts
 */
@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {

	@Redirect(method = "drawHighlightedBlockOutline", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getOutlineShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/VerticalEntityPosition;)Lnet/minecraft/util/shape/VoxelShape;"))
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, VerticalEntityPosition verticalEntityPosition) {
		if (state.getBlock() == SimpleMultipart.containerBlock || state.getBlock() == SimpleMultipart.tickableContainerBlock) {
			MultipartContainer container = (MultipartContainer)world.getBlockEntity(pos);
			MultipartHitResult result = MultipartHelper.rayTrace(container, world, pos, MinecraftClient.getInstance().player);
			if (result != null && result.view != null) {
				return result.view.getState().getBoundingShape(result.view);
			}
		}

		return state.getOutlineShape(world, pos);
	}

}
