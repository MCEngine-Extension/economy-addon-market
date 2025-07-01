package io.github.mcengine.extension.addon.currency.market.util;

import io.github.mcengine.api.core.extension.addon.MCEngineAddOnLogger;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Utility class for generating example market item configuration files for demonstration or testing purposes.
 */
public class MarketItemFileGenerator {

    /**
     * Creates example configuration files in two directories: 'example' and 'example2'.
     * Each directory includes a config.yml and multiple item files (YAML).
     *
     * @param plugin The plugin instance used to resolve the data folder.
     * @param logger Logger used for reporting file creation issues.
     */
    public static void createSimpleFiles(Plugin plugin, MCEngineAddOnLogger logger) {
        File baseDir = new File(plugin.getDataFolder(), "configs/addons/MCEngineMarket");
        if (baseDir.exists()) {
            logger.info("Base config folder already exists, skipping example file generation.");
            return;
        }

        createExampleSet(
            new File(baseDir, "example"),
            new String[][]{
                {"Stone", "STONE", "1", "1", "10", "5"},
                {"Diamond", "DIAMOND", "2", "3", "500", "250"}
            },
            logger
        );

        createExampleSet(
            new File(baseDir, "example2"),
            new String[][]{
                {"Emerald", "EMERALD", "4", "2", "300", "150"},
                {"Iron Ingot", "IRON_INGOT", "5", "8", "160", "80"}
            },
            logger
        );
    }

    /**
     * Creates a set of example market files in a given directory.
     *
     * @param directory Target directory.
     * @param items     Each item is a String array: {name, material, position, amount, buyPrice, sellPrice}.
     * @param logger    Logger used for reporting issues.
     */
    private static void createExampleSet(File directory, String[][] items, MCEngineAddOnLogger logger) {
        if (!directory.exists() && !directory.mkdirs()) {
            logger.warning("Failed to create market config folder at: " + directory.getPath());
            return;
        }

        writeFileIfNotExists(
            new File(directory, "config.yml"),
            "name: " + capitalize(directory.getName()) + "\n",
            logger,
            "config.yml"
        );

        for (int i = 0; i < items.length; i++) {
            String[] item = items[i];
            String fileName = "example" + (i + 1) + ".yml";
            String content = getExampleItemContent(item[0], item[1], Integer.parseInt(item[2]), Integer.parseInt(item[3]), Integer.parseInt(item[4]), Integer.parseInt(item[5]));
            writeFileIfNotExists(new File(directory, fileName), content, logger, fileName);
        }
    }

    /**
     * Writes content to a file if the file does not already exist.
     *
     * @param file    The file to write.
     * @param content The text content to write.
     * @param logger  Logger for error reporting.
     * @param label   Human-readable name for logs.
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
     * Builds the contents of a market item YAML file.
     *
     * @param name      Display name of the item.
     * @param material  Bukkit material name.
     * @param position  Slot position in the GUI.
     * @param amount    Quantity per transaction.
     * @param buyPrice  Buy price for the full amount.
     * @param sellPrice Sell price for the full amount.
     * @return A formatted YAML string.
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

    /**
     * Capitalizes the first letter of a string.
     *
     * @param input The input string.
     * @return Capitalized string.
     */
    private static String capitalize(String input) {
        if (input == null || input.isEmpty()) return input;
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
