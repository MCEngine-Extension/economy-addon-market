package io.github.mcengine.extension.addon.currency.market.listener;

import io.github.mcengine.api.core.extension.logger.MCEngineExtensionLogger;
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
 * <p>
 * Handles balance checks, inventory checks, item transactions, and feedback to the player
 * for both buy and sell menus.
 */
public class MarketListener implements Listener {

    /** The plugin instance used for scheduling tasks and accessing configuration. */
    private final Plugin plugin;

    /** Logger for reporting warnings and debug messages. */
    private final MCEngineExtensionLogger logger;

    /** API to access and manipulate currency balances. */
    private final MCEngineCurrencyCommon currencyApi;

    /**
     * Constructs a new MarketListener.
     *
     * @param plugin The plugin instance.
     * @param logger The logger instance.
     */
    public MarketListener(Plugin plugin, MCEngineExtensionLogger logger) {
        this.plugin = plugin;
        this.logger = logger;
        this.currencyApi = MCEngineCurrencyCommon.getApi();
    }

    /**
     * Handles clicks on custom market GUI items.
     * <p>
     * Cancels inventory interaction if the click occurs inside a market buy/sell GUI.
     * If the clicked item corresponds to a configured market item, it will attempt to buy/sell.
     *
     * @param event The InventoryClickEvent.
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
                int amount = config.getAmount();

                if (isBuy) {
                    double balance = currencyApi.getCoin(uuid, currency);
                    if (balance < price) {
                        Bukkit.getScheduler().runTask(plugin, () ->
                            player.sendMessage("§cNot enough " + currency + " to buy §f" + amount + "x " + config.getName() + "§c.")
                        );
                        return;
                    }

                    if (!MarketListenerUtil.hasInventorySpace(player, material, amount)) {
                        Bukkit.getScheduler().runTask(plugin, () ->
                            player.sendMessage("§cYou don't have enough inventory space to buy §f" + amount + "x " + config.getName() + "§c.")
                        );
                        return;
                    }

                    currencyApi.minusCoin(uuid, currency, price);
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        ItemStack stack = new ItemStack(material, amount);
                        player.getInventory().addItem(stack);
                        player.sendMessage("§aYou bought §f" + amount + "x " + config.getName() + "§a for §e" + price + " " + currency + "§a.");
                    });

                } else if (isSell) {
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        boolean removed = MarketListenerUtil.removeItemsFromInventory(player, material, amount);

                        if (!removed) {
                            player.sendMessage("§cYou don't have §f" + amount + "x " + config.getName() + "§c to sell.");
                            return;
                        }

                        currencyApi.addCoin(uuid, currency, price);
                        player.sendMessage("§aYou sold §f" + amount + "x " + config.getName() + "§a for §e" + price + " " + currency + "§a.");
                    });
                }
            });

            break;
        }
    }
}
