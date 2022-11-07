package me.l2x9.core.chat.commands;

import me.l2x9.core.chat.ChatCommand;
import me.l2x9.core.chat.ChatInfo;
import me.l2x9.core.chat.ChatManager;
import me.l2x9.core.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class MessageCommand extends ChatCommand {
    private final ChatManager manager;

    public MessageCommand(ChatManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length >= 2) {
                Player target = Bukkit.getPlayer(args[0]);
                if (Bukkit.getOnlinePlayers().contains(target)) {
                    ChatInfo senderInfo = manager.getInfo(player);
                    ChatInfo targetInfo = manager.getInfo(target);
                    String msg = ChatColor.stripColor(String.join(" ", Arrays.copyOfRange(args, 1, args.length)));
                    sendWhisper(player, senderInfo, target, targetInfo, msg);
                } else Utils.sendPrefixedLocalizedMessage(player, "msg_could_not_find_player", args[0]);
            } else Utils.sendPrefixedLocalizedMessage(player, "msg_command_syntax");
        } else Utils.sendMessage(sender, "&cYou must be a player");
        return true;
    }
}
