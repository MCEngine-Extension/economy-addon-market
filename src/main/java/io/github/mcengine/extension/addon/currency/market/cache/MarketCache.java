package io.github.mcengine.extension.addon.currency.market.cache;

import io.github.mcengine.extension.addon.currency.market.model.MenuData;

import java.util.HashMap;
import java.util.Map;

public class MarketCache {

    private static final Map<String, MenuData> menuCache = new HashMap<>();

    public static void setMenus(Map<String, MenuData> data) {
        menuCache.clear();
        menuCache.putAll(data);
    }

    public static MenuData getMenu(String key) {
        return menuCache.get(key);
    }

    public static Map<String, MenuData> getAllMenus() {
        return menuCache;
    }

    public static boolean isLoaded() {
        return !menuCache.isEmpty();
    }
}
