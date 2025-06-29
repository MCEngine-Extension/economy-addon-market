package io.github.mcengine.extension.addon.currency.market.tabcompleter;

import io.github.mcengine.extension.addon.currency.market.cache.MarketCache;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MarketTabCompleter implements TabCompleter {

    private final List<String> subCommands = List.of("buy", "sell", "list");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>();
            for (String sub : subCommands) {
                if (sub.startsWith(args[0].toLowerCase())) {
                    suggestions.add(sub);
                }
            }
            return suggestions;
        }

        if (args.length == 2 && (args[0].equalsIgnoreCase("buy") || args[0].equalsIgnoreCase("sell"))) {
            String partial = args[1].toLowerCase();
            List<String> menuKeys = new ArrayList<>();
            for (String key : MarketCache.getAllMenus().keySet()) {
                if (key.toLowerCase().startsWith(partial)) {
                    menuKeys.add(key);
                }
            }
            return menuKeys;
        }

        return Collections.emptyList();
    }
}
