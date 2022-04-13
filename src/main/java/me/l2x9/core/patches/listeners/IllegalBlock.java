package me.l2x9.core.patches.listeners;

import me.l2x9.core.util.Utils;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class IllegalBlock implements Listener {
    private final List<Material> materials;

    public IllegalBlock(FileConfiguration config) {
        List<String> strBlocks = config.getStringList("IllegalBlocks");
        materials = strBlocks.stream().map(Material::getMaterial).filter(Material::isBlock).collect(Collectors.toList());
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Material material = event.getBlock().getType();
        if (materials.contains(material)) {
            Player player = event.getPlayer();
            event.setCancelled(true);
            Arrays.stream(player.getInventory().getContents()).filter(Objects::nonNull).filter(i -> materials.contains(i.getType())).forEach(s -> player.getInventory().remove(s));
            Utils.log(String.format("&aPrevented %s from placing an illegal block at %s", player.getName(), Utils.formatLocation(event.getBlock().getLocation())));
        }
    }
}
