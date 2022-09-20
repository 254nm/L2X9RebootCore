package me.l2x9.core.mixin;

import me.txmc.rtmixin.RtMixin;
import me.txmc.rtmixin.Utils;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.instrument.Instrumentation;
import java.util.jar.JarFile;

public class MixinMain {

    public void init(JavaPlugin plugin) throws Throwable {
        plugin.getLogger().info(translate("&3Initializing mixins"));
        Instrumentation inst = RtMixin.attachAgent().orElseThrow(() -> new RuntimeException("Failed to attach agent"));
        inst.appendToSystemClassLoaderSearch(new JarFile(Utils.getSelf(MixinMain.class)));
        plugin.getLogger().info(translate("&3Successfully attached agent and got instrumentation instance&r&a %s&r", inst.getClass().getName()));
        long start = System.currentTimeMillis();
        //Register your mixins here
//        RtMixin.processMixins(class);
        //---
        plugin.getLogger().info(translate("&3Preformed all mixins in&r&a %dms&r", (System.currentTimeMillis() - start)));
    }

    private String translate(String message, Object... args) {
        return ChatColor.translateAlternateColorCodes('&', String.format(message, args));
    }
}
