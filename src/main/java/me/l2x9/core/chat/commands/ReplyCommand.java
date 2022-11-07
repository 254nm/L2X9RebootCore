package me.l2x9.core.chat.commands;

import me.l2x9.core.chat.ChatCommand;
import me.l2x9.core.chat.ChatInfo;
import me.l2x9.core.chat.ChatManager;
import me.l2x9.core.util.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReplyCommand extends ChatCommand {
    private final ChatManager manager;

    public ReplyCommand(ChatManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length >= 1) {
                ChatInfo senderInfo = manager.getInfo(player);
                if (senderInfo.getReplyTarget() != null) {
                    Player target = senderInfo.getReplyTarget();
                    if (target.isOnline()) {
                        ChatInfo targetInfo = manager.getInfo(target);
                        String msg = ChatColor.stripColor(String.join(" ", args));
                        sendWhisper(player, senderInfo, target, targetInfo, msg);
                    } else Utils.sendPrefixedLocalizedMessage(player, "reply_player_offline", target.getName());
                } else Utils.sendPrefixedLocalizedMessage(player, "reply_no_target");
            } else Utils.sendPrefixedLocalizedMessage(player, "reply_command_syntax");
        } else Utils.sendMessage(sender, "&cYou must be a player");
        return true;
    }
}
