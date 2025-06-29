package io.github.mcengine.extension.addon.currency.market;

import io.github.mcengine.api.currency.extension.addon.IMCEngineCurrencyAddOn;
import io.github.mcengine.api.mcengine.MCEngineApi;
import io.github.mcengine.api.mcengine.extension.addon.MCEngineAddOnLogger;
import io.github.mcengine.extension.addon.currency.market.cache.MarketCache;
import io.github.mcengine.extension.addon.currency.market.command.MarketCommand;
import io.github.mcengine.extension.addon.currency.market.listener.MarketListener;
import io.github.mcengine.extension.addon.currency.market.tabcompleter.MarketTabCompleter;
import io.github.mcengine.extension.addon.currency.market.model.MenuData;
import io.github.mcengine.extension.addon.currency.market.util.MarketItemFileGenerator;
import io.github.mcengine.extension.addon.currency.market.util.MarketItemLoader;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class Market implements IMCEngineCurrencyAddOn {

    @Override
    public void onLoad(Plugin plugin) {
        MCEngineAddOnLogger logger = new MCEngineAddOnLogger(plugin, "MCEngineMarket");
        MarketItemFileGenerator.createSimpleFiles(plugin, logger);

        Map<String, MenuData> menus = MarketItemLoader.loadAllMarketMenus(plugin, logger);
        MarketCache.setMenus(menus);

        try {
            PluginManager pluginManager = Bukkit.getPluginManager();
            pluginManager.registerEvents(new MarketListener(plugin, logger), plugin);

            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());

            Command marketCommand = new Command("market") {
                private final MarketCommand handler = new MarketCommand();
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

        MCEngineApi.checkUpdate(plugin, logger.getLogger(),
            "[AddOn] [MCEngineMarket] ", "github", "MCEngine-Extension",
            "currency-addon-market", plugin.getConfig().getString("github.token", "null"));
    }
}
