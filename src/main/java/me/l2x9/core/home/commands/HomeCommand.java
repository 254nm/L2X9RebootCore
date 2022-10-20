package me.l2x9.core.home.commands;

import lombok.RequiredArgsConstructor;
import me.l2x9.core.L2X9RebootCore;
import me.l2x9.core.home.Home;
import me.l2x9.core.home.HomeManager;
import me.l2x9.core.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

@RequiredArgsConstructor
public class HomeCommand implements TabExecutor {
    private final HomeManager main;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            int radius = main.getConfig().getInt("Radius");
            List<Home> homes = main.getHomes().getOrDefault(player.getUniqueId(), null);
            if (homes == null) {
                Utils.sendPrefixMessage(player, "&3No homes found");
                return true;
            }
            String names = String.join(", ", homes.stream().map(Home::getName).toArray(String[]::new));
            if (args.length < 1) {
                Utils.sendPrefixMessage(player, "&3Please include a home! Current homes: (&r&a" + names + "&r&3)&r");
                return true;
            }
            if (isSpawn(player, radius)) {
                Utils.sendPrefixMessage(player, "&3You must be&r&a " + radius + "&r&3 blocks from spawn in order to use /home");
                return true;
            }
            boolean teleported = false;
            for (Home home : homes) {
                if (home.getName().equalsIgnoreCase(args[0])) {
                    vanish(player);
                    player.teleport(home.getLocation());
                    unVanish(player);
                    Utils.sendPrefixMessage(player, "&3Teleporting to home&r&a " + home.getName() + "&r&3...");
                    teleported = true;
                    break;
                }
            }
            if (!teleported) Utils.sendPrefixMessage(player, "&3Home&r&a " + args[0] + "&r&3 was not found");
        } else Utils.sendPrefixMessage(sender, "&3You must be a player to use this command");
        return true;
    }

    private boolean isSpawn(Player player, int range) {
        if (player.isOp()) return false;
        Location loc = player.getLocation();
        return loc.getBlockX() < range && loc.getBlockX() > -range && loc.getBlockZ() < range && loc.getBlockZ() > -range;
    }

    private void vanish(Player player) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!onlinePlayer.equals(player)) {
                onlinePlayer.hidePlayer(L2X9RebootCore.getInstance(), player);
            }
        }
    }

    public void unVanish(Player player) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!onlinePlayer.equals(player)) {
                onlinePlayer.showPlayer(L2X9RebootCore.getInstance(), player);
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return main.tabComplete(sender, args);
    }
}