package me.l2x9.core.misc;

import me.l2x9.core.L2X9RebootCore;
import me.l2x9.core.Manager;
import me.l2x9.core.misc.epc.EntityCheckTask;
import me.l2x9.core.misc.epc.EntitySpawnListener;
import me.l2x9.core.misc.listeners.*;
import me.l2x9.core.util.Utils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MiscManager extends Manager {
    private ConfigurationSection config;
    private HashMap<EntityType, Integer> entityPerChunk;
    public MiscManager() {
        super("Misc");
    }

    @Override
    public void init(L2X9RebootCore plugin) {
        config = plugin.getModuleConfig(this);
        entityPerChunk = parseEntityConf();
        plugin.getExecutorService().scheduleAtFixedRate(new EntityCheckTask(entityPerChunk), 0, config.getInt("EntityPerChunk.CheckInterval"), TimeUnit.MINUTES);
        plugin.registerListener(new EntitySpawnListener(entityPerChunk));
        plugin.registerListener(new OldSchoolKill());
        plugin.registerListener(new MoveListener());
        plugin.registerListener(new JoinMessages());
        new AutoRestart();
    }

    private HashMap<EntityType, Integer> parseEntityConf() {
        List<String> raw = config.getStringList("EntityPerChunk.EntitiesPerChunk");
        HashMap<EntityType, Integer> buf = new HashMap<>();
        for (String str : raw) {
            String[] split = str.split("::");
            try {
                EntityType type = EntityType.valueOf(split[0].toUpperCase());
                int i = Integer.parseInt(split[1]);
                buf.put(type,i);
            } catch (EnumConstantNotPresentException | NumberFormatException e) {
                if (e instanceof NumberFormatException) {
                    Utils.log("&a%s&r&c is not a number", split[1]);
                    continue;
                }
                Utils.log("&cUnknown EntityType&r&a %s", split[0]);
            }
        }
        return buf;
    }

    @Override
    public void destruct(L2X9RebootCore plugin) {

    }

    @Override
    public void reloadConfig(ConfigurationSection config) {

    }
}
