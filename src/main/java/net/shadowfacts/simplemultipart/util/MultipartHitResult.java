package net.shadowfacts.simplemultipart.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.shadowfacts.simplemultipart.container.MultipartContainer;
import net.shadowfacts.simplemultipart.multipart.MultipartView;

/**
 * A raytrace result for a multipart.
 *
 * @author shadowfacts
 * @since 0.1.0
 * @see MultipartHelper#rayTrace(MultipartContainer, World, BlockPos, PlayerEntity)
 */
public class MultipartHitResult extends HitResult {

	/**
	 * The view of the hit multipart.
	 */
	public MultipartView view;

	public MultipartHitResult(Vec3d pos, Direction side, BlockPos blockPos, MultipartView view) {
		super(pos, side, blockPos);
		this.view = view;
	}

	public MultipartHitResult(HitResult result, MultipartView view) {
		this(result.pos, result.side, result.getBlockPos(), view);
		if (result.type != Type.BLOCK) {
			throw new IllegalArgumentException("Can't create a MultipartHitResult from a non BLOCK-type HitResult");
		}
	}

	@Override
	public String toString() {
		return "HitResult{type=" + type + ", blockpos=" + getBlockPos() + ", f=" + side + ", pos=" + pos + ", view=" + view + '}';
	}
}
