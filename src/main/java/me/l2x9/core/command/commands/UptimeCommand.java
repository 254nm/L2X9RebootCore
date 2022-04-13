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
        sendMessage(sender, "&6The server has had &r&c" + Utils.getFormattedInterval(System.currentTimeMillis() - plugin.getStartTime()) + "&r&6 uptime");
    }
}
