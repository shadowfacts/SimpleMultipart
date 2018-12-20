package net.shadowfacts.simplemultipart;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.IdRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.loot.context.LootContextType;
import net.minecraft.world.loot.context.LootContextTypes;
import net.minecraft.world.loot.context.Parameter;
import net.minecraft.world.loot.context.Parameters;
import net.shadowfacts.simplemultipart.container.MultipartContainerEventHandler;
import net.shadowfacts.simplemultipart.container.MultipartContainerBlock;
import net.shadowfacts.simplemultipart.container.MultipartContainerBlockEntity;
import net.shadowfacts.simplemultipart.multipart.Multipart;
import net.shadowfacts.simplemultipart.multipart.MultipartState;

import java.lang.reflect.Method;
import java.util.function.Consumer;

/**
 * @author shadowfacts
 */
public class SimpleMultipart implements ModInitializer {

	public static final String MODID = "simplemultipart";

	public static final Registry<Multipart> MULTIPART = createMultipartRegistry();

	public static final Parameter<MultipartState> MULTIPART_STATE_PARAMETER = new Parameter<>(new Identifier(MODID, "multipart_state"));
	public static final LootContextType MULTIPART_LOOT_CONTEXT = createMultipartLootContextType();

	public static final MultipartContainerBlock containerBlock = new MultipartContainerBlock();
	public static final BlockEntityType<MultipartContainerBlockEntity> containerBlockEntity = createBlockEntityType();

	@Override
	public void onInitialize() {
		Registry.register(Registry.BLOCK, new Identifier(MODID, "container"), containerBlock);

		MultipartContainerEventHandler.register();
	}

	private static Registry<Multipart> createMultipartRegistry() {
		IdRegistry<Multipart> registry = new IdRegistry<>();
		Registry.REGISTRIES.register(new Identifier(MODID, "multipart"), registry);
		return registry;
	}

	private static BlockEntityType<MultipartContainerBlockEntity> createBlockEntityType() {
		BlockEntityType.Builder<MultipartContainerBlockEntity> builder = BlockEntityType.Builder.create(MultipartContainerBlockEntity::new);
		return Registry.register(Registry.BLOCK_ENTITY, new Identifier(MODID, "container"), builder.method_11034(null));
	}

	private static LootContextType createMultipartLootContextType() {
		try {
			Method register = LootContextTypes.class.getDeclaredMethod("register", String.class, Consumer.class);
			register.setAccessible(true);
			Consumer<LootContextType.Builder> initializer = builder -> {
				builder.require(MULTIPART_STATE_PARAMETER).require(Parameters.POSITION);
			};
			return (LootContextType)register.invoke(null, MODID + ":multipart", initializer);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

}
