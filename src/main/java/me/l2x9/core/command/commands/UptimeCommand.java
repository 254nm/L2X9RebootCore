package me.l2x9.core.command.commands;


import me.l2x9.core.command.BaseCommand;
import me.l2x9.core.util.Utils;
import org.bukkit.command.CommandSender;

public class UptimeCommand extends BaseCommand {

    public UptimeCommand() {
        super(
                "uptime",
                "/uptime",
                "l2x9core.command.uptime",
                "Show the uptime of the server");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        sendMessage(sender, "&3The server has had&r&a %s&r&3 uptime", Utils.getFormattedInterval(System.currentTimeMillis() - plugin.getStartTime()));
    }
}
