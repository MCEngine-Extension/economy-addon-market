package io.github.mcengine.extension.addon.currency.market.listener;

import io.github.mcengine.api.mcengine.extension.addon.MCEngineAddOnLogger;
import io.github.mcengine.common.currency.MCEngineCurrencyCommon;
import io.github.mcengine.extension.addon.currency.market.cache.MarketCache;
import io.github.mcengine.extension.addon.currency.market.model.MarketItemConfig;
import io.github.mcengine.extension.addon.currency.market.model.MenuData;
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

public class MarketListener implements Listener {

    private final Plugin plugin;
    private final MCEngineAddOnLogger logger;
    private final MCEngineCurrencyCommon currencyApi;

    public MarketListener(Plugin plugin, MCEngineAddOnLogger logger) {
        this.plugin = plugin;
        this.logger = logger;
        this.currencyApi = MCEngineCurrencyCommon.getApi();
    }

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

            if (!(guiTitle.equalsIgnoreCase(baseName + " Buy") || guiTitle.equalsIgnoreCase(baseName + " Sell"))) continue;

            event.setCancelled(true); // Block taking items
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
                        player.getInventory().addItem(new ItemStack(material, 1));
                        player.sendMessage("§aYou bought §f" + config.getName() + "§a for §e" + price + " " + currency + "§a.");
                    });
                } else if (isSell) {
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        // Check for one item to remove
                        ItemStack[] contents = player.getInventory().getContents();
                        boolean found = false;

                        for (int i = 0; i < contents.length; i++) {
                            ItemStack stack = contents[i];
                            if (stack != null && stack.getType() == material && stack.getAmount() >= 1) {
                                stack.setAmount(stack.getAmount() - 1);
                                found = true;
                                break;
                            }
                        }

                        if (!found) {
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
