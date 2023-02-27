package me.l2x9.core.command.commands;

import me.l2x9.core.command.BaseCommand;
import me.l2x9.core.util.Utils;
import net.minecraft.server.v1_12_R1.NBTCompressedStreamTools;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.WorldNBTStorage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;

import java.io.File;
import java.io.FileInputStream;


/**
 * @author 254n_m
 * @since 2023/02/26 6:57 PM
 * This file was created as a part of L2X9RebootCore
 */
public class WhoIs extends BaseCommand {
    public WhoIs() {
        super(
                "whois",
                "/whois <uuid>",
                "l2x9core.command.whois",
                "Get some basic player info based on UUID");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 1) {
            File dataDir = ((WorldNBTStorage) ((CraftServer) Bukkit.getServer()).getHandle().playerFileData).getPlayerDir();
            File dataFile = new File(dataDir, args[0].concat(".dat"));
            if (dataFile.exists()) {
                try {
                    NBTTagCompound comp = NBTCompressedStreamTools.a(new FileInputStream(dataFile)).getCompound("bukkit");
                    String name = comp.getString("lastKnownName");
                    long lastPlayed = comp.getLong("lastPlayed");
                    sendMessage(sender, "&3-- &r&a%s &3--", args[0]);
                    sendMessage(sender, "&3Name:&r&a %s", name);
                    sendMessage(sender, "&3Last Played:&r&a %s&r&3 ago", Utils.getFormattedInterval(System.currentTimeMillis() - lastPlayed));
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            } else sendMessage(sender, "&c%s either is not a valid uuid or has never played the server before", args[0]);
        } else sendErrorMessage(sender, getUsage());
    }
}
