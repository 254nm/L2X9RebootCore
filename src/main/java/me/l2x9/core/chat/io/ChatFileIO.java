package me.l2x9.core.chat.io;

import com.google.gson.*;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import me.l2x9.core.IStorage;
import me.l2x9.core.chat.ChatInfo;
import me.l2x9.core.chat.ChatManager;
import me.l2x9.core.util.Utils;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.HashSet;
import java.util.UUID;

/**
 * @author 254n_m
 * @since 2023/03/04 1:41 AM
 * This file was created as a part of L2X9RebootCore
 */
@RequiredArgsConstructor
public class ChatFileIO implements IStorage<ChatInfo, Player> {
    private final File dataDir;
    private final ChatManager cm;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void save(ChatInfo chatInfo, Player player) {
        File file = new File(dataDir, player.getUniqueId().toString().concat(".json"));

        if (chatInfo.shouldNotSave()) {
            if (file.exists()) delete(player);
            return;
        }

        try {
            JsonObject obj = new JsonObject();
            obj.addProperty("togglechat", chatInfo.isToggledChat());
            obj.addProperty("togglejoinmessages", chatInfo.isJoinMessages());
            obj.addProperty("autotranslate", chatInfo.isAutoTranslate());
            JsonArray arr = new JsonArray();
            chatInfo.getIgnoring().forEach(u -> arr.add(u.toString()));
            obj.add("ignores", arr);

            @Cleanup FileWriter fw = new FileWriter(file, false);
            gson.toJson(obj, fw);
        } catch (Throwable t) {
            Utils.log("Failed to save ChatInfo for&r&a %s&r&c. Please see the stacktrace below for more info", player.getUniqueId());
            t.printStackTrace();
        }
    }

    @Override
    public ChatInfo load(Player player) {
        try {
            File file = new File(dataDir, player.getUniqueId().toString().concat(".json"));
            if (file.exists()) {

                @Cleanup FileReader reader = new FileReader(file);
                JsonObject obj = gson.fromJson(reader, JsonObject.class);

                boolean toggleChat = obj.has("togglechat") && obj.get("togglechat").getAsBoolean();
                boolean toggleJoinMessages = obj.has("togglejoinmessages") && obj.get("togglejoinmessages").getAsBoolean();
                boolean autoTranslate = obj.has("autotranslate") && obj.get("autotranslate").getAsBoolean();
                HashSet<UUID> ignores = (obj.has("ignores")) ? parse(obj.get("ignores").getAsJsonArray()) : new HashSet<>();

                return new ChatInfo(player, cm, ignores, toggleChat, toggleJoinMessages, autoTranslate);
            } else return new ChatInfo(player, cm);
        } catch (Throwable t) {
            Utils.log("&cFailed to parse&r&a %s&r&c. This is most likely due to malformed json", player.getUniqueId());
            t.printStackTrace();
            return null;
        }
    }

    private HashSet<UUID> parse(JsonArray arr) {
        HashSet<UUID> buf = new HashSet<>();
        arr.forEach(u -> buf.add(UUID.fromString(u.getAsString())));
        return buf;
    }

    @Override
    public void delete(Player player) {
        File file = new File(dataDir, player.getUniqueId().toString().concat(".json"));
        file.delete();
        Utils.log("&3Deleted ChatInfo file for&r&a %s&r", player.getName());
    }
}
