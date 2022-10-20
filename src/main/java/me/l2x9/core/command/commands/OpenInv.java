package me.l2x9.core.command.commands;


import me.l2x9.core.command.BaseCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenInv extends BaseCommand {
    public OpenInv() {
        super("open", "/open <inv | ender> <player>", "l2x9core.command.openinv", "Open peoples inventories", new String[]{"inv::Open the inventory of the specified player", "ender::Open the ender chest of the specified player"});
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = getSenderAsPlayer(sender).orElse(null);
        if (player != null) {
            if (args.length < 2) {
                sendErrorMessage(sender, getUsage());
            } else {
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sendMessage(sender, "&cPlayer&r&a %s&r&c not online&r", args[1]);
                    return;
                }
                switch (args[0]) {
                    case "ender":
                        player.openInventory(target.getEnderChest());
                        break;
                    case "inv":
                    case "inventory":
                        player.openInventory(target.getInventory());
                        break;
                    default:
                        sendErrorMessage(sender, "Unknown argument " + args[0]);
                }
            }
        } else sendErrorMessage(sender, PLAYER_ONLY);
    }
}
