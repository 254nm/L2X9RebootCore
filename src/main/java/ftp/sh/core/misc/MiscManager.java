package ftp.sh.core.misc;

import ftp.sh.core.L2X9RebootCore;
import ftp.sh.core.Manager;
import ftp.sh.core.misc.listeners.AutoRestart;
import ftp.sh.core.misc.listeners.MoveListener;
import ftp.sh.core.misc.listeners.OldSchoolKill;
import ftp.sh.core.util.ConfigCreator;

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
