package me.l2x9.core.home.commands;

import lombok.AllArgsConstructor;
import me.l2x9.core.home.Home;
import me.l2x9.core.home.HomeManager;
import me.l2x9.core.util.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public class SetHomeCommand implements CommandExecutor {
    private final HomeManager main;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length < 1) {
                Utils.sendPrefixedLocalizedMessage(player, "sethome_include_name");
                return true;
            }
            Set<PermissionAttachmentInfo> perms = player.getEffectivePermissions();
            List<Integer> maxL = perms.stream().map(PermissionAttachmentInfo::getPermission).filter(p -> p.startsWith("l2x9core.home.max.")).map(s -> Integer.parseInt(s.substring(s.lastIndexOf('.') + 1))).collect(Collectors.toList());
            int maxHomes = (!maxL.isEmpty()) ? Collections.max(maxL) : main.getConfig().getInt("MaxHomes");
            List<Home> homes = main.getHomes().getOrDefault(player.getUniqueId(), null);
            if (homes == null) homes = new ArrayList<>();
            if (homes.stream().anyMatch(h -> h.getName().equals(args[0]))) {
                Home home = homes.stream().filter(h -> h.getName().equals(args[0])).findAny().get();
                Utils.sendPrefixedLocalizedMessage(player, "sethome_home_already_exists");
                main.getHomeIO().deleteHome(home);
            }
            if (homes.size() >= maxHomes && !player.isOp()) {
                Utils.sendPrefixedLocalizedMessage(player, "sethome_max_reached");
                return true;
            }
            File playerFolder = new File(main.getHomeIO().getHomesFolder(), player.getUniqueId().toString());
            if (!playerFolder.exists()) playerFolder.mkdir();
            Home home = new Home(args[0], player.getUniqueId(), player.getLocation());
            main.getHomeIO().save(playerFolder, home.getName() + ".map", home);
            Utils.sendPrefixedLocalizedMessage(player, "sethome_success", home.getName());
        } else Utils.sendMessage(sender, "&3You must be a player to use this command");
        return true;
    }
}

