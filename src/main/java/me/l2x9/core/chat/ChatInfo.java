package me.l2x9.core.chat;

import lombok.Cleanup;
import lombok.Data;
import me.l2x9.core.util.Utils;
import org.bukkit.entity.Player;

import java.io.*;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class ChatInfo {
    private final Player player;
    private final HashSet<UUID> ignoring;
    private final ChatManager manager;
    private Player replyTarget;
    private boolean toggledChat;
    private boolean joinMessages;
    private boolean chatLock;

    public ChatInfo(Player player, ChatManager manager) {
        this.player = player;
        this.manager = manager;
        ignoring = loadIgnores();
    }

    public boolean isIgnoring(UUID player) {
        return ignoring.contains(player);
    }

    public void ignorePlayer(UUID player) {
        ignoring.add(player);
    }

    public void unignorePlayer(UUID player) {
        ignoring.remove(player);
    }

    public void saveIgnores() {
        File ignoreList = new File(manager.getIgnoresFolder(), player.getName().concat(".lst"));
        try {
            if (ignoreList.exists() && ignoring.size() == 0) {
                ignoreList.delete();
                return;
            }
            if (!ignoreList.exists()) ignoreList.createNewFile();
            @Cleanup OutputStream fos = new FileOutputStream(ignoreList, false);
            @Cleanup OutputStreamWriter osw = new OutputStreamWriter(fos);
            @Cleanup BufferedWriter writer = new BufferedWriter(osw);
            for (UUID id : ignoring) writer.write(id.toString().concat("\n"));
        } catch (Throwable t) {
            Utils.log("&cFailed to save ignores for player &r&a%s&r&c please see the stacktrace below for more info", player.getName());
            t.printStackTrace();
        }
    }

    private HashSet<UUID> loadIgnores() {
        File ignoreList = new File(manager.getIgnoresFolder(), player.getName().concat(".lst"));
        if (!ignoreList.exists()) return new HashSet<>();
        try {
            @Cleanup InputStream fis = Files.newInputStream(ignoreList.toPath());
            @Cleanup InputStreamReader isr = new InputStreamReader(fis);
            @Cleanup BufferedReader reader = new BufferedReader(isr);
            return reader.lines().map(UUID::fromString).collect(Collectors.toCollection(HashSet::new));
        } catch (Throwable t) {
            Utils.log("&cFailed to load ignores for player&r&a %s&r", player.getName());
            t.printStackTrace();
            return new HashSet<>();
        }
    }
}
