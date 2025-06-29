package io.github.mcengine.extension.addon.currency.market.command;

import io.github.mcengine.extension.addon.currency.market.cache.MarketCache;
import io.github.mcengine.extension.addon.currency.market.util.MarketCommandUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class MarketCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }

        if (args.length < 1) {
            player.sendMessage("§cUsage: /market <buy|sell|list> [menu]");
            return true;
        }

        String action = args[0].toLowerCase();

        switch (action) {
            case "buy" -> {
                if (args.length < 2) {
                    player.sendMessage("§cUsage: /market buy <menu>");
                    return true;
                }
                MarketCommandUtil.openBuyMenu(player, args[1]);
            }
            case "sell" -> {
                if (args.length < 2) {
                    player.sendMessage("§cUsage: /market sell <menu>");
                    return true;
                }
                MarketCommandUtil.openSellMenu(player, args[1]);
            }
            case "list" -> {
                Set<String> menus = MarketCache.getAllMenus().keySet();
                if (menus.isEmpty()) {
                    player.sendMessage("§cNo market menus available.");
                } else {
                    player.sendMessage("§aAvailable Menus:");
                    for (String menu : menus) {
                        player.sendMessage("§f- §e" + menu);
                    }
                }
            }
            default -> player.sendMessage("§cUnknown market action. Use: buy, sell, or list.");
        }

        return true;
    }
}
