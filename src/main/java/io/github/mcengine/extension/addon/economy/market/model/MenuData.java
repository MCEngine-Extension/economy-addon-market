package io.github.mcengine.extension.addon.economy.market.model;

import java.util.Map;

/**
 * Holds a single market menu's configuration and item mapping.
 * <p>
 * Each menu has:
 * <ul>
 *   <li>A {@link MarketMenuConfig} for metadata (e.g., GUI name).</li>
 *   <li>A slot-indexed map of {@link MarketItemConfig} representing items.</li>
 * </ul>
 */
public class MenuData {

    /** The menu configuration (GUI name, etc.). */
    private final MarketMenuConfig menuConfig;

    /** Map of item slot positions to their respective market item configurations. */
    private final Map<Integer, MarketItemConfig> items;

    /**
     * Constructs a new MenuData object.
     *
     * @param menuConfig The menu configuration object.
     * @param items      A map of slot indexes to market item configurations.
     */
    public MenuData(MarketMenuConfig menuConfig, Map<Integer, MarketItemConfig> items) {
        this.menuConfig = menuConfig;
        this.items = items;
    }

    /**
     * Gets the market menu configuration.
     *
     * @return The {@link MarketMenuConfig} instance.
     */
    public MarketMenuConfig getMenuConfig() {
        return menuConfig;
    }

    /**
     * Gets the item mapping for this menu.
     *
     * @return A map of slot → {@link MarketItemConfig}.
     */
    public Map<Integer, MarketItemConfig> getItems() {
        return items;
    }
}
