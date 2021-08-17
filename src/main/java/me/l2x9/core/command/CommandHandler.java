package me.l2x9.core.command;

import me.l2x9.core.L2X9RebootCore;
import me.l2x9.core.command.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandHandler implements TabExecutor {
    private final ArrayList<BaseCommand> commands = new ArrayList<>();
    private final L2X9RebootCore plugin;
    private final CommandManager manager;

    public CommandHandler(L2X9RebootCore plugin, CommandManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    public void registerCommands() throws NotInPluginYMLException {
        try {
            addCommand(new BaseCmd());
            addCommand(new CrashCommand());
            addCommand(new DiscordCommand());
            addCommand(new HelpCommand());
            addCommand(new OpenInv());
            addCommand(new SayCommand());
            addCommand(new SpawnCommand());
            addCommand(new SpeedCommand());
            addCommand(new UptimeCommand());
            addCommand(new UUidCommand());
            addCommand(new WorldSwitcher());
            addCommand(new TopEntity());
        } catch (Exception e) {
            throw new NotInPluginYMLException("Command not in plugin.yml");
        }
    }

    private void addCommand(BaseCommand command) {
        commands.add(command);
        plugin.getCommand(command.getName()).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        for (BaseCommand command : commands) {
            if (command.getName().equalsIgnoreCase(cmd.getName())) {
                if (sender.hasPermission(command.getPermission())) {
                    command.execute(sender, args);
                } else {
                    command.sendNoPermission(sender);
                }
                break;
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        for (BaseCommand command : commands) {
            if (command.getName().equalsIgnoreCase(cmd.getName())) {
                if (command instanceof BaseTabCommand) {
                    BaseTabCommand tabCommand = (BaseTabCommand) command;
                    return tabCommand.onTab(args);
                } else {
                    List<String> players = new ArrayList<>();
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        players.add(player.getName());
                    }
                    return players;
                }
            }
        }
        return Collections.singletonList("Not a tab command");
    }

    public ArrayList<BaseCommand> getCommands() {
        return commands;
    }
}