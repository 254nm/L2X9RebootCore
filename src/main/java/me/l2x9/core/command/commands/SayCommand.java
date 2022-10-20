package me.l2x9.core.command.commands;

import me.l2x9.core.command.BaseCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class SayCommand extends BaseCommand {

    public SayCommand() {
        super("say", "/say <message>", "l2x9core.command.say", "Configurable say command");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length > 0) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', String.format("&3L2&r&aX9&r&7 >> &3&l%s", String.join(" ", args))));
        } else sendErrorMessage(sender, "Message cannot be blank");
    }
}