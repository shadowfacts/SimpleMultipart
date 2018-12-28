package net.shadowfacts.simplemultipart.util;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.state.PropertyContainer;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.Property;
import net.minecraft.util.HitResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import net.shadowfacts.simplemultipart.SimpleMultipart;
import net.shadowfacts.simplemultipart.container.MultipartContainer;
import net.shadowfacts.simplemultipart.multipart.Multipart;
import net.shadowfacts.simplemultipart.multipart.MultipartState;

import java.util.*;

/**
 * @author shadowfacts
 */
public class MultipartHelper {

	public static MultipartHitResult rayTrace(MultipartContainer container, World world, BlockPos pos, PlayerEntity player) {
		// copied from BoatItem::use
		float var6 = MathHelper.lerp(1.0F, player.prevPitch, player.pitch);
		float var7 = MathHelper.lerp(1.0F, player.prevYaw, player.yaw);
		double var8 = MathHelper.lerp(1.0D, player.prevX, player.x);
		double var10 = MathHelper.lerp(1.0D, player.prevY, player.y) + (double)player.getEyeHeight();
		double var12 = MathHelper.lerp(1.0D, player.prevZ, player.z);
		Vec3d start = new Vec3d(var8, var10, var12);

		float var15 = MathHelper.cos(-var7 * 0.017453292F - 3.1415927F);
		float var16 = MathHelper.sin(-var7 * 0.017453292F - 3.1415927F);
		float var17 = -MathHelper.cos(-var6 * 0.017453292F);
		float var18 = MathHelper.sin(-var6 * 0.017453292F);
		float var19 = var16 * var17;
		float var21 = var15 * var17;
		Vec3d end = start.add((double)var19 * 5.0D, (double)var18 * 5.0D, (double)var21 * 5.0D);

		return rayTrace(container, world, pos, start, end);
	}

	public static MultipartHitResult rayTrace(MultipartContainer container, World world, BlockPos pos, Vec3d start, Vec3d end) {
		return container.getParts().stream()
				.map(view -> {
					VoxelShape shape = view.getState().getBoundingShape(view);
					HitResult result = shape.rayTrace(start, end, pos);
					return result == null ? null : new MultipartHitResult(result, view);
				})
				.filter(Objects::nonNull)
				.min(Comparator.comparingDouble(hit -> hit.pos.subtract(start).lengthSquared()))
				.orElse(null);
	}

	public static CompoundTag serializeMultipartState(MultipartState state) {
		CompoundTag tag = new CompoundTag();
		tag.putString("Name", SimpleMultipart.MULTIPART.getId(state.getMultipart()).toString());

		ImmutableMap<Property<?>, Comparable<?>> propertyMap = state.getEntries();
		if (!propertyMap.isEmpty()) {
			CompoundTag propertyTag = new CompoundTag();

			for (Map.Entry<Property<?>, Comparable<?>> e : propertyMap.entrySet()) {
				Property<?> property = e.getKey();
				String str = getValueAsString(state, property);
				propertyTag.putString(property.getName(), str);
			}

			tag.put("Properties", propertyTag);
		}

		return tag;
	}

	private static <C extends PropertyContainer<C>, T extends Comparable<T>> String getValueAsString(C state, Property<T> property) {
		return property.getValueAsString(state.get(property));
	}

	public static MultipartState deserializeMultipartState(CompoundTag tag) {
		if (!tag.containsKey("Name", 8)) {
			return null;
		} else {
			Multipart part = SimpleMultipart.MULTIPART.get(new Identifier(tag.getString("Name")));
			MultipartState state = part.getDefaultState();

			if (tag.containsKey("Properties", 10)) {
				CompoundTag propertyTag = tag.getCompound("Properties");

				StateFactory<Multipart, MultipartState> stateFactory = part.getStateFactory();

				for (String propertyName : propertyTag.getKeys()) {
					Property<?> property = stateFactory.getProperty(propertyName);

					if (property != null) {
						String valueStr = propertyTag.getString(propertyName);
						state = withProperty(state, property, valueStr);
					}
				}
			}

			return state;
		}
	}

	private static <C extends PropertyContainer<C>, T extends Comparable<T>> C withProperty(C state, Property<T> property, String valueString) {
		Optional<T> value = property.getValue(valueString);
		if (!value.isPresent()) {
			// TODO: logging
			return state;
		}
		return state.with(property, value.get());
	}

}
