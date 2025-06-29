package io.github.mcengine.extension.addon.currency.market.listener;

import io.github.mcengine.api.mcengine.extension.addon.MCEngineAddOnLogger;
import io.github.mcengine.common.currency.MCEngineCurrencyCommon;
import io.github.mcengine.extension.addon.currency.market.cache.MarketCache;
import io.github.mcengine.extension.addon.currency.market.model.MarketItemConfig;
import io.github.mcengine.extension.addon.currency.market.model.MenuData;
import io.github.mcengine.extension.addon.currency.market.util.MarketListenerUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.UUID;

/**
 * Listener class that handles GUI interactions for buying and selling market items.
 */
public class MarketListener implements Listener {

    /** The plugin instance for scheduling tasks and accessing config. */
    private final Plugin plugin;

    /** Logger utility for sending warnings and debug info. */
    private final MCEngineAddOnLogger logger;

    /** Currency API used to check and update player balances. */
    private final MCEngineCurrencyCommon currencyApi;

    /**
     * Constructs the listener with plugin context and logger.
     *
     * @param plugin The plugin instance.
     * @param logger The logger instance.
     */
    public MarketListener(Plugin plugin, MCEngineAddOnLogger logger) {
        this.plugin = plugin;
        this.logger = logger;
        this.currencyApi = MCEngineCurrencyCommon.getApi();
    }

    /**
     * Event handler for clicking items in the custom market GUI.
     *
     * @param event The inventory click event.
     */
    @EventHandler
    public void onMarketClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        Inventory clickedInv = event.getClickedInventory();
        if (clickedInv == null || event.getView().getTopInventory() != clickedInv) return;

        String guiTitle = ChatColor.stripColor(event.getView().getTitle());
        boolean isBuy = guiTitle.toLowerCase().endsWith("buy");
        boolean isSell = guiTitle.toLowerCase().endsWith("sell");

        for (Map.Entry<String, MenuData> entry : MarketCache.getAllMenus().entrySet()) {
            MenuData menu = entry.getValue();
            String baseName = ChatColor.stripColor(menu.getMenuConfig().getGuiName());

            if (!MarketListenerUtil.titleMatches(guiTitle, baseName, isBuy, isSell)) continue;

            event.setCancelled(true);
            int slot = event.getRawSlot();
            if (slot >= event.getInventory().getSize()) return;

            MarketItemConfig config = menu.getItems().get(slot);
            if (config == null) return;

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                UUID uuid = player.getUniqueId();
                String currency = config.getCurrency();
                double price = isBuy ? config.getBuyPrice() : config.getSellPrice();
                Material material = config.getItemType();

                if (isBuy) {
                    double balance = currencyApi.getCoin(uuid, currency);
                    if (balance < price) {
                        Bukkit.getScheduler().runTask(plugin, () ->
                            player.sendMessage("§cNot enough " + currency + " to buy this item.")
                        );
                        return;
                    }

                    currencyApi.minusCoin(uuid, currency, price);
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        player.getInventory().addItem(new ItemStack(material));
                        player.sendMessage("§aYou bought §f" + config.getName() + "§a for §e" + price + " " + currency + "§a.");
                    });

                } else if (isSell) {
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        boolean removed = MarketListenerUtil.removeOneItemFromInventory(player, material);

                        if (!removed) {
                            player.sendMessage("§cYou don't have any " + config.getName() + " to sell.");
                            return;
                        }

                        currencyApi.addCoin(uuid, currency, price);
                        player.sendMessage("§aYou sold §f" + config.getName() + "§a for §e" + price + " " + currency + "§a.");
                    });
                }
            });

            break;
        }
    }
}
