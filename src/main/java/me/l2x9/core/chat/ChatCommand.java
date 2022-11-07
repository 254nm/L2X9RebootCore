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
                Utils.sendLocalizedMessage(target, "whisper_from", false, player.getName(), msg);
                Utils.sendLocalizedMessage(player, "whisper_to", false, target.getName(), msg);
            } else Utils.sendLocalizedMessage(player, "whisper_ignoring", false, target.getName());
        } else Utils.sendLocalizedMessage(player, "whisper_you_are_ignoring", false);
    }
}
