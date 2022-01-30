package me.l2x9.core.impl.home.util;

import me.l2x9.core.impl.home.Home;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class HomeUtil {
    private final File homesFolder;
    private final HashMap<UUID, ArrayList<Home>> homes;

    public HomeUtil(File homesFolder) {
        this.homesFolder = homesFolder;
        homes = new HashMap<>();
    }

    public File getHomesFolder() {
        return homesFolder;
    }

    public HashMap<UUID, ArrayList<Home>> getHomes() {
        return homes;
    }

    private Home parseHome(File mapFile) {
        try {
            FileInputStream fis = new FileInputStream(mapFile);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isr);
            String[] lines = reader.lines().toArray(String[]::new);
            String[] locArray = lines[1].split("::");
            double x = Double.parseDouble(locArray[0]), y = Double.parseDouble(locArray[1]), z = Double.parseDouble(locArray[2]);
            World world = Bukkit.getWorld(locArray[3]);
            UUID owner = UUID.fromString(lines[0]);
            Location loc = new Location(world, x, y, z);
            String name = lines[2];
            fis.close();
            isr.close();
            reader.close();
            return new Home(name, loc, owner);
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }

    public void loadHomes(Player player) {
        for (File data : homesFolder.listFiles()) {
            if (!data.isDirectory()) continue;
            if (!data.getName().equals(player.getUniqueId().toString())) continue;
            ArrayList<Home> homeList = new ArrayList<>();
            for (File mapFile : data.listFiles()) {
                if (!getFileExtension(mapFile).equals(".map")) continue;
                homeList.add(parseHome(mapFile));
            }
            if (homes.containsKey(player.getUniqueId())) {
                homes.replace(player.getUniqueId(), homeList);
            } else {
                homes.put(player.getUniqueId(), homeList);
            }
            break;
        }
    }

    public void save(File dataFolder, String fileName, Home home) {
        try {
            if (!dataFolder.exists()) dataFolder.mkdir();
            File file = new File(dataFolder, fileName);
            if (!file.exists()) file.createNewFile();
            UUID owner = home.getOwner();
            Location loc = home.getLocation();
            String name = home.getName();
            FileWriter fw = new FileWriter(file);
            double x = loc.getX(), y = loc.getY(), z = loc.getZ();
            String world = loc.getWorld().getName();
            String[] serialized = new String[3];
            serialized[0] = owner.toString();
            serialized[1] = x + "::" + y + "::" + z + "::" + world;
            serialized[2] = name;
            for (String str : serialized) fw.write(str + "\n");
            if (homes.containsKey(owner)) {
                ArrayList<Home> homeList = new ArrayList<>(homes.get(owner));
                homeList.add(home);
                homes.replace(owner, homeList);
            } else homes.put(owner, new ArrayList<>(Collections.singletonList(home)));
            fw.flush();
            fw.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int index = name.lastIndexOf(".");
        if (index == -1) {
            return "";
        }
        return name.substring(index);
    }

    public boolean hasHomes(Player player) {
        return homes.containsKey(player.getUniqueId());
    }
}