package me.l2x9.core.impl.home.commands;

import me.l2x9.core.boiler.util.Utils;
import me.l2x9.core.impl.home.Home;
import me.l2x9.core.impl.home.HomeManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DelHomeCommand implements CommandExecutor {
    HomeManager main;

    public DelHomeCommand(HomeManager main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ArrayList<Home> homes = main.getHomes().getOrDefault(player.getUniqueId(), null);
            File playerFolder = new File(main.getHomeUtil().getHomesFolder(), player.getUniqueId().toString());
            if (args.length < 1) {
                Utils.sendMessage(player, "&3Please include the name of a home to delete");
                return true;
            }
            Home home =  homes.stream().filter(h -> h.getName().equals(args[0])).findFirst().orElse(null);
            if (home == null) {
                Utils.sendMessage(player, "&3Home&r&a " + args[0] + "&r&3 was not found");
                return true;
            }
            File homeFile = new File(playerFolder, home.getName() + ".map");
            if (homeFile.delete()) {
                homes.remove(home);
                main.getHomes().replace(player.getUniqueId(), homes);
                Utils.sendMessage(player, "&3Home&r&a " + home.getName() + "&r&3 has been deleted");
            } else {
                Utils.sendMessage(player, "&3Could not delete&r&a " + home.getName());
            }
        } else Utils.sendMessage(sender, "&3You must be a player to use this command");
        return true;
    }
}
