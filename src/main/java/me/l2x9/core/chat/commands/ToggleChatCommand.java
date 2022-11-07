package me.l2x9.core.chat.commands;

import me.l2x9.core.chat.ChatInfo;
import me.l2x9.core.chat.ChatManager;
import me.l2x9.core.util.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleChatCommand implements CommandExecutor {
    private final ChatManager manager;

    public ToggleChatCommand(ChatManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ChatInfo info = manager.getInfo(player);
            if (info.isToggledChat()) {
                Utils.sendPrefixedLocalizedMessage(player, "togglechat_chat_enabled");
            } else Utils.sendPrefixedLocalizedMessage(player, "togglechat_chat_disabled");
            info.setToggledChat(!info.isToggledChat());
        } else Utils.sendMessage(sender, "&cYou must be a player");
        return true;
    }
}
