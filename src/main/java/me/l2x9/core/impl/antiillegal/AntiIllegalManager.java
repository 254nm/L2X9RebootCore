package me.l2x9.core.impl.antiillegal;

import lombok.Getter;
import me.l2x9.core.L2X9RebootCore;
import me.l2x9.core.Manager;
import me.l2x9.core.impl.antiillegal.check.Check;
import me.l2x9.core.impl.antiillegal.check.checks.ItemStackCheck;
import me.l2x9.core.impl.antiillegal.listeners.InventoryOpen;
import org.bukkit.configuration.ConfigurationSection;

public class AntiIllegalManager extends Manager {

    @Getter
    private ConfigurationSection config;
    @Getter
    private final Check check;

    public AntiIllegalManager() {
        super("AntiIllegal");
        check = new ItemStackCheck();
    }

    @Override
    public void init(L2X9RebootCore plugin) {
        config = plugin.getModuleConfig(this);
        plugin.registerListener(new InventoryOpen(check));
    }

    @Override
    public void destruct(L2X9RebootCore plugin) {

    }

    @Override
    public void reloadConfig(ConfigurationSection config) {

    }
}
