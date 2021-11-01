package me.l2x9.core.impl.command.commands;

import me.l2x9.core.impl.command.BaseCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class SayCommand extends BaseCommand {

    public SayCommand() {
        super(
                "say",
                "/say <message>",
                "l2x9core.command.say",
                "Configurable say command");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length > 0) {
            String configMessage = config.getString("Prefix");
            StringBuilder builder = new StringBuilder();
            for (String arg : args) {
                builder.append(arg.concat(" "));
            }
            Bukkit.getServer().broadcastMessage(
                    ChatColor.translateAlternateColorCodes(
                            '&',
                            configMessage.replace("{message}", builder.toString())));
        } else {
            sendErrorMessage(sender, "Message cannot be blank");
        }
    }
}