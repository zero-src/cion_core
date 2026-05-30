package cion.core.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Global registry of all {@link ConfigManager} instances created in this JVM.
 *
 * Each manager registers itself on construction, so any mod using
 * {@code new ConfigManager<>(...)} is automatically discoverable by tools
 * like cion_menu without explicit wiring.
 */
public final class ConfigRegistry {
	private static final List<ConfigManager<?>> MANAGERS = new ArrayList<>();

	private ConfigRegistry() {}

	static synchronized void register(ConfigManager<?> manager) {
		for (ConfigManager<?> existing : MANAGERS) {
			if (existing.modId().equals(manager.modId())) {
				return;
			}
		}
		MANAGERS.add(manager);
	}

	public static synchronized List<ConfigManager<?>> all() {
		return List.copyOf(MANAGERS);
	}

	public static synchronized ConfigManager<?> get(String modId) {
		for (ConfigManager<?> m : MANAGERS) {
			if (m.modId().equals(modId)) return m;
		}
		return null;
	}
}
