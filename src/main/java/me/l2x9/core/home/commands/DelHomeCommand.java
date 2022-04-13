package me.l2x9.core.home.commands;

import lombok.AllArgsConstructor;
import me.l2x9.core.home.Home;
import me.l2x9.core.home.HomeManager;
import me.l2x9.core.util.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class DelHomeCommand implements CommandExecutor {
    HomeManager main;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length < 1) {
                Utils.sendMessage(player, "&3Please include the name of a home to delete");
                return true;
            }
            Home home = main.getHomes().getOrDefault(player.getUniqueId(), null).stream().filter(h -> h.getName().equals(args[0])).findFirst().orElse(null);
            if (home == null) {
                Utils.sendMessage(player, "&3Home&r&a " + args[0] + "&r&3 was not found");
                return true;
            }
            if (main.getHomeUtil().deleteHome(home)) {
                Utils.sendMessage(player, "&3Home&r&a " + home.getName() + "&r&3 has been deleted");
            } else Utils.sendMessage(player, "&3Could not delete&r&a " + home.getName());
        } else Utils.sendMessage(sender, "&3You must be a player to use this command");
        return true;
    }
}
