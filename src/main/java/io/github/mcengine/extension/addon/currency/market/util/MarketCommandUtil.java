package io.github.mcengine.extension.addon.currency.market.util;

import io.github.mcengine.extension.addon.currency.market.cache.MarketCache;
import io.github.mcengine.extension.addon.currency.market.model.MarketItemConfig;
import io.github.mcengine.extension.addon.currency.market.model.MenuData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;

public class MarketCommandUtil {

    public static void openBuyMenu(Player player, String menuKey) {
        MenuData data = MarketCache.getMenu(menuKey);
        if (data == null) {
            player.sendMessage("§cMarket menu not found: " + menuKey);
            return;
        }

        Inventory gui = Bukkit.createInventory(null, 54, data.getMenuConfig().getGuiName());

        for (Map.Entry<Integer, MarketItemConfig> entry : data.getItems().entrySet()) {
            int slot = entry.getKey();
            MarketItemConfig config = entry.getValue();

            ItemStack item = createMenuItem(
                config.getItemType(),
                "§a" + config.getName(),
                "§7Buy: " + config.getBuyPrice()
            );

            gui.setItem(slot, item);
        }

        player.openInventory(gui);
    }

    public static void openSellMenu(Player player, String menuKey) {
        MenuData data = MarketCache.getMenu(menuKey);
        if (data == null) {
            player.sendMessage("§cMarket menu not found: " + menuKey);
            return;
        }

        Inventory gui = Bukkit.createInventory(null, 54, data.getMenuConfig().getGuiName());

        for (Map.Entry<Integer, MarketItemConfig> entry : data.getItems().entrySet()) {
            int slot = entry.getKey();
            MarketItemConfig config = entry.getValue();

            ItemStack item = createMenuItem(
                config.getItemType(),
                "§c" + config.getName(),
                "§7Sell: " + config.getSellPrice()
            );

            gui.setItem(slot, item);
        }

        player.openInventory(gui);
    }

    private static ItemStack createMenuItem(Material material, String displayName, String lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(displayName);
            meta.setLore(List.of(lore));
            item.setItemMeta(meta);
        }

        return item;
    }
}
