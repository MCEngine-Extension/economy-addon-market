package io.github.mcengine.extension.addon.currency.market.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MarketCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }

        if (args.length < 1) {
            player.sendMessage("§cUsage: /market <buy|sell|list>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "buy" -> player.sendMessage("§aOpening the buy menu...");
            case "sell" -> player.sendMessage("§aOpening the sell interface...");
            case "list" -> player.sendMessage("§aListing available market items...");
            default -> player.sendMessage("§cUnknown market action. Use: buy, sell, or list.");
        }

        return true;
    }
}
