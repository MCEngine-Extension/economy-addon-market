package io.github.mcengine.extension.addon.currency.market.model;

import org.bukkit.Material;

import java.util.List;

/**
 * Represents the configuration for a market item, including buy/sell price and lore.
 */
public class MarketItemConfig {

    /** The display name of the item. */
    private final String name;

    /** The type of currency used for transactions. */
    private final String currency;

    /** The buy price of the item. */
    private final double buyPrice;

    /** The sell price of the item. */
    private final double sellPrice;

    /** The Bukkit {@link Material} type of the item. */
    private final Material itemType;

    /** The lore displayed when buying the item. */
    private final List<String> buyLore;

    /** The lore displayed when selling the item. */
    private final List<String> sellLore;

    /**
     * Constructs a new MarketItemConfig object.
     *
     * @param name      Item name
     * @param currency  Currency type
     * @param buyPrice  Buy price
     * @param sellPrice Sell price
     * @param itemType  Bukkit material type
     * @param buyLore   Lore shown when buying
     * @param sellLore  Lore shown when selling
     */
    public MarketItemConfig(String name, String currency, double buyPrice, double sellPrice, Material itemType,
                            List<String> buyLore, List<String> sellLore) {
        this.name = name;
        this.currency = currency;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.itemType = itemType;
        this.buyLore = buyLore;
        this.sellLore = sellLore;
    }

    /**
     * @return The item name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return The currency type used.
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * @return The buy price of the item.
     */
    public double getBuyPrice() {
        return buyPrice;
    }

    /**
     * @return The sell price of the item.
     */
    public double getSellPrice() {
        return sellPrice;
    }

    /**
     * @return The Bukkit {@link Material} type.
     */
    public Material getItemType() {
        return itemType;
    }

    /**
     * @return The lore shown when buying.
     */
    public List<String> getBuyLore() {
        return buyLore;
    }

    /**
     * @return The lore shown when selling.
     */
    public List<String> getSellLore() {
        return sellLore;
    }
}
