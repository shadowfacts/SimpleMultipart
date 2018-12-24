package net.shadowfacts.simplemultipart.util;

import net.minecraft.util.BooleanBiFunction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

/**
 * @author shadowfacts
 */
public class ShapeUtils {

	public static boolean intersect(VoxelShape a, VoxelShape b) {
		VoxelShape overlap = VoxelShapes.combine(a, b, BooleanBiFunction.AND);
		return !overlap.isEmpty();
	}

}
