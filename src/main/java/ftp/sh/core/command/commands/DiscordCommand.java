package ftp.sh.core.command.commands;

import ftp.sh.core.command.BaseCommand;
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
        sendMessage(sender, config.getString("Discord"));
    }
}
