package net.shadowfacts.simplemultipart.container;

import net.fabricmc.fabric.events.PlayerInteractionEvent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.shadowfacts.simplemultipart.util.MultipartHitResult;
import net.shadowfacts.simplemultipart.SimpleMultipart;
import net.shadowfacts.simplemultipart.util.MultipartHelper;

/**
 * @author shadowfacts
 */
public class MultipartContainerEventHandler {

	public static void register() {
		PlayerInteractionEvent.ATTACK_BLOCK.register(MultipartContainerEventHandler::handleBlockAttack);
	}

	private static ActionResult handleBlockAttack(PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction) {
		if (world.isRemote || world.getBlockState(pos).getBlock() != SimpleMultipart.containerBlock) {
			return ActionResult.PASS;
		}

		MultipartContainerBlockEntity container = (MultipartContainerBlockEntity)world.getBlockEntity(pos);
		if (container == null) {
			return ActionResult.FAILURE;
		}

		MultipartHitResult hit = MultipartHelper.rayTrace(container, world, pos, player);
		if (hit == null) {
			return ActionResult.FAILURE;
		}

		boolean success = container.breakPart(hit.partSlot);
		return success ? ActionResult.SUCCESS : ActionResult.FAILURE;
	}

}
