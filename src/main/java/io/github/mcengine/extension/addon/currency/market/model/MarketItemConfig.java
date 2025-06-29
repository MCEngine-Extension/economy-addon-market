package io.github.mcengine.extension.addon.currency.market.model;

import org.bukkit.Material;

import java.util.List;

public class MarketItemConfig {
    private final String name;
    private final String currency;
    private final double buyPrice;
    private final double sellPrice;
    private final Material itemType;
    private final List<String> lore;

    public MarketItemConfig(String name, String currency, double buyPrice, double sellPrice, Material itemType, List<String> lore) {
        this.name = name;
        this.currency = currency;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.itemType = itemType;
        this.lore = lore;
    }

    public String getName() {
        return name;
    }

    public String getCurrency() {
        return currency;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public Material getItemType() {
        return itemType;
    }

    public List<String> getLore() {
        return lore;
    }
}
