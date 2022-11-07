package me.l2x9.core.chat.commands;

import me.l2x9.core.util.Utils;
import me.l2x9.core.chat.ChatInfo;
import me.l2x9.core.chat.ChatManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnIgnoreCommand implements CommandExecutor {
    private final ChatManager manager;

    public UnIgnoreCommand(ChatManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                ChatInfo info = manager.getInfo(player);
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                if (info.isIgnoring(target.getUniqueId())) {
                    info.unignorePlayer(target.getUniqueId());
                    Utils.sendPrefixedLocalizedMessage(player, "unignore_successful", target.getName());
                } else Utils.sendPrefixedLocalizedMessage(player, "unignore_not_ignoring", target.getName());
            } else Utils.sendPrefixedLocalizedMessage(player, "unignore_command_syntax");
        } else Utils.sendMessage(sender, "&cYou must be a player");
        return true;
    }
}
