package net.shadowfacts.simplemultipart.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.BlockHitResult;
import net.minecraft.util.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.shadowfacts.simplemultipart.container.MultipartContainer;
import net.shadowfacts.simplemultipart.multipart.MultipartView;
import sun.jvm.hotspot.opto.Block;

/**
 * A raytrace result for a multipart.
 *
 * @author shadowfacts
 * @since 0.1.0
 * @see MultipartHelper#rayTrace(MultipartContainer, net.minecraft.world.BlockView, BlockPos, PlayerEntity)
 */
public class MultipartHitResult extends BlockHitResult  {

	/**
	 * The view of the hit multipart.
	 */
	public MultipartView view;

	public MultipartHitResult(Vec3d pos, Direction side, BlockPos blockPos, MultipartView view) {
		super(pos, side, blockPos, false); // TODO: what does this boolean do?
		this.view = view;
	}

	public MultipartHitResult(BlockHitResult result, MultipartView view) {
		this(result.getPos(), result.getSide(), result.getBlockPos(), view);
	}

	@Override
	public String toString() {
		return "HitResult{type=" + getType() + ", blockpos=" + getBlockPos() + ", f=" + getSide() + ", pos=" + pos + ", view=" + view + '}';
	}
}
