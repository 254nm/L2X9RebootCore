package me.l2x9.core.impl.chat;

import org.bukkit.entity.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public List<UUID> getIgnoring() {
        return ignoring;
    }

    public Player getReplyTarget() {
        return replyTarget;
    }

    public void setReplyTarget(Player replyTarget) {
        this.replyTarget = replyTarget;
    }

    public boolean isToggledChat() {
        return toggledChat;
    }

    public void setToggledChat(boolean toggledChat) {
        this.toggledChat = toggledChat;
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
            if (!ignoreList.exists()) ignoreList.createNewFile();
            OutputStream fos = new FileOutputStream(ignoreList);
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
        List<UUID> buffer = new ArrayList<>();
        try {
            InputStream fis = new FileInputStream(ignoreList);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isr);
            reader.lines().forEach(l -> buffer.add(UUID.fromString(l)));
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
