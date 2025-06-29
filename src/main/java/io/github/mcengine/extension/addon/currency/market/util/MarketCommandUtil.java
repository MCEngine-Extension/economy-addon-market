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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Utility class responsible for building and opening market GUI menus for buy and sell interactions.
 */
public class MarketCommandUtil {

    /**
     * Opens a market buy GUI for the specified player using the given menu key.
     *
     * @param player  The player to open the menu for.
     * @param menuKey The key of the menu to load from the MarketCache.
     */
    public static void openBuyMenu(Player player, String menuKey) {
        MenuData data = MarketCache.getMenu(menuKey);
        if (data == null) {
            player.sendMessage("§cMarket menu not found: " + menuKey);
            return;
        }

        String guiTitle = data.getMenuConfig().getGuiName() + " Buy";
        Inventory gui = Bukkit.createInventory(null, 54, guiTitle);

        for (Map.Entry<Integer, MarketItemConfig> entry : data.getItems().entrySet()) {
            int slot = entry.getKey();
            MarketItemConfig config = entry.getValue();

            ItemStack item = createMenuItem(
                config.getItemType(),
                "§a" + config.getName(),
                formatLore(config.getBuyPrice(), config.getCurrency(), config.getBuyLore(), true, config.getAmount())
            );

            gui.setItem(slot, item);
        }

        player.openInventory(gui);
    }

    /**
     * Opens a market sell GUI for the specified player using the given menu key.
     *
     * @param player  The player to open the menu for.
     * @param menuKey The key of the menu to load from the MarketCache.
     */
    public static void openSellMenu(Player player, String menuKey) {
        MenuData data = MarketCache.getMenu(menuKey);
        if (data == null) {
            player.sendMessage("§cMarket menu not found: " + menuKey);
            return;
        }

        String guiTitle = data.getMenuConfig().getGuiName() + " Sell";
        Inventory gui = Bukkit.createInventory(null, 54, guiTitle);

        for (Map.Entry<Integer, MarketItemConfig> entry : data.getItems().entrySet()) {
            int slot = entry.getKey();
            MarketItemConfig config = entry.getValue();

            ItemStack item = createMenuItem(
                config.getItemType(),
                "§c" + config.getName(),
                formatLore(config.getSellPrice(), config.getCurrency(), config.getSellLore(), false, config.getAmount())
            );

            gui.setItem(slot, item);
        }

        player.openInventory(gui);
    }

    /**
     * Creates a decorated ItemStack with name and lore used for menu display.
     *
     * @param material     The material of the item.
     * @param displayName  The display name to set.
     * @param loreLines    The lore lines to display.
     * @return ItemStack configured for GUI.
     */
    private static ItemStack createMenuItem(Material material, String displayName, List<String> loreLines) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(displayName);
            meta.setLore(loreLines);
            item.setItemMeta(meta);
        }

        return item;
    }

    /**
     * Formats the price and currency line with amount and additional custom lore lines for display in GUI.
     *
     * @param totalPrice The total price for the given amount.
     * @param currency   The currency name.
     * @param baseLore   The custom lore lines.
     * @param isBuy      True if buy mode; false if sell mode.
     * @param amount     Number of items being traded.
     * @return A list of formatted lore lines.
     */
    private static List<String> formatLore(double totalPrice, String currency, List<String> baseLore, boolean isBuy, int amount) {
        List<String> result = new ArrayList<>();
        result.add((isBuy ? "§aBuy" : "§cSell") + ": " + amount + " for " + totalPrice + " " + currency);
        for (String line : baseLore) {
            result.add("§f" + line);
        }
        return result;
    }
}
