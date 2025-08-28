package io.github.mcengine.extension.addon.economy.market.model;

/**
 * Represents the configuration for a market menu GUI.
 * Contains only metadata such as the displayed title.
 */
public class MarketMenuConfig {

    /** The name/title of the GUI menu. */
    private final String guiName;

    /**
     * Constructs a new MarketMenuConfig.
     *
     * @param guiName The title to be displayed on the menu.
     */
    public MarketMenuConfig(String guiName) {
        this.guiName = guiName;
    }

    /**
     * Gets the GUI name of the market menu.
     *
     * @return The GUI name.
     */
    public String getGuiName() {
        return guiName;
    }
}
