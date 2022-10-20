package me.l2x9.core.command.commands;

import me.l2x9.core.command.BaseCommand;
import me.l2x9.core.command.CommandManager;
import me.l2x9.core.util.Utils;
import org.bukkit.command.CommandSender;

public class BaseCmd extends BaseCommand {

    public BaseCmd() {
        super(
                "aef",
                "/aef reload | version | help",
                "l2x9core.command.aef",
                "Base command of the plugin",
                new String[]{
                        "reload::Reload the config file",
                        "version::Show the version of the plugin",
                        "help::Shows the help for the plugin"
                });
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length > 0) {
            switch (args[0]) {
                case "reload":
                    plugin.reloadConfig();
                    sendMessage(sender, PREFIX + "&aSuccessfully reloaded configuration file");
                    break;
                case "version":
                    sendMessage(sender, PREFIX + "&6Version &r&c" + plugin.getDescription().getVersion());
                    break;
                case "help":
                    sendMessage(sender, PREFIX + "&1---&r " + PREFIX + "&6Help &r&1---");
                    sendMessage(sender, PREFIX);
                    CommandManager.getInstance().getCommandHandler().getCommands().forEach(command -> {
                        String helpMsg = "&1---&r&3&l /" + command.getName() + "&r&6 Help &r&1---";
                        sendMessage(sender, PREFIX + helpMsg);
                        sendMessage(sender, PREFIX + "&3Description: " + command.getDescription());
                        if (command.getSubCommands() != null) {
                            if (command.getSubCommands().length > 0) {
                                sendMessage(sender, PREFIX + helpMsg.replace("Help", "Subcommands"));
                                for (String subCommand : command.getSubCommands()) {
                                    String[] split = subCommand.split("::");
                                    if (split.length > 0) {
                                        sendMessage(sender, PREFIX + "&6 /" + command.getName() + " " + split[0] + " |&r&e " + split[1]);
                                    } else sendMessage(sender, PREFIX + "&6 /" + command.getName() + " " + subCommand);
                                }
                                sendMessage(sender, PREFIX + "&1--------------------");
                            }
                        }
                        sendMessage(sender, PREFIX + "");
                    });
                    break;
                default:
                    Utils.sendMessage(sender, String.format("&cUnknown command&r&3 %s&r", args[0]));
                    break;
            }
        } else {
            sendErrorMessage(sender, getUsage());
        }
    }
}
