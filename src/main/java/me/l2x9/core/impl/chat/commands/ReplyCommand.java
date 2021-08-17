package me.l2x9.core.impl.chat.commands;

import me.l2x9.core.impl.chat.ChatCommand;
import me.l2x9.core.impl.chat.ChatInfo;
import me.l2x9.core.impl.chat.ChatManager;
import me.l2x9.core.boiler.util.Utils;
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
            if (args.length >= 1) {
                Player player = (Player) sender;
                ChatInfo senderInfo = manager.getInfo(player);
                if (senderInfo.getReplyTarget() != null) {
                    Player target = senderInfo.getReplyTarget();
                    if (target.isOnline()) {
                        ChatInfo targetInfo = manager.getInfo(target);
                        String msg = ChatColor.stripColor(String.join(" ", args));
                        sendWhisper(player, senderInfo, target, targetInfo, msg);
                    } else Utils.sendMessage(player, "&cThe player " + target.getName() + " is offline");
                } else Utils.sendMessage(player, "&cYou have not messaged anyone yet");
            } else Utils.sendMessage(sender, "&cPlease include a message /r <message>");
        } else Utils.sendMessage(sender, "&cYou must be a player");
        return true;
    }
}
