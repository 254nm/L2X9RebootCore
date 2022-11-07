package me.l2x9.core.command.commands;

import me.l2x9.core.Localization;
import me.l2x9.core.command.BaseCommand;
import me.l2x9.core.util.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpCommand extends BaseCommand {
    public HelpCommand() {
        super("help", "/help", "l2x9core.command.help", "Displays a custom help menu");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Localization loc = Localization.getLocalization(player.getLocale().toLowerCase());
            loc.getStringList("HelpMessage").forEach(s -> Utils.sendOptionalPrefixMessage(player, s, false));
        } else {
            for (String line : config.getStringList("HelpMessage")) {
                Utils.sendOptionalPrefixMessage(sender, line, false);
            }
        }
    }
}
