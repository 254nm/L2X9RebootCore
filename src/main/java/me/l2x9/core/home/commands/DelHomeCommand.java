package me.l2x9.core.home.commands;

import lombok.RequiredArgsConstructor;
import me.l2x9.core.home.Home;
import me.l2x9.core.home.HomeManager;
import me.l2x9.core.util.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

@RequiredArgsConstructor
public class DelHomeCommand implements TabExecutor {
    private final HomeManager main;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            List<Home> homes = main.getHomes().getOrDefault(player.getUniqueId(), null);
            if (homes == null) {
                Utils.sendPrefixedLocalizedMessage(player, "delhome_no_homes");
                return true;
            }
            if (args.length < 1) {
                String names = String.join(", ", homes.stream().map(Home::getName).toArray(String[]::new));
                Utils.sendPrefixedLocalizedMessage(player, "delhome_specify_home", names);
                return true;
            }
            Home home = homes.stream().filter(h -> h.getName().equals(args[0])).findFirst().orElse(null);
            if (home == null) {
                Utils.sendPrefixedLocalizedMessage(player, "delhome_home_not_found", args[0]);
                return true;
            }
            if (main.getHomeIO().deleteHome(home)) {
                Utils.sendPrefixedLocalizedMessage(player, "delhome_success", home.getName());
            } else Utils.sendPrefixedLocalizedMessage(player, "delhome_fail", home.getName());
        } else Utils.sendMessage(sender, "&3You must be a player to use this command");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return main.tabComplete(sender, args);
    }
}
