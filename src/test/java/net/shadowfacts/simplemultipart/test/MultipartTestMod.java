package net.shadowfacts.simplemultipart.test;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.shadowfacts.simplemultipart.SimpleMultipart;
import net.shadowfacts.simplemultipart.item.ItemMultipart;
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

	public static final ItemMultipart testItem = new ItemMultipart(testPart);
	public static final ItemMultipart ironSlabItem = new ItemMultipart(ironSlab);
	public static final ItemMultipart goldSlabItem  = new ItemMultipart(goldSlab);
	public static final ItemMultipart entityTestItem = new ItemMultipart(entityTest);

	@Override
	public void onInitialize() {
		registerPartAndItem("test_part", testPart, testItem);
		registerPartAndItem("iron_slab", ironSlab, ironSlabItem);
		registerPartAndItem("gold_slab", goldSlab, goldSlabItem);
		registerPartAndItem("entity_test", entityTest, entityTestItem);
	}

	private void registerPartAndItem(String name, Multipart part, Item item) {
		Identifier id = new Identifier(MODID, name);
		Registry.register(SimpleMultipart.MULTIPART, id, part);
		Registry.register(Registry.ITEM, id, item);
	}

}
