package me.l2x9.core.command;

import lombok.Getter;
import me.l2x9.core.L2X9RebootCore;
import me.l2x9.core.util.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Optional;

@Getter
public abstract class BaseCommand {
    public final String CONSOLE_ONLY = "This command is console only";
    public final String PLAYER_ONLY = "This command is player only";
    public final String PREFIX = Utils.getPrefix();
    public final ConfigurationSection config = CommandManager.getInstance().getConfig();
    public final L2X9RebootCore plugin = L2X9RebootCore.getInstance();
    private final String name;
    private final String usage;
    private final String permission;
    private final String description;
    private final String[] subCommands;

    public BaseCommand(String name, String usage, String permission) {
        this(name, usage, permission, null, null);
    }

    public BaseCommand(String name, String usage, String permission, String description) {
        this(name, usage, permission, description, null);
    }

    public BaseCommand(String name, String usage, String permission, String description, String[] subCommands) {
        this.name = name;
        this.usage = usage;
        this.permission = permission;
        this.description = description;
        this.subCommands = subCommands;
    }

    public void sendNoPermission(CommandSender sender) {
        Utils.sendMessage(sender, "&cYou are lacking the permission&r&a %s", getPermission());
    }

    public void sendErrorMessage(CommandSender sender, String message) {
        Utils.sendMessage(sender, String.format("&c%s", message));
    }

    public void sendMessage(CommandSender sender, String format, Object... args) {
        Utils.sendMessage(sender, format, args);
    }

    public Optional<Player> getSenderAsPlayer(CommandSender sender) {
        if (sender instanceof Player) {
            return Optional.of((Player) sender);
        } else return Optional.empty();
    }

    public abstract void execute(CommandSender sender, String[] args);
}