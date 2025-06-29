package io.github.mcengine.extension.addon.currency.market.listener;

import io.github.mcengine.common.currency.MCEngineCurrencyCommon;
import io.github.mcengine.api.mcengine.extension.addon.MCEngineAddOnLogger;
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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
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
        if (clickedInv == null) return;

        String guiTitle = ChatColor.stripColor(event.getView().getTitle());

        for (Map.Entry<String, MenuData> entry : MarketCache.getAllMenus().entrySet()) {
            MenuData menuData = entry.getValue();
            if (!guiTitle.equals(ChatColor.stripColor(menuData.getMenuConfig().getGuiName()))) continue;

            event.setCancelled(true); // Prevent taking items from GUI

            int slot = event.getSlot();
            MarketItemConfig config = menuData.getItems().get(slot);
            if (config == null) return;

            boolean isBuy = guiTitle.toLowerCase().contains("buy");
            boolean isSell = guiTitle.toLowerCase().contains("sell");

            new BukkitRunnable() {
                @Override
                public void run() {
                    UUID uuid = player.getUniqueId();
                    String currencyType = config.getCurrency();
                    double price = isBuy ? config.getBuyPrice() : config.getSellPrice();

                    double balance = currencyApi.getCoin(uuid, currencyType);
                    if (isBuy) {
                        if (balance >= price) {
                            currencyApi.minusCoin(uuid, currencyType, price);
                            Bukkit.getScheduler().runTask(plugin, () -> {
                                ItemStack item = new ItemStack(config.getItemType());
                                ItemMeta meta = item.getItemMeta();
                                if (meta != null) {
                                    meta.setDisplayName("§f" + config.getName());
                                    meta.setLore(formatLore(config.getBuyLore()));
                                    item.setItemMeta(meta);
                                }
                                player.getInventory().addItem(item);
                                player.sendMessage("§aYou bought §f" + config.getName() + " §afor §e" + price + " " + currencyType + "§a.");
                            });
                        } else {
                            player.sendMessage("§cYou don't have enough " + currencyType + " to buy this.");
                        }
                    } else if (isSell) {
                        currencyApi.addCoin(uuid, currencyType, price);
                        Bukkit.getScheduler().runTask(plugin, () -> {
                            player.sendMessage("§aYou sold §f" + config.getName() + " §afor §e" + price + " " + currencyType + "§a.");
                        });
                    }
                }
            }.runTaskAsynchronously(plugin);

            break;
        }
    }

    private List<String> formatLore(List<String> raw) {
        return raw.stream().map(line -> "§f" + line).toList();
    }
}
