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
            ChatInfo info = manager.getInfo((Player) sender);
            if (info.isToggledChat()) {
                Utils.sendMessage(sender, "&aEnabled chat!");
            } else Utils.sendMessage(sender, "&cDisabled chat!");
            info.setToggledChat(!info.isToggledChat());
        } else Utils.sendMessage(sender, "&cYou must be a player");
        return true;
    }
}
