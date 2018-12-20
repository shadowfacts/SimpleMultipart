package net.shadowfacts.simplemultipart.mixin.client;

import net.minecraft.client.render.block.BiomeColors;
import net.minecraft.client.render.block.GrassColorHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ExtendedBlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

/**
 * @author shadowfacts
 */
@Mixin(BiomeColors.class)
public class MixinFuckYouBiomeColors {

	@Overwrite
	public static int grassColorAt(ExtendedBlockView world, BlockPos pos) {
		return GrassColorHandler.getColor(0.5D, 1.0D);
	}

}
