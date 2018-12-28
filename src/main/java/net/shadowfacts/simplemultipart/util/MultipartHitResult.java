package net.shadowfacts.simplemultipart.util;

import net.minecraft.util.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.shadowfacts.simplemultipart.multipart.MultipartView;

/**
 * @author shadowfacts
 */
public class MultipartHitResult extends HitResult {

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
