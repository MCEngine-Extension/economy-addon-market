package io.github.mcengine.extension.addon.currency.market.util;

import io.github.mcengine.api.mcengine.extension.addon.MCEngineAddOnLogger;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Utility class to generate example market item configuration files for demonstration or testing.
 */
public class MarketItemFileGenerator {

    /**
     * Creates example config and item YAML files for the market plugin, if they do not already exist.
     * <p>
     * Files created:
     * - config.yml: defines the menu name.
     * - example.yml: example item (Stone).
     * - example2.yml: example item (Diamond).
     *
     * @param plugin The plugin instance used to resolve the data folder.
     * @param logger Logger for logging file creation issues.
     */
    public static void createSimpleFiles(Plugin plugin, MCEngineAddOnLogger logger) {
        File exampleDir = new File(plugin.getDataFolder(), "configs/addons/MCEngineMarket/example");

        if (!exampleDir.exists() && !exampleDir.mkdirs()) {
            logger.warning("Failed to create example market config folder.");
            return;
        }

        // Create config.yml with GUI name
        writeFileIfNotExists(
            new File(exampleDir, "config.yml"),
            "name: Example\n",
            logger,
            "config.yml"
        );

        // Create example item: Stone
        writeFileIfNotExists(
            new File(exampleDir, "example.yml"),
            getExampleItemContent("Stone", "STONE", 1, 1, 10, 5),
            logger,
            "example.yml"
        );

        // Create example item: Diamond
        writeFileIfNotExists(
            new File(exampleDir, "example2.yml"),
            getExampleItemContent("Diamond", "DIAMOND", 2, 3, 500, 250),
            logger,
            "example2.yml"
        );
    }

    /**
     * Writes content to a file only if it does not already exist.
     *
     * @param file    The file to write to.
     * @param content The content to write into the file.
     * @param logger  Logger used to log errors.
     * @param label   The name used for logging purposes.
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
     * Generates a string representing the contents of a market item YAML file.
     *
     * @param name      The display name of the item.
     * @param material  The Bukkit material type of the item.
     * @param position  The GUI slot position of the item.
     * @param amount    The number of items in each transaction.
     * @param buyPrice  The price to buy a full amount of the item.
     * @param sellPrice The price to sell a full amount of the item.
     * @return A formatted YAML string representing the item.
     */
    private static String getExampleItemContent(String name, String material, int position, int amount, int buyPrice, int sellPrice) {
        return "name: " + name + "\n" +
               "position: " + position + "\n" +
               "currency: coin\n" +
               "item:\n" +
               "  type: " + material + "\n" +
               "  amount: " + amount + "\n" +
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
