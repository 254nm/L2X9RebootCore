package me.l2x9.core.home.commands;

import lombok.AllArgsConstructor;
import me.l2x9.core.home.Home;
import me.l2x9.core.home.HomeManager;
import me.l2x9.core.util.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class SetHomeCommand implements CommandExecutor {
    private final HomeManager main;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length < 1) {
                Utils.sendMessage(player, "&3Please include a name for your new home");
                return true;
            }
            int maxHomes = main.getConfig().getInt("MaxHomes");
            List<Home> homes = main.getHomes().getOrDefault(player.getUniqueId(), null);
            if (homes == null) homes = new ArrayList<>();
            if (homes.stream().anyMatch(h -> h.getName().equals(args[0]))) {
                Home home = homes.stream().filter(h -> h.getName().equals(args[0])).findAny().get();
                Utils.sendMessage(sender, "A home by that name already exists.");
                main.getHomeIO().deleteHome(home);
            }
            if (homes.size() >= maxHomes) {
                Utils.sendMessage(player, "&3Max number of homes reached!");
                return true;
            }
            File playerFolder = new File(main.getHomeIO().getHomesFolder(), player.getUniqueId().toString());
            if (!playerFolder.exists()) playerFolder.mkdir();
            Home home = new Home(args[0], player.getUniqueId(), player.getLocation());
            main.getHomeIO().save(playerFolder, home.getName() + ".map", home);
            Utils.sendMessage(player, "&3Home&r&a " + home.getName() + " &r&3Set");
        } else Utils.sendMessage(sender, "&3You must be a player to use this command");
        return true;
    }
}
