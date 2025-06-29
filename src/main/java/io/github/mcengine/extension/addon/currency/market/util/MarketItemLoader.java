package io.github.mcengine.extension.addon.currency.market.util;

import io.github.mcengine.api.mcengine.extension.addon.MCEngineAddOnLogger;
import io.github.mcengine.extension.addon.currency.market.model.MarketItemConfig;
import io.github.mcengine.extension.addon.currency.market.model.MarketMenuConfig;
import io.github.mcengine.extension.addon.currency.market.model.MenuData;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.*;

/**
 * Utility class to load market item and menu configuration files from disk.
 */
public class MarketItemLoader {

    /**
     * Loads all market menus from the plugin's configuration directory.
     *
     * Each folder is treated as a separate market menu, containing a config.yml
     * and multiple item files in YAML format.
     *
     * @param plugin The plugin instance.
     * @param logger Logger for warnings or errors during loading.
     * @return A map of menu keys (folder names) to menu data objects.
     */
    public static Map<String, MenuData> loadAllMarketMenus(Plugin plugin, MCEngineAddOnLogger logger) {
        Map<String, MenuData> allMenus = new HashMap<>();
        File baseDir = new File(plugin.getDataFolder(), "configs/addons/MCEngineMarket");

        if (!baseDir.exists()) {
            logger.warning("Directory not found: " + baseDir.getAbsolutePath());
            return allMenus;
        }

        File[] menuFolders = baseDir.listFiles(File::isDirectory);
        if (menuFolders == null) return allMenus;

        for (File folder : menuFolders) {
            File configFile = new File(folder, "config.yml");
            if (!configFile.exists()) {
                logger.warning("Missing config.yml in: " + folder.getName());
                continue;
            }

            YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
            String guiName = config.getString("name", folder.getName());

            MarketMenuConfig menuConfig = new MarketMenuConfig(guiName);
            Map<Integer, MarketItemConfig> items = new HashMap<>();

            for (File file : Objects.requireNonNull(folder.listFiles())) {
                if (!file.getName().endsWith(".yml") || file.getName().equals("config.yml")) continue;

                YamlConfiguration itemConfig = YamlConfiguration.loadConfiguration(file);
                String name = itemConfig.getString("name", "Unnamed Item");
                int position = itemConfig.getInt("position", -1);
                if (position < 0 || position > 53) {
                    logger.warning("Invalid or missing 'position' in file: " + file.getName());
                    continue;
                }

                String currency = itemConfig.getString("currency", "coin");
                String type = itemConfig.getString("item.type", "STONE");

                Material material;
                try {
                    material = Material.valueOf(type.toUpperCase(Locale.ROOT));
                } catch (IllegalArgumentException e) {
                    logger.warning("Invalid item type in " + file.getName() + ": " + type);
                    continue;
                }

                double buy = itemConfig.getDouble("item.buy.price", 0.0);
                List<String> buyLore = itemConfig.getStringList("item.buy.lore");

                double sell = itemConfig.getDouble("item.sell.price", 0.0);
                List<String> sellLore = itemConfig.getStringList("item.sell.lore");

                MarketItemConfig marketItem = new MarketItemConfig(name, currency, buy, sell, material, buyLore, sellLore);
                items.put(position, marketItem);
            }

            allMenus.put(folder.getName(), new MenuData(menuConfig, items));
        }

        return allMenus;
    }
}
