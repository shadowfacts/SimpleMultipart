package net.shadowfacts.simplemultipart.test;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.shadowfacts.simplemultipart.SimpleMultipart;
import net.shadowfacts.simplemultipart.client.SimpleMultipartClient;
import net.shadowfacts.simplemultipart.item.ItemMultipart;

/**
 * @author shadowfacts
 */
public class MultipartTest implements ModInitializer {

	public static final String MODID = "multipart_test";

	public static final RedMultipart red = new RedMultipart();
	public static final GreenMultipart green = new GreenMultipart();

	public static final ItemMultipart redItem = new ItemMultipart(red);
	public static final ItemMultipart greenItem = new ItemMultipart(green);

	@Override
	public void onInitialize() {
		Registry.register(SimpleMultipart.MULTIPART, new Identifier(MODID, "red"), red);
		Registry.register(SimpleMultipart.MULTIPART, new Identifier(MODID, "green"), green);

		Registry.register(Registry.ITEM, new Identifier(MODID, "red"), redItem);
		Registry.register(Registry.ITEM, new Identifier(MODID, "green"), greenItem);

		SimpleMultipartClient.registerModel(red.getDefaultState(), new TestMultipartModel(true));
		SimpleMultipartClient.registerModel(green.getDefaultState(), new TestMultipartModel(false));
	}

}
