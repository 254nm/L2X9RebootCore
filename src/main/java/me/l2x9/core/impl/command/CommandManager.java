package me.l2x9.core.impl.command;

import me.l2x9.core.L2X9RebootCore;
import me.l2x9.core.Manager;
import org.bukkit.configuration.ConfigurationSection;

public class CommandManager extends Manager {
    private static CommandManager instance;
    private CommandHandler commandHandler;
    private ConfigurationSection config;

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
        config = plugin.getModuleConfig(this);
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
    public void reloadConfig(ConfigurationSection config) {
        this.config = config;
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public ConfigurationSection getConfig() {
        return config;
    }
}
