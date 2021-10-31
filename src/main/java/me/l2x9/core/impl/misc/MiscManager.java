package me.l2x9.core.impl.misc;

import me.l2x9.core.L2X9RebootCore;
import me.l2x9.core.Manager;
import me.l2x9.core.impl.misc.listeners.*;
import me.l2x9.core.boiler.util.ConfigCreator;

public class MiscManager extends Manager {
    public MiscManager() {
        super("Misc");
    }

    @Override
    public void init(L2X9RebootCore plugin) {
        plugin.registerListener(new OldSchoolKill());
        plugin.registerListener(new MoveListener());
        plugin.registerListener(new JoinMessages());
        new AutoRestart();
    }

    @Override
    public void destruct(L2X9RebootCore plugin) {

    }

    @Override
    public void reloadConfig(ConfigCreator creator) {

    }
}
