package cion.core.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Generic JSON config loader/saver. One instance per mod.
 *
 * Usage:
 *   ConfigManager<MyConfig> mgr = new ConfigManager<>("my_mod", MyConfig.class, MyConfig::new);
 *   mgr.postLoad(cfg -> { ... fill nullable defaults ... });
 *   mgr.load();
 *   MyConfig cfg = mgr.get();
 *
 * File location: <fabric config dir>/<modId>.json
 *
 * postLoad runs after deserialization and before re-save, so consumer-side
 * defaults written there are persisted to disk.
 */
public final class ConfigManager<T> {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private final String modId;
	private final Class<T> type;
	private final Supplier<T> defaults;
	private final Logger logger;

	private T instance;
	private Consumer<T> postLoad;

	public ConfigManager(String modId, Class<T> type, Supplier<T> defaults) {
		this.modId = modId;
		this.type = type;
		this.defaults = defaults;
		this.logger = LoggerFactory.getLogger(modId + "/config");
		this.instance = defaults.get();
		ConfigRegistry.register(this);
	}

	/** Set a callback that runs after each successful load, before re-save. */
	public ConfigManager<T> postLoad(Consumer<T> cb) {
		this.postLoad = cb;
		return this;
	}

	public T get() {
		return instance;
	}

	public String modId() {
		return modId;
	}

	public Class<T> type() {
		return type;
	}

	/** Replace the live instance. Caller is responsible for calling save() if needed. */
	public void set(T value) {
		if (value == null) throw new IllegalArgumentException("config instance cannot be null");
		this.instance = value;
	}

	/** Deep-clone the current instance via Gson round-trip. */
	public T clone(T value) {
		return GSON.fromJson(GSON.toJson(value), type);
	}

	/** Serialize given instance to canonical JSON (for equality comparison). */
	public String toJson(T value) {
		return GSON.toJson(value);
	}

	public void load() {
		Path path = configPath();
		if (!Files.exists(path)) {
			if (postLoad != null) postLoad.accept(instance);
			save();
			return;
		}
		try {
			String json = Files.readString(path);
			T parsed = GSON.fromJson(json, type);
			if (parsed != null) {
				instance = parsed;
			}
			if (postLoad != null) postLoad.accept(instance);
			save();
		} catch (Exception e) {
			logger.error("Failed to load config, using defaults", e);
		}
	}

	public void save() {
		Path path = configPath();
		try {
			Files.createDirectories(path.getParent());
			Files.writeString(path, GSON.toJson(instance));
		} catch (IOException e) {
			logger.error("Failed to save config", e);
		}
	}

	private Path configPath() {
		return FabricLoader.getInstance().getConfigDir().resolve(modId + ".json");
	}
}
