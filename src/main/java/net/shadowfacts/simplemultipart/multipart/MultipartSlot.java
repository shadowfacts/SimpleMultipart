package net.shadowfacts.simplemultipart.multipart;

import net.minecraft.util.StringRepresentable;
import net.minecraft.util.math.Direction;

/**
 * @author shadowfacts
 */
public enum MultipartSlot implements StringRepresentable  {
	TOP,
	BOTTOM,
	NORTH,
	SOUTH,
	EAST,
	WEST,
	CENTER;

	public static MultipartSlot fromClickedSide(Direction side) {
		switch (side) {
			case UP:
				return BOTTOM;
			case DOWN:
				return TOP;
			case NORTH:
				return SOUTH;
			case SOUTH:
				return NORTH;
			case EAST:
				return WEST;
			case WEST:
				return EAST;
		}
		throw new RuntimeException("Unreachable: got direction outside of DUNSWE");
	}

	@Override
	public String asString() {
		return name().toLowerCase();
	}
}
