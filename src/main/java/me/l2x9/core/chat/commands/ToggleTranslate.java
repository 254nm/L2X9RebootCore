package me.l2x9.core.chat.commands;

import me.l2x9.core.chat.ChatInfo;
import me.l2x9.core.chat.ChatManager;
import me.l2x9.core.util.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author 254n_m
 * @since 2023/04/30 4:50 AM
 * This file was created as a part of L2X9RebootCore
 */
public class ToggleTranslate implements CommandExecutor {
    private final ChatManager manager;

    public ToggleTranslate(ChatManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ChatInfo info = manager.getInfo(player);
            if (info.isAutoTranslate()) {
                Utils.sendPrefixedLocalizedMessage(player, "chat_toggle_translate_disabled");
            } else Utils.sendPrefixedLocalizedMessage(player, "chat_toggle_translate_enabled");
            info.setAutoTranslate(!info.isAutoTranslate());
        } else Utils.sendMessage(sender, "&cYou must be a player");
        return true;
    }
}
