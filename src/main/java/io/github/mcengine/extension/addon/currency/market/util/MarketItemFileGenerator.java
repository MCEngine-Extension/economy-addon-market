package io.github.mcengine.extension.addon.currency.market.util;

import io.github.mcengine.api.mcengine.extension.addon.MCEngineAddOnLogger;

import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MarketItemFileGenerator {

    public static void createSimpleFiles(Plugin plugin, MCEngineAddOnLogger logger) {
        File exampleDir = new File(plugin.getDataFolder(), "configs/addons/MCEngineMarket/example");

        if (!exampleDir.exists() && !exampleDir.mkdirs()) {
            logger.warning("Failed to create example market config folder.");
            return;
        }

        File configFile = new File(exampleDir, "config.yml");
        File itemFile = new File(exampleDir, "1.yml");

        // Create config.yml
        if (!configFile.exists()) {
            try (FileWriter writer = new FileWriter(configFile)) {
                writer.write("name: Example\n");
            } catch (IOException e) {
                logger.warning("Failed to create config.yml: " + e.getMessage());
            }
        }

        // Create 1.yml item file
        if (!itemFile.exists()) {
            try (FileWriter writer = new FileWriter(itemFile)) {
                writer.write("name: Stone\n");
                writer.write("currency: coin\n");
                writer.write("price:\n");
                writer.write("  buy: 10\n");
                writer.write("  sell: 5\n");
                writer.write("item:\n");
                writer.write("  type: STONE\n");
                writer.write("  lore:\n");
                writer.write("    - \"A solid block.\"\n");
                writer.write("    - \"Useful for building.\"\n");
            } catch (IOException e) {
                logger.warning("Failed to create 1.yml: " + e.getMessage());
            }
        }
    }
}
