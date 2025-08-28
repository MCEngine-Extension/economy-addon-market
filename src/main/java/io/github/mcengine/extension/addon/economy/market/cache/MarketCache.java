package io.github.mcengine.extension.addon.economy.market.cache;

import io.github.mcengine.extension.addon.economy.market.model.MenuData;

import java.util.HashMap;
import java.util.Map;

/**
 * Global cache for storing loaded market menu data.
 */
public class MarketCache {

    /** Internal cache storing menu data by menu key. */
    private static final Map<String, MenuData> menuCache = new HashMap<>();

    /**
     * Replaces the current menu cache with the given map.
     *
     * @param data The new map of menu data to cache.
     */
    public static void setMenus(Map<String, MenuData> data) {
        menuCache.clear();
        menuCache.putAll(data);
    }

    /**
     * Retrieves a specific market menu by its key.
     *
     * @param key The name of the menu (usually the folder name).
     * @return The associated {@link MenuData}, or null if not found.
     */
    public static MenuData getMenu(String key) {
        return menuCache.get(key);
    }

    /**
     * Retrieves all loaded market menus.
     *
     * @return A map of all cached menu data.
     */
    public static Map<String, MenuData> getAllMenus() {
        return menuCache;
    }

    /**
     * Checks if any menus have been loaded into the cache.
     *
     * @return True if at least one menu is loaded, false otherwise.
     */
    public static boolean isLoaded() {
        return !menuCache.isEmpty();
    }
}
