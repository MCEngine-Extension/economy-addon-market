package io.github.mcengine.extension.addon.currency.market.util;

import io.github.mcengine.api.core.extension.logger.MCEngineExtensionLogger;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Utility methods for handling GUI title checks and inventory item operations in the market system.
 */
public class MarketListenerUtil {

    /**
     * Determines if the GUI title matches a buy or sell menu.
     *
     * @param guiTitle The actual GUI title.
     * @param baseName The base menu name (from config).
     * @param isBuy    Whether the action is a buy check.
     * @param isSell   Whether the action is a sell check.
     * @return True if the title matches the expected Buy/Sell suffix.
     */
    public static boolean titleMatches(String guiTitle, String baseName, boolean isBuy, boolean isSell) {
        if (isBuy && guiTitle.equalsIgnoreCase(baseName + " Buy")) return true;
        if (isSell && guiTitle.equalsIgnoreCase(baseName + " Sell")) return true;
        return false;
    }

    /**
     * Checks whether the player's inventory has enough room to receive the specified number of items.
     *
     * @param player   The player to check.
     * @param material The material to simulate adding.
     * @param amount   The number of items to add.
     * @return True if there is enough space, false otherwise.
     */
    public static boolean hasInventorySpace(Player player, Material material, int amount) {
        int remaining = amount;
        ItemStack[] contents = player.getInventory().getStorageContents();

        for (ItemStack stack : contents) {
            if (stack == null) {
                remaining -= Math.min(remaining, material.getMaxStackSize());
            } else if (stack.getType() == material && stack.getAmount() < stack.getMaxStackSize()) {
                int space = stack.getMaxStackSize() - stack.getAmount();
                remaining -= Math.min(remaining, space);
            }
            if (remaining <= 0) return true;
        }

        return false;
    }

    /**
     * Removes one item of the specified material from the player's inventory, if available.
     *
     * @param player   The player whose inventory to modify.
     * @param material The item type to search for and remove.
     * @return True if an item was removed; false otherwise.
     */
    public static boolean removeOneItemFromInventory(Player player, Material material) {
        return removeItemsFromInventory(player, material, 1);
    }

    /**
     * Removes a specific number of items of the given material from the player's inventory.
     *
     * @param player   The player whose inventory to modify.
     * @param material The item type to remove.
     * @param amount   The number of items to remove.
     * @return True if enough items were removed, false otherwise.
     */
    public static boolean removeItemsFromInventory(Player player, Material material, int amount) {
        int remaining = amount;
        ItemStack[] contents = player.getInventory().getContents();

        for (int i = 0; i < contents.length; i++) {
            ItemStack stack = contents[i];
            if (stack != null && stack.getType() == material) {
                int stackAmount = stack.getAmount();
                if (stackAmount >= remaining) {
                    stack.setAmount(stackAmount - remaining);
                    if (stack.getAmount() <= 0) {
                        player.getInventory().setItem(i, null);
                    }
                    return true;
                } else {
                    remaining -= stackAmount;
                    player.getInventory().setItem(i, null);
                }
            }
        }

        return false;
    }
}
