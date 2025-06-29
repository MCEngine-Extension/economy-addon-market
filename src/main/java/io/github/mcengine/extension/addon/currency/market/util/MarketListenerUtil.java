package io.github.mcengine.extension.addon.currency.market.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MarketListenerUtil {

    public static boolean titleMatches(String guiTitle, String baseName, boolean isBuy, boolean isSell) {
        if (isBuy && guiTitle.equalsIgnoreCase(baseName + " Buy")) return true;
        if (isSell && guiTitle.equalsIgnoreCase(baseName + " Sell")) return true;
        return false;
    }

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
