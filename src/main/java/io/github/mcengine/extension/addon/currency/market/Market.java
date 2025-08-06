package io.github.mcengine.extension.addon.currency.market;

import io.github.mcengine.api.currency.extension.addon.IMCEngineCurrencyAddOn;
import io.github.mcengine.api.core.MCEngineCoreApi;
import io.github.mcengine.api.core.extension.logger.MCEngineExtensionLogger;
import io.github.mcengine.extension.addon.currency.market.cache.MarketCache;
import io.github.mcengine.extension.addon.currency.market.command.MarketCommand;
import io.github.mcengine.extension.addon.currency.market.listener.MarketListener;
import io.github.mcengine.extension.addon.currency.market.tabcompleter.MarketTabCompleter;
import io.github.mcengine.extension.addon.currency.market.model.MenuData;
import io.github.mcengine.extension.addon.currency.market.util.MarketCommandUtil;
import io.github.mcengine.extension.addon.currency.market.util.MarketItemFileGenerator;
import io.github.mcengine.extension.addon.currency.market.util.MarketItemLoader;
import io.github.mcengine.extension.addon.currency.market.util.MarketListenerUtil;
import io.github.mcengine.extension.addon.currency.market.util.MarketUtil;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * Main class for the MCEngineMarket add-on.
 * Registers commands, event listeners, and loads market data on plugin load.
 */
public class Market implements IMCEngineCurrencyAddOn {

    /** The path where market configuration is stored. */
    private final String folderPath = "extensions/addons/configs/MCEngineMarket";

    /**
     * Called when the add-on is loaded. Initializes logger, config files, menus, listeners, and command registration.
     *
     * @param plugin The Bukkit plugin instance.
     */
    @Override
    public void onLoad(Plugin plugin) {
        MCEngineExtensionLogger logger = new MCEngineExtensionLogger(plugin, "AddOn", "MCEngineMarket");

        MarketCommandUtil.check(logger);
        MarketListenerUtil.check(logger);

        // Create config.yml if not present
        MarketUtil.createConfig(plugin, folderPath);

        // License check
        File configFile = new File(plugin.getDataFolder(), folderPath + "/config.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        String licenseType = config.getString("license", "free");

        if (!"free".equalsIgnoreCase(licenseType)) {
            logger.warning("License is not 'free'. Disabling Market AddOn.");
            return;
        }

        // Create example config files
        MarketItemFileGenerator.createSimpleFiles(plugin, folderPath, logger);

        // Load all menu data from configuration
        Map<String, MenuData> menus = MarketItemLoader.loadAllMarketMenus(plugin, folderPath, logger);
        MarketCache.setMenus(menus);

        try {
            // Register listener
            PluginManager pluginManager = Bukkit.getPluginManager();
            pluginManager.registerEvents(new MarketListener(plugin, logger), plugin);

            // Access command map using reflection
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());

            // Register /market command with handler and tab completer
            Command marketCommand = new Command("market") {

                /** Handler for command execution. */
                private final MarketCommand handler = new MarketCommand();

                /** Tab completer for market command. */
                private final MarketTabCompleter completer = new MarketTabCompleter();

                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    return handler.onCommand(sender, this, label, args);
                }

                @Override
                public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
                    return completer.onTabComplete(sender, this, alias, args);
                }
            };

            marketCommand.setDescription("Access the market system.");
            marketCommand.setUsage("/market <buy|sell|list>");

            commandMap.register(plugin.getName().toLowerCase(), marketCommand);

            logger.info("Market command registered.");
        } catch (Exception e) {
            logger.warning("Failed to initialize Market AddOn: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Sets the unique ID for the Market AddOn.
     *
     * @param id The identifier string.
     */
    @Override
    public void setId(String id) {
        MCEngineCoreApi.setId("mcengine-market");
    }

    /**
     * Called when the add-on is unloaded.
     *
     * @param plugin The Bukkit plugin instance.
     */
    @Override
    public void onDisload(Plugin plugin) {}
}
