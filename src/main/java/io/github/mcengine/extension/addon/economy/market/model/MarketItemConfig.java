package io.github.mcengine.extension.addon.economy.market.model;

import org.bukkit.Material;

import java.util.List;

/**
 * Represents the configuration for a market item, including:
 * <ul>
 *     <li>Display name</li>
 *     <li>Currency type</li>
 *     <li>Buy/sell pricing (base and total)</li>
 *     <li>Lore shown in GUI menus</li>
 *     <li>Quantity traded per transaction</li>
 * </ul>
 */
public class MarketItemConfig {

    /** The display name of the item. */
    private final String name;

    /** The type of economy currency used for transactions. */
    private final String currency;

    /** The base buy price of a single item. */
    private final double baseBuyPrice;

    /** The base sell price of a single item. */
    private final double baseSellPrice;

    /** The Bukkit {@link Material} type of the item. */
    private final Material itemType;

    /** The lore displayed when buying the item. */
    private final List<String> buyLore;

    /** The lore displayed when selling the item. */
    private final List<String> sellLore;

    /** The quantity of items involved in the transaction. */
    private final int amount;

    /**
     * Constructs a new MarketItemConfig object.
     *
     * @param name          Item name
     * @param currency      Economy currency type
     * @param baseBuyPrice  Price per item when buying
     * @param baseSellPrice Price per item when selling
     * @param itemType      Bukkit material type
     * @param buyLore       Lore shown when buying
     * @param sellLore      Lore shown when selling
     * @param amount        Quantity involved in each transaction
     */
    public MarketItemConfig(String name, String currency, double baseBuyPrice, double baseSellPrice,
                            Material itemType, List<String> buyLore, List<String> sellLore, int amount) {
        this.name = name;
        this.currency = currency;
        this.baseBuyPrice = baseBuyPrice;
        this.baseSellPrice = baseSellPrice;
        this.itemType = itemType;
        this.buyLore = buyLore;
        this.sellLore = sellLore;
        this.amount = amount;
    }

    /** @return The item name. */
    public String getName() {
        return name;
    }

    /** @return The economy currency type used. */
    public String getCurrency() {
        return currency;
    }

    /** @return The total buy price (base price × amount). */
    public double getBuyPrice() {
        return baseBuyPrice * amount;
    }

    /** @return The total sell price (base price × amount). */
    public double getSellPrice() {
        return baseSellPrice * amount;
    }

    /** @return The base price per item when buying. */
    public double getBaseBuyPrice() {
        return baseBuyPrice;
    }

    /** @return The base price per item when selling. */
    public double getBaseSellPrice() {
        return baseSellPrice;
    }

    /** @return The Bukkit {@link Material} type. */
    public Material getItemType() {
        return itemType;
    }

    /** @return The lore shown when buying. */
    public List<String> getBuyLore() {
        return buyLore;
    }

    /** @return The lore shown when selling. */
    public List<String> getSellLore() {
        return sellLore;
    }

    /** @return The number of items in each transaction. */
    public int getAmount() {
        return amount;
    }
}
