package ftp.sh.core;

import ftp.sh.core.util.ConfigCreator;

public abstract class Manager {

    private final String name;

    public Manager(String name) {
        this.name = name;

    }

    public abstract void init(L2X9RebootCore plugin);

    public abstract void destruct(L2X9RebootCore plugin);

    public abstract void reloadConfig(ConfigCreator creator);

    public String getName() {
        return name;
    }
}
