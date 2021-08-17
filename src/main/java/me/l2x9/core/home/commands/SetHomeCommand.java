package me.l2x9.core.home.commands;

import me.l2x9.core.home.Home;
import me.l2x9.core.home.HomeManager;
import me.l2x9.core.util.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;

public class SetHomeCommand implements CommandExecutor {
    private final HomeManager main;

    public SetHomeCommand(HomeManager main) {
        this.main = main;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            int maxHomes = main.getConfig().getInt("MaxHomes");
            List<Home> homes = main.getHomes().getOrDefault(player.getUniqueId(), null);
            if (homes != null && homes.size() >= maxHomes) {
                Utils.sendMessage(player, "&3Max number of homes reached!");
                return true;
            }
            File playerFolder = new File(main.getHomeUtil().getHomesFolder(), player.getUniqueId().toString());
            if (!playerFolder.exists()) playerFolder.mkdir();
            if (args.length < 1) {
                Utils.sendMessage(player, "&3Please include a name for your new home");
                return true;
            }
            Home home = new Home(args[0], player.getLocation(), player.getUniqueId());
            main.getHomeUtil().save(playerFolder, home.getName() + ".map", home);
            Utils.sendMessage(player, "&3Home&r&a " + home.getName() + " &r&3Set");
        } else Utils.sendMessage(sender, "&3You must be a player to use this command");
        return true;
    }
}
