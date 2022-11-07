package me.l2x9.core.chat.commands;

import me.l2x9.core.chat.ChatInfo;
import me.l2x9.core.chat.ChatManager;
import me.l2x9.core.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IgnoreCommand implements CommandExecutor {
    private final ChatManager manager;

    public IgnoreCommand(ChatManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                ChatInfo info = manager.getInfo((Player) sender);
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                if (!info.isIgnoring(target.getUniqueId())) {
                    info.ignorePlayer(target.getUniqueId());
                    Utils.sendPrefixedLocalizedMessage(player, "ignore_successful", target.getName());
                } else Utils.sendPrefixedLocalizedMessage(player, "already_ignoring");
            } else Utils.sendPrefixedLocalizedMessage(player,"ignore_command_syntax");
        } else Utils.sendMessage(sender, "&cYou must be a player");
        return true;
    }
}
