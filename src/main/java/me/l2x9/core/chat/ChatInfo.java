package me.l2x9.core.chat;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
public class ChatInfo {
    private final Player player;
    private final List<UUID> ignoring;
    private final ChatManager manager;
    private Player replyTarget;
    private boolean toggledChat = false;

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
            OutputStream fos = new FileOutputStream(ignoreList, false);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter writer = new BufferedWriter(osw);
            for (UUID id : ignoring) writer.write(id.toString().concat("\n"));
            writer.close();
            osw.close();
            fos.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private List<UUID> loadIgnores() {
        File ignoreList = new File(manager.getIgnoresFolder(), player.getName().concat(".lst"));
        if (!ignoreList.exists()) return new ArrayList<>();
        try {
            InputStream fis = Files.newInputStream(ignoreList.toPath());
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isr);
            List<UUID> buffer = reader.lines().map(UUID::fromString).collect(Collectors.toList());
            reader.close();
            isr.close();
            fis.close();
            return buffer;
        } catch (Throwable t) {
            t.printStackTrace();
            return new ArrayList<>();
        }
    }
}
