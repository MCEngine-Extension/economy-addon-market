package io.github.mcengine.extension.addon.currency.market;

import io.github.mcengine.api.currency.extension.addon.IMCEngineCurrencyAddOn;
import io.github.mcengine.api.mcengine.MCEngineApi;
import io.github.mcengine.api.mcengine.extension.addon.MCEngineAddOnLogger;
import org.bukkit.plugin.Plugin;

public class Market implements IMCEngineCurrencyAddOn {

    @Override
    public void onLoad(Plugin plugin) {
        MCEngineAddOnLogger logger = new MCEngineAddOnLogger(plugin, "MCEngineMarket");

        MCEngineApi.checkUpdate(plugin, logger.getLogger(),
        "[AddOn] [MCEngineMarket] ", "github", "MCEngine-Extension",
        "currency-addon-market", plugin.getConfig().getString("github.token", "null"));
    }
}
