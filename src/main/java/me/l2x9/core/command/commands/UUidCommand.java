package me.l2x9.core.command.commands;

import me.l2x9.core.command.BaseCommand;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;

import java.util.UUID;

public class UUidCommand extends BaseCommand {

    public UUidCommand() {
        super(
                "uuid",
                "/uuid <player>",
                "l2x9core.command.uuid",
                "Get the uuid of the specified player");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length > 0) {
            UUID uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + args[0]).getBytes());
            sendClickableMessage(
                    sender,
                    "&3The UUID of&r&a " + args[0] + "&r&3 is &r&a" + uuid,
                    "&3Click to copy",
                    uuid.toString(),
                    ClickEvent.Action.SUGGEST_COMMAND);

        } else sendErrorMessage(sender, "Please include at least one argument /uuid <playerName>");
    }

    private void sendClickableMessage(CommandSender sender, String message, String hoverText, String command, ClickEvent.Action action) {
        TextComponent msg = new TextComponent(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', message));
        msg.setClickEvent(new ClickEvent(action, command));
        msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', hoverText))
                        .create()));
        sender.spigot().sendMessage(msg);
    }
}
