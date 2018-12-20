package net.shadowfacts.simplemultipart.util;

import net.minecraft.util.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.shadowfacts.simplemultipart.multipart.MultipartSlot;

/**
 * @author shadowfacts
 */
public class MultipartHitResult extends HitResult {

	public MultipartSlot partSlot;

	public MultipartHitResult(Vec3d pos, Direction side, BlockPos blockPos, MultipartSlot partSlot) {
		super(pos, side, blockPos);
		this.partSlot = partSlot;
	}

	public MultipartHitResult(HitResult result, MultipartSlot partSlot) {
		this(result.pos, result.side, result.getBlockPos(), partSlot);
		if (result.type != Type.BLOCK) {
			throw new IllegalArgumentException("Can't create a MultipartHitResult from a non BLOCK-type HitResult");
		}
	}

	@Override
	public String toString() {
		return "HitResult{type=" + type + ", blockpos=" + getBlockPos() + ", f=" + side + ", pos=" + pos + ", partSlot=" + partSlot + '}';
	}
}
