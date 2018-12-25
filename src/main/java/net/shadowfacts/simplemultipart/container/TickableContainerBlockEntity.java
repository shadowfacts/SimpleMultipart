package net.shadowfacts.simplemultipart.container;

import net.minecraft.util.Tickable;
import net.shadowfacts.simplemultipart.SimpleMultipart;

/**
 * @author shadowfacts
 */
public class TickableContainerBlockEntity extends AbstractContainerBlockEntity implements Tickable {

	public TickableContainerBlockEntity() {
		super(SimpleMultipart.tickableContainerBlockEntity);
	}

	@Override
	public void tick() {
		for (Entry e : parts) {
			if (e.getEntity() != null && e.getEntity() instanceof Tickable) {
				((Tickable)e.getEntity()).tick();
			}
		}
	}

}
