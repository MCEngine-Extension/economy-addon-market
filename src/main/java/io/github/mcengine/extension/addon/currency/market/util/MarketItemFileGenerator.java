package io.github.mcengine.extension.addon.currency.market.util;

import io.github.mcengine.api.mcengine.extension.addon.MCEngineAddOnLogger;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Utility class to generate example market item configuration files.
 */
public class MarketItemFileGenerator {

    /**
     * Creates example config and item files for the market plugin.
     *
     * @param plugin The plugin instance used to resolve the data folder.
     * @param logger Logger for logging file creation errors.
     */
    public static void createSimpleFiles(Plugin plugin, MCEngineAddOnLogger logger) {
        File exampleDir = new File(plugin.getDataFolder(), "configs/addons/MCEngineMarket/example");

        if (!exampleDir.exists() && !exampleDir.mkdirs()) {
            logger.warning("Failed to create example market config folder.");
            return;
        }

        // Create main config.yml
        writeFileIfNotExists(
            new File(exampleDir, "config.yml"),
            "name: Example\n",
            logger,
            "config.yml"
        );

        // Create example.yml (stone item)
        writeFileIfNotExists(
            new File(exampleDir, "example.yml"),
            getExampleItemContent("Stone", "STONE", 1, 10, 5),
            logger,
            "example.yml"
        );

        // Create example2.yml (diamond item)
        writeFileIfNotExists(
            new File(exampleDir, "example2.yml"),
            getExampleItemContent("Diamond", "DIAMOND", 2, 500, 250),
            logger,
            "example2.yml"
        );
    }

    /**
     * Writes a file only if it does not already exist.
     *
     * @param file     File to write.
     * @param content  File content to write.
     * @param logger   Logger for error reporting.
     * @param label    Name used for logging identification.
     */
    private static void writeFileIfNotExists(File file, String content, MCEngineAddOnLogger logger, String label) {
        if (file.exists()) return;

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        } catch (IOException e) {
            logger.warning("Failed to create " + label + ": " + e.getMessage());
        }
    }

    /**
     * Generates YAML-formatted market item content.
     *
     * @param name       Display name of the item.
     * @param material   Bukkit material type.
     * @param position   Slot position in GUI.
     * @param buyPrice   Buy price.
     * @param sellPrice  Sell price.
     * @return YAML content string.
     */
    private static String getExampleItemContent(String name, String material, int position, int buyPrice, int sellPrice) {
        return "name: " + name + "\n" +
               "position: " + position + "\n" +
               "currency: coin\n" +
               "item:\n" +
               "  type: " + material + "\n" +
               "  buy:\n" +
               "    price: " + buyPrice + "\n" +
               "    lore:\n" +
               "      - \"Buy this valuable item.\"\n" +
               "      - \"A great addition to your resources.\"\n" +
               "  sell:\n" +
               "    price: " + sellPrice + "\n" +
               "    lore:\n" +
               "      - \"Sell this item for profit.\"\n" +
               "      - \"Always in demand.\"\n";
    }
}
