package io.github.mcengine.extension.addon.currency.market.util;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

/**
 * Utility class for general Market operations like configuration handling.
 */
public class MarketUtil {

    /**
     * Creates the default config.yml for the Market AddOn if it doesn't already exist.
     *
     * @param plugin     The plugin instance.
     * @param folderPath The folder path relative to the plugin's data directory.
     */
    public static void createConfig(Plugin plugin, String folderPath) {
        File configFile = new File(plugin.getDataFolder(), folderPath + "/config.yml");

        if (configFile.exists()) return;

        File configDir = configFile.getParentFile();
        if (!configDir.exists() && !configDir.mkdirs()) {
            System.err.println("Failed to create config directory: " + configDir.getAbsolutePath());
            return;
        }

        YamlConfiguration config = new YamlConfiguration();
        config.options().header("Main configuration for MCEngineMarket AddOn");

        config.set("license", "free");
        config.set("market.defaultCurrency", "coin");
        config.set("market.menu.title", "&6Market");
        config.set("market.menu.rows", 3);

        try {
            config.save(configFile);
            System.out.println("Created default Market config: " + configFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to save Market config: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
