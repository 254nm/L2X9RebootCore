package me.l2x9.core.chat;

import me.l2x9.core.util.Utils;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

public abstract class ChatCommand implements CommandExecutor {
    public void sendWhisper(Player player, ChatInfo senderInfo, Player target, ChatInfo targetInfo, String msg) {
        if (!senderInfo.isIgnoring(target.getUniqueId())) {
            if (!targetInfo.isIgnoring(player.getUniqueId())) {
                targetInfo.setReplyTarget(player);
                senderInfo.setReplyTarget(target);
                Utils.sendMessage(target, "&d" + player.getName() + " Whispers: " + msg, false);
                Utils.sendMessage(player, "&dTo " + target.getName().concat(": ") + msg, false);
            } else Utils.sendMessage(player, "&c" + target.getName() + " is ignoring you", false);
        } else Utils.sendMessage(player, "&cYou cannot message a player that you have ignored", false);
    }
}
