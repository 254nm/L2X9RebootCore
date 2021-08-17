package me.l2x9.core.misc;

import me.l2x9.core.L2X9RebootCore;
import me.l2x9.core.Manager;
import me.l2x9.core.misc.listeners.AutoRestart;
import me.l2x9.core.misc.listeners.MoveListener;
import me.l2x9.core.misc.listeners.OldSchoolKill;
import me.l2x9.core.util.ConfigCreator;

public class MiscManager extends Manager {
    public MiscManager() {
        super("Misc");
    }

    @Override
    public void init(L2X9RebootCore plugin) {
        plugin.registerListener(new OldSchoolKill());
        plugin.registerListener(new MoveListener());
        new AutoRestart();
    }

    @Override
    public void destruct(L2X9RebootCore plugin) {

    }

    @Override
    public void reloadConfig(ConfigCreator creator) {

    }
}
