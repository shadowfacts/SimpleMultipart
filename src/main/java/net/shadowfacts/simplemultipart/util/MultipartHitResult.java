package net.shadowfacts.simplemultipart.util;

import net.minecraft.util.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.shadowfacts.simplemultipart.multipart.MultipartState;

/**
 * @author shadowfacts
 */
public class MultipartHitResult extends HitResult {

	public MultipartState partState;

	public MultipartHitResult(Vec3d pos, Direction side, BlockPos blockPos, MultipartState partState) {
		super(pos, side, blockPos);
		this.partState = partState;
	}

	public MultipartHitResult(HitResult result, MultipartState partState) {
		this(result.pos, result.side, result.getBlockPos(), partState);
		if (result.type != Type.BLOCK) {
			throw new IllegalArgumentException("Can't create a MultipartHitResult from a non BLOCK-type HitResult");
		}
	}

	@Override
	public String toString() {
		return "HitResult{type=" + type + ", blockpos=" + getBlockPos() + ", f=" + side + ", pos=" + pos + ", partState=" + partState + '}';
	}
}
