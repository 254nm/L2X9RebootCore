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
                Utils.sendPrefixMessage(player, "&3You do not have any homes");
                return true;
            }
            if (args.length < 1) {
                String names = String.join(", ", homes.stream().map(Home::getName).toArray(String[]::new));
                Utils.sendPrefixMessage(player, String.format("&3Please include the name of a home to delete. Current homes: (&r&a%s&r&3)&r", names));
                return true;
            }
            Home home = homes.stream().filter(h -> h.getName().equals(args[0])).findFirst().orElse(null);
            if (home == null) {
                Utils.sendPrefixMessage(player, "&3Home&r&a " + args[0] + "&r&3 was not found");
                return true;
            }
            if (main.getHomeIO().deleteHome(home)) {
                Utils.sendPrefixMessage(player, "&3Home&r&a " + home.getName() + "&r&3 has been deleted");
            } else Utils.sendPrefixMessage(player, "&3Could not delete&r&a " + home.getName());
        } else Utils.sendPrefixMessage(sender, "&3You must be a player to use this command");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return main.tabComplete(sender, args);
    }
}
