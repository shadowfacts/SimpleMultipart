package net.shadowfacts.simplemultipart.test;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.shadowfacts.simplemultipart.SimpleMultipart;
import net.shadowfacts.simplemultipart.item.ItemMultipart;

/**
 * @author shadowfacts
 */
public class MultipartTestMod implements ModInitializer {

	public static final String MODID = "multipart_test";

	public static final TestMultipart testPart = new TestMultipart();
	public static final ItemMultipart testItem = new ItemMultipart(testPart);

	@Override
	public void onInitialize() {
		Registry.register(SimpleMultipart.MULTIPART, new Identifier(MODID, "test_part"), testPart);
		Registry.register(Registry.ITEM, new Identifier(MODID, "test_part"), testItem);
	}

}
