package me.l2x9.core.command.commands;

import me.l2x9.core.command.BaseCommand;
import me.l2x9.core.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopEntity extends BaseCommand {
    public TopEntity() {
        super("etop", "Show the top entitys", "l2x9core.command.etop");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        int total = 0;
        HashMap<EntityType, Integer> amounts = new HashMap<>();
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity instanceof Player) continue;
                total++;
                if (!amounts.containsKey(entity.getType())) {
                    amounts.put(entity.getType(), 1);
                } else amounts.replace(entity.getType(), amounts.get(entity.getType()) + 1);
            }
        }
        HashMap<EntityType, Integer> sorted = sort(amounts);
        if (args.length > 1) {
            String deep = args[0];
            if (deep.equalsIgnoreCase("deep")) {
                Utils.sendMessage(sender, "&3Total entities&r&a " + total);
                sorted.forEach((key, val) -> Utils.sendMessage(sender, "&3Total &r&a" + key.name().toLowerCase().replace("_", " ") + "s&r&3 on the server&r&a " + val));
            } else {
                printResults(sender, total, sorted);
            }
        } else {
            printResults(sender, total, sorted);
        }
    }

    private void printResults(CommandSender sender, int total, HashMap<EntityType, Integer> sorted) {
        Utils.sendMessage(sender, "&3Total entities&r&a " + total);
        int count = 0;
        for (Map.Entry<EntityType, Integer> entry : sorted.entrySet()) {
            count++;
            if (count >= 5) break;
            Utils.sendMessage(sender, "&3Total &r&a" + entry.getKey().name().toLowerCase().replace("_", " ") + "s&r&3 on the server&r&a " + entry.getValue());
        }
    }

    private HashMap<EntityType, Integer> sort(HashMap<EntityType, Integer> map) {
        List<Map.Entry<EntityType, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());

        HashMap<EntityType, Integer> sortedMap = new HashMap<>();
        list.forEach(e -> sortedMap.put(e.getKey(), e.getValue()));
        return sortedMap;
    }
}
