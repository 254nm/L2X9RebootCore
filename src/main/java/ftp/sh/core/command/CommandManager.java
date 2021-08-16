package ftp.sh.core.command;

import ftp.sh.core.L2X9RebootCore;
import ftp.sh.core.Manager;
import ftp.sh.core.util.IOUtil;
import ftp.sh.core.util.ConfigCreator;
import org.bukkit.configuration.Configuration;

public class CommandManager extends Manager {
    private static CommandManager instance;
    private CommandHandler commandHandler;
    private Configuration config;

    public CommandManager() {
        super("Commands");
    }

    public static CommandManager getInstance() {
        return instance;
    }

    public static void setInstance(CommandManager instance) {
        CommandManager.instance = instance;
    }

    @Override
    public void init(L2X9RebootCore plugin) {
        instance = this;
        commandHandler = new CommandHandler(L2X9RebootCore.getInstance(), this);
        ConfigCreator.ConfigurationWrapper wrapper = IOUtil.createConfig(plugin, getName(), getName() + "-config", "configs/commands.yml");
        config = wrapper.getConfig();
        try {
            commandHandler.registerCommands();
        } catch (NotInPluginYMLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destruct(L2X9RebootCore plugin) {

    }

    @Override
    public void reloadConfig(ConfigCreator creator) {
        config = creator.getConfigs().get(getName() + "-config").getConfig();
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public Configuration getConfig() {
        return config;
    }
}
