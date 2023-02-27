package me.l2x9.core.command;

import lombok.RequiredArgsConstructor;
import me.l2x9.core.L2X9RebootCore;
import me.l2x9.core.command.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CommandHandler implements TabExecutor {
    private final ArrayList<BaseCommand> commands = new ArrayList<>();
    private final L2X9RebootCore plugin;

    public void registerCommands() {
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
        addCommand(new WhoIs());
    }

    private void addCommand(BaseCommand command) {
        commands.add(command);
        plugin.registerCommand(command.getName(), this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        for (BaseCommand command : commands) {
            if (!command.getName().equalsIgnoreCase(cmd.getName())) continue;
            if (sender.hasPermission(command.getPermission())) {
                command.execute(sender, args);
            } else command.sendNoPermission(sender);
            break;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        for (BaseCommand command : commands) {
            if (!command.getName().equalsIgnoreCase(cmd.getName())) continue;
            if (command instanceof BaseTabCommand) {
                BaseTabCommand tabCommand = (BaseTabCommand) command;
                return tabCommand.onTab(args);
            }
            if (command.getSubCommands() != null && args.length == 1) {
                return Arrays.stream(command.getSubCommands()).map(s -> s.split("::")[0]).filter(s -> s.startsWith(args[0].toLowerCase())).collect(Collectors.toList());
            } else if (args.length > 1) {
                return Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(s -> s.toLowerCase().startsWith(args[args.length - 1].toLowerCase())).collect(Collectors.toList());
            } else return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
        }
        return Collections.singletonList("");
    }

    public ArrayList<BaseCommand> getCommands() {
        return commands;
    }
}