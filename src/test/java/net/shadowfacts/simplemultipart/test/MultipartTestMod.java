package net.shadowfacts.simplemultipart.test;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.shadowfacts.simplemultipart.SimpleMultipart;
import net.shadowfacts.simplemultipart.item.MultipartItem;
import net.shadowfacts.simplemultipart.multipart.Multipart;

/**
 * @author shadowfacts
 */
public class MultipartTestMod implements ModInitializer {

	public static final String MODID = "multipart_test";

	public static final TestMultipart testPart = new TestMultipart();
	public static final SlabMultipart ironSlab = new SlabMultipart();
	public static final SlabMultipart goldSlab = new SlabMultipart();
	public static final EntityTestPart entityTest = new EntityTestPart();
	public static final TickableEntityTestPart tickableEntityTest = new TickableEntityTestPart();

	public static final MultipartItem testItem = new MultipartItem(testPart);
	public static final MultipartItem ironSlabItem = new MultipartItem(ironSlab);
	public static final MultipartItem goldSlabItem  = new MultipartItem(goldSlab);
	public static final MultipartItem entityTestItem = new MultipartItem(entityTest);
	public static final MultipartItem tickableEntityTestItem = new MultipartItem(tickableEntityTest);

	@Override
	public void onInitialize() {
		registerPartAndItem("test_part", testPart, testItem);
		registerPartAndItem("iron_slab", ironSlab, ironSlabItem);
		registerPartAndItem("gold_slab", goldSlab, goldSlabItem);
		registerPartAndItem("entity_test", entityTest, entityTestItem);
		registerPartAndItem("tickable_entity_test", tickableEntityTest, tickableEntityTestItem);
	}

	private void registerPartAndItem(String name, Multipart part, Item item) {
		Identifier id = new Identifier(MODID, name);
		Registry.register(SimpleMultipart.MULTIPART, id, part);
		Registry.register(Registry.ITEM, id, item);
	}

}
