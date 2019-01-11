package net.shadowfacts.simplemultipart.mixin;

import net.minecraft.world.loot.context.LootContextType;
import net.minecraft.world.loot.context.LootContextTypes;
import net.shadowfacts.simplemultipart.SimpleMultipart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Consumer;

@Mixin(LootContextTypes.class)
public class MixinLootContextTypes {
	static {
		SimpleMultipart.setLootContextRegisterFunction(MixinLootContextTypes::register);
	}

	@Shadow private static LootContextType register(String string_1, Consumer<LootContextType.Builder> consumer_1) {
		return null;
	}
}
