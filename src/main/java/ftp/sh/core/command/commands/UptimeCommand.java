package ftp.sh.core.command.commands;


import ftp.sh.core.command.BaseCommand;
import ftp.sh.core.util.Utils;
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
