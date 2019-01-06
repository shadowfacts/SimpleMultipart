package net.shadowfacts.simplemultipart.util;

import net.minecraft.util.BooleanBiFunction;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import java.util.function.BiFunction;
import java.util.function.DoubleFunction;

/**
 * @author shadowfacts
 */
public class ShapeUtils {

	public static boolean intersect(VoxelShape a, VoxelShape b) {
		// TODO: this seems very inefficient
		VoxelShape overlap = VoxelShapes.combine(a, b, BooleanBiFunction.AND);
		return !overlap.isEmpty();
	}

	public static boolean hasSolidSide(VoxelShape shape, Direction side) {
		if ((side.getDirection() == Direction.AxisDirection.POSITIVE && shape.getMaximum(side.getAxis()) < 1) || (side.getDirection() == Direction.AxisDirection.NEGATIVE && shape.getMinimum(side.getAxis()) > 0)) {
			return false;
		}
		for (Direction.Axis axis : Direction.Axis.values()) {
			if (axis == side.getAxis()) {
				continue;
			}

			if (shape.getMinimum(axis) > 0 || shape.getMaximum(axis) < 1) {
				return false;
			}
		}
		return true;
	}

}
