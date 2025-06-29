package io.github.mcengine.extension.addon.currency.market.util;

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
     * Removes one item of the specified material from the player's inventory, if available.
     *
     * @param player   The player whose inventory to modify.
     * @param material The item type to search for and remove.
     * @return True if an item was removed; false otherwise.
     */
    public static boolean removeOneItemFromInventory(Player player, Material material) {
        ItemStack[] contents = player.getInventory().getContents();

        for (int i = 0; i < contents.length; i++) {
            ItemStack stack = contents[i];
            if (stack != null && stack.getType() == material && stack.getAmount() >= 1) {
                stack.setAmount(stack.getAmount() - 1);
                if (stack.getAmount() <= 0) {
                    player.getInventory().setItem(i, null);
                }
                return true;
            }
        }

        return false;
    }
}
