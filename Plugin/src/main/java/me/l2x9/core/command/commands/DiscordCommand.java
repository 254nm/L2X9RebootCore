package me.l2x9.core.command.commands;

import me.l2x9.core.command.BaseCommand;
import me.l2x9.core.util.Utils;
import org.bukkit.command.CommandSender;

public class DiscordCommand extends BaseCommand {

    public DiscordCommand() {
        super(
                "discord",
                "/discord",
                "l2x9core.command.discord",
                "Shows a discord link");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Utils.sendMessage(sender, config.getString("Discord"));
    }
}
