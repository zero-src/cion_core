package cion.core.client;

import cion.core.CionCore;
import net.fabricmc.api.ClientModInitializer;

public class CionCoreClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		CionCore.LOGGER.info("cion_core client initialized");
	}
}
