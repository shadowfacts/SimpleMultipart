package net.shadowfacts.simplemultipart.test;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.StringTextComponent;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.shadowfacts.simplemultipart.container.MultipartContainerBlockEntity;
import net.shadowfacts.simplemultipart.multipart.Multipart;
import net.shadowfacts.simplemultipart.multipart.MultipartState;
import net.shadowfacts.simplemultipart.multipart.entity.MultipartEntity;
import net.shadowfacts.simplemultipart.multipart.entity.MultipartEntityProvider;
import net.shadowfacts.simplemultipart.api.MultipartView;

import java.util.function.Function;

/**
 * @author shadowfacts
 */
public class EntityTestPart extends Multipart implements MultipartEntityProvider<EntityTestPart.Entity> {

	@Override
	public VoxelShape getBoundingShape(MultipartState state, MultipartView view) {
		return VoxelShapes.cube(6/16f, 6/16f, 6/16f, 10/16f, 10/16f, 10/16f);
	}

	@Override
	@Deprecated
	public boolean activate(MultipartState state, MultipartView view, PlayerEntity player, Hand hand) {
		BlockPos pos = ((Entity)view.getEntity()).getPos();
		player.addChatMessage(new StringTextComponent("Clicked: " + pos), false);
		return true;
	}

	@Override
	public Function<MultipartContainerBlockEntity, Entity> getMultipartEntityFactory() {
		return Entity::new;
	}

	public static class Entity extends MultipartEntity {
		public Entity(MultipartContainerBlockEntity container) {
			super(container);
		}

		public BlockPos getPos() {
			return container.getPos();
		}
	}

}
