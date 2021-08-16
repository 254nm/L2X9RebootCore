package ftp.sh.core.home.commands;

import ftp.sh.core.L2X9RebootCore;
import ftp.sh.core.home.Home;
import ftp.sh.core.home.HomeManager;
import ftp.sh.core.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class HomeCommand implements CommandExecutor {
    private final HomeManager main;

    public HomeCommand(HomeManager main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            int radius = main.getConfig().getInt("Radius");
            List<Home> homes = main.getHomes().getOrDefault(player.getUniqueId(), null);
            if (homes == null) {
                Utils.sendMessage(player, "&3No homes found");
                return true;
            }
            String names = String.join(", ", homes.stream().map(Home::getName).toArray(String[]::new));
            if (args.length < 1) {
                Utils.sendMessage(player, "&3Please include a home! Current homes: (&r&a" + names + "&r&3)&r");
                return true;
            }
            if (isSpawn(player, radius)) {
                Utils.sendMessage(player, "&3You must be&r&a " + radius + "&r&3 blocks from spawn in order to use /home");
                return true;
            }
            boolean teleported = false;
            for (Home home : homes) {
                if (home.getName().equalsIgnoreCase(args[0])) {
                    vanish(player);
                    player.teleport(home.getLocation());
                    unVanish(player);
                    Utils.sendMessage(player, "&3Teleporting to home&r&a " + home.getName() + "&r&3...");
                    teleported = true;
                    break;
                }
            }
            if (!teleported) Utils.sendMessage(player, "&3Home&r&a " + args[0] + "&r&3 was not found");
        } else Utils.sendMessage(sender, "&3You must be a player to use this command");
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
}