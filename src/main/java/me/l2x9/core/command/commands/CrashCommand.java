package me.l2x9.core.command.commands;

import me.l2x9.core.command.BaseCommand;
import me.l2x9.core.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicInteger;

public class CrashCommand extends BaseCommand {

    public CrashCommand() {
        super("crash", "/crash <player> | nearby <radius> | everyone | elytra | taco", "l2x9core.command.crash", "Crash players games", new String[]{"elytra::Crash the games of everyone who is using an elytra", "everyone::Crash the game of everyone on the server", "nearby::Crash everyone within a radius", "taco::Crash the game of everyone with their game set to a spanish language"});
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sendErrorMessage(sender, getUsage());
        } else {
            AtomicInteger count = new AtomicInteger(0);
            switch (args[0]) {
                case "elytra":
                    Bukkit.getOnlinePlayers().stream().filter(p -> !p.isOp()).filter(Player::isGliding).forEach(p -> {
                        Utils.crashPlayer(p);
                        count.incrementAndGet();
                    });
                    sendMessage(sender, "&3You have just crashed &r&a%d&r&3 %s", count.get(), (count.get() == 1) ? "player" : "players");
                    break;
                case "everyone":
                    Bukkit.getOnlinePlayers().stream().filter(p -> !p.isOp()).forEach(p -> {
                        Utils.crashPlayer(p);
                        count.incrementAndGet();
                    });
                    sendMessage(sender, "&3You have just crashed &r&a%d&r&3 %s", count.get(), (count.get() == 1) ? "player" : "players");
                    break;
                case "nearby":
                    getSenderAsPlayer(sender).ifPresent(player -> {
                        try {
                            double x = Double.parseDouble(args[1]), y = Double.parseDouble(args[1]), z = Double.parseDouble(args[1]);
                            for (Player nearby : player.getLocation().getWorld().getNearbyEntitiesByType(Player.class, player.getLocation(), x, y, z).toArray(new Player[0])) {
                                if (nearby.hasPermission(getPermission())) continue;
                                Utils.crashPlayer(nearby);
                                sendMessage(player, "&3You have just crashed &r&a%s&r", nearby.getName());
                            }
                        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                            sendMessage(sender, "The second argument must be a number");
                        }
                    });
                    break;
                case "taco":
                    Bukkit.getOnlinePlayers().stream().filter(p -> !p.isOp()).filter(p -> p.getLocale().toLowerCase().contains("es")).forEach(p -> {
                        count.incrementAndGet();
                        Utils.crashPlayer(p);
                    });
                    sendMessage(sender, "&3You have just crashed &r&a%d&r&3 %s", count.get(), (count.get() == 1) ? "player" : "players");
                    break;
                default:
                    Player target = Bukkit.getPlayer(args[0]);
                    if (Bukkit.getOnlinePlayers().contains(target)) {
                        if (!target.hasPermission(getPermission())) {
                            Utils.crashPlayer(target);
                            sendMessage(sender, "&3You have just crashed &r&a%s", target.getName());
                        } else sendErrorMessage(sender, "&cYou cannot crash that player");
                    } else sendErrorMessage(sender, "&cTarget not online");
                    break;
            }
        }
    }
}