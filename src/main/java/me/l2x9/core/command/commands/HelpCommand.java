package me.l2x9.core.command.commands;

import me.l2x9.core.command.BaseCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

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
        List<String> list = config.getStringList("HelpMessage");
        String join = String.join("\n", list);
        sendMessage(sender, join);
    }
}
