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
            if (args.length == 1) {
                ChatInfo info = manager.getInfo((Player) sender);
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                if (!info.isIgnoring(target.getUniqueId())) {
                    info.ignorePlayer(target.getUniqueId());
                    Utils.sendPrefixMessage(sender, "&3Successfully ignored player&r&a " + target.getName());
                } else Utils.sendPrefixMessage(sender, "&cYou are already ignoring that player");
            } else Utils.sendPrefixMessage(sender, "&cPlease include a player /ignore <playerName>");
        } else Utils.sendPrefixMessage(sender, "&cYou must be a player");
        return true;
    }
}
