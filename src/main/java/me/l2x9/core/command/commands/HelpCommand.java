package me.l2x9.core.command.commands;

import me.l2x9.core.command.BaseCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class HelpCommand extends BaseCommand {
    public HelpCommand() {
        super(
                "help",
                "/help",
                "l2x9core.command.help",
                "Displays a custom help menu");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        for (String line : config.getStringList("HelpMessage")) {
           sendMessage(sender, ChatColor.translateAlternateColorCodes('&', line));
        }
    }
}