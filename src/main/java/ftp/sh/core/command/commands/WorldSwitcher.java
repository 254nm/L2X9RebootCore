package ftp.sh.core.command.commands;

import ftp.sh.core.command.BaseTabCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WorldSwitcher extends BaseTabCommand {

    public WorldSwitcher() {
        super(
                "world",
                "/world <worldName>",
                "l2x9core.command.world",
                "Switch worlds",
                new String[]{
                        "overworld::Teleport your self to the overworld",
                        "nether::Teleport your self to the nether",
                        "end::Teleport your self to the end"
                }
        );
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = getSenderAsPlayer(sender);
        if (player != null) {
            if (args.length > 0) {
                String worldName = Bukkit.getWorlds().get(0).getName();
                double x = player.getLocation().getX();
                double y = player.getLocation().getY();
                double z = player.getLocation().getZ();
                switch (args[0]) {
                    case "overworld":
                        World overWorld = Bukkit.getWorld(worldName);
                        player.teleport(new Location(overWorld, x, y, z));
                        sendMessage(player, "&6Teleporting to &r&c" + args[0]);
                        break;
                    case "nether":
                        World netherWorld = Bukkit.getWorld(worldName.concat("_nether"));
                        if (y < 128) {
                            player.teleport(new Location(netherWorld, x, 125, z));
                        } else {
                            player.teleport(new Location(netherWorld, x, y, z));

                        }
                        sendMessage(player, "&6Teleporting to &r&c" + args[0]);
                        break;
                    case "end":
                        World endWorld = Bukkit.getWorld(worldName.concat("_the_end"));
                        player.teleport(new Location(endWorld, x, y, z));
                        sendMessage(player, "&6Teleporting to &r&c" + args[0]);
                        break;
                    default:
                        sendMessage(sender, "&4Error:&r&c Unknown world");
                        break;
                }
            } else {
                sendErrorMessage(sender, "Please include one argument /world <end | overworld | nether>");
            }
        } else {
            sendErrorMessage(sender, PLAYER_ONLY);
        }
    }

    @Override
    public List<String> onTab(String[] args) {
        List<String> list;
        if (args.length > 0) {
            if (args[0].startsWith("o")) {
                list = Collections.singletonList("overworld");
                return list;
            }
            if (args[0].startsWith("e")) {
                list = Collections.singletonList("end");
                return list;
            }
            if (args[0].startsWith("n")) {
                list = Collections.singletonList("nether");
                return list;
            }
        } else {
            list = Arrays.asList("overworld", "nether", "end");
            return list;
        }
        return null;
    }
}