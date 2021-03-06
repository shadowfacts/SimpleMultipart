package net.shadowfacts.simplemultipart;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.IdRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.loot.context.LootContextType;
import net.minecraft.world.loot.context.LootContextTypes;
import net.minecraft.world.loot.context.Parameter;
import net.minecraft.world.loot.context.Parameters;
import net.shadowfacts.simplemultipart.container.*;
import net.shadowfacts.simplemultipart.multipart.Multipart;
import net.shadowfacts.simplemultipart.multipart.MultipartState;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author shadowfacts
 */
public class SimpleMultipart implements ModInitializer {

	public static final String MODID = "simplemultipart";

	public static final Registry<Multipart> MULTIPART = createMultipartRegistry();

	public static final Parameter<MultipartState> MULTIPART_STATE_PARAMETER = new Parameter<>(new Identifier(MODID, "multipart_state"));
	public static final LootContextType MULTIPART_LOOT_CONTEXT = createMultipartLootContextType();

	public static final ContainerBlock containerBlock = new ContainerBlock();
	public static final TickableContainerBlock tickableContainerBlock = new TickableContainerBlock();
	public static final BlockEntityType<ContainerBlockEntity> containerBlockEntity = createBlockEntityType("container", ContainerBlockEntity::new);
	public static final BlockEntityType<TickableContainerBlockEntity> tickableContainerBlockEntity = createBlockEntityType("tickable_container", TickableContainerBlockEntity::new);

	private static BiFunction<String, Consumer<LootContextType.Builder>, LootContextType> lootContextRegisterFunction;

	@Override
	public void onInitialize() {
		Registry.register(Registry.BLOCK, new Identifier(MODID, "container"), containerBlock);
		Registry.register(Registry.BLOCK, new Identifier(MODID, "tickable_container"), tickableContainerBlock);

		ContainerEventHandler.register();
	}

	private static Registry<Multipart> createMultipartRegistry() {
		IdRegistry<Multipart> registry = new IdRegistry<>();
		Registry.REGISTRIES.register(new Identifier(MODID, "multipart"), registry);
		return registry;
	}

	private static <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(String name, Supplier<T> supplier) {
		BlockEntityType.Builder<T> builder = BlockEntityType.Builder.create(supplier);
		return Registry.register(Registry.BLOCK_ENTITY, new Identifier(MODID, name), builder.build(null));
	}

	@Deprecated
	public static void setLootContextRegisterFunction(BiFunction<String, Consumer<LootContextType.Builder>, LootContextType> function) {
		lootContextRegisterFunction = function;
	}

	private static LootContextType createMultipartLootContextType() {
		try {
			// Load the function
			Class.forName(LootContextTypes.class.getCanonicalName());
		} catch (ClassNotFoundException e) {}
		return lootContextRegisterFunction.apply(
				MODID + ":multipart",
				builder -> builder.require(MULTIPART_STATE_PARAMETER).require(Parameters.POSITION)
		);
	}

}
