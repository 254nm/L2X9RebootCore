package me.l2x9.core.command.commands;

import me.l2x9.core.command.BaseCommand;
import me.l2x9.core.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CrashCommand extends BaseCommand {

    public CrashCommand() {
        super(
                "crash",
                "/crash <player> | nearby <radius> | everyone | elytra | taco",
                "l2x9core.command.crash",
                "Crash players games",
                new String[]{
                        "elytra::Crash the games of everyone who is using an elytra",
                        "everyone::Crash the game of everyone on the server",
                        "nearby::Crash everyone within a radius",
                        "taco::Crash the game of everyone with their game set to a spanish language"
                });
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sendErrorMessage(sender, getUsage());
        } else {
            switch (args[0]) {
                case "elytra":
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        if (!online.isOp()) {
                            if (online.isGliding()) {
                                Utils.crashPlayer(online);
                                sendMessage(sender, "&6You have just crashed&r&c " + online.getName());

                            }
                        }
                    }
                    break;
                case "everyone":
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        if (!online.isOp()) {
                            Utils.crashPlayer(online);
                            sendMessage(sender, "&6You have just crashed&r&c " + online.getName());
                        }
                    }
                    break;
                case "nearby":
                    getSenderAsPlayer(sender).ifPresent(player -> {
                        try {
                            for (Player nearby : player.getLocation().getWorld().getNearbyEntities(player.getLocation(), Double.parseDouble(args[1]), Double.parseDouble(args[1]), Double.parseDouble(args[1])).stream().filter(e -> e instanceof Player).toArray(Player[]::new)) {
                                if (!nearby.hasPermission(getPermission())) {
                                    Utils.crashPlayer(nearby);
                                    sendMessage(player, "&6You have just crashed&r&c " + nearby.getName());
                                }
                            }
                        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                            sendMessage(sender, "The second argument must be a number");
                        }
                    });
                    break;
                case "taco":
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        if (online.getLocale().toLowerCase().contains("es")) {
                            Utils.crashPlayer(online);
                            sendMessage(sender, "&6You have just crashed&r&c " + online.getName());
                        }
                    }
                    break;
                default:
                    Player target = Bukkit.getPlayer(args[0]);
                    if (Bukkit.getOnlinePlayers().contains(target)) {
                        if (!target.hasPermission(getPermission())) {
                            Utils.crashPlayer(target);
                            sendMessage(sender, "&6You have just crashed&r&c " + target.getName());
                        } else {
                            sendErrorMessage(sender, "You cannot crash that player");
                        }
                    } else {
                        sendErrorMessage(sender, "Target not online");
                    }
                    break;
            }
        }
    }
}