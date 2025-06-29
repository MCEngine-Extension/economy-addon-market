package io.github.mcengine.extension.addon.currency.market.model;

import java.util.Map;

public class MenuData {
    private final MarketMenuConfig menuConfig;
    private final Map<Integer, MarketItemConfig> items;

    public MenuData(MarketMenuConfig menuConfig, Map<Integer, MarketItemConfig> items) {
        this.menuConfig = menuConfig;
        this.items = items;
    }

    public MarketMenuConfig getMenuConfig() {
        return menuConfig;
    }

    public Map<Integer, MarketItemConfig> getItems() {
        return items;
    }
}
