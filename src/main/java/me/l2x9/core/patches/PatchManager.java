package me.l2x9.core.patches;

import lombok.Getter;
import me.l2x9.core.L2X9RebootCore;
import me.l2x9.core.Manager;
import me.l2x9.core.patches.listeners.*;
import me.l2x9.core.patches.listeners.packetsize.PreLoginListener;
import me.l2x9.core.util.Utils;
import me.txmc.protocolapi.PacketListener;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;

public class PatchManager extends Manager {
    @Getter
    private Image image;
    private L2X9RebootCore plugin;
    @Getter
    private ConfigurationSection config;

    public PatchManager() {
        super("Patches");
    }

    @Override
    public void init(L2X9RebootCore plugin) {
        this.plugin = plugin;
        image = loadImage();
        config = plugin.getModuleConfig(this);
//        plugin.registerListener(new PreLoginListener());
        plugin.registerListener(new BoatFly());
        plugin.registerListener(new Damage());
        plugin.registerListener(new DispenserCrash());
        plugin.registerListener(new ElytraSpeedLimit());
        plugin.registerListener(new EndGateway());
        plugin.registerListener(new EntityCollideListener());
        registerConfigurable(FakePlugins.class, PacketPlayInTabComplete.class);
//        plugin.registerListener(new LargePacketListener());
        plugin.registerListener(new LeverRateLimit());
        plugin.registerListener(new MapLag(this), PacketPlayOutMap.class);
        plugin.registerListener(new NoCom(), PacketPlayOutBlockChange.class, PacketPlayInBlockDig.class);
        plugin.registerListener(new PacketFly(), PacketPlayInFlying.class, PacketPlayInFlying.PacketPlayInPosition.class, PacketPlayInFlying.PacketPlayInPositionLook.class);
        plugin.registerListener(new PacketPerSecondLimit(), (Class<? extends Packet<?>>) null); //null means that this listener receives every packet
        plugin.registerListener(new ProjectileCrash());
        plugin.registerListener(new ProjectileVelocity());
        plugin.registerListener(new Redstone());
    }

    @SafeVarargs
    private void registerConfigurable(Class<? extends PacketListener> listener, Class<? extends Packet<?>>... listeningFor) {
        try {
            ConfigurationSection patchSection = config.getConfigurationSection(listener.getSimpleName());
            if (patchSection == null) throw new IllegalArgumentException("Missing configuration section for " + listener.getName());
            Constructor<? extends PacketListener> constructor = listener.getConstructor(ConfigurationSection.class, PatchManager.class);
            constructor.setAccessible(true);
            PacketListener packetListener = constructor.newInstance(patchSection, this);
            plugin.registerListener(packetListener, listeningFor);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
    private void registerConfigurable(Class<? extends Listener> listener) {
        try {
            ConfigurationSection patchSection = config.getConfigurationSection(listener.getSimpleName());
            if (patchSection == null) throw new IllegalArgumentException("Missing configuration section for " + listener.getName());
            Constructor<? extends Listener> constructor = listener.getConstructor(ConfigurationSection.class, PatchManager.class);
            constructor.setAccessible(true);
            Listener packetListener = constructor.newInstance(patchSection, this);
            plugin.registerListener(packetListener);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public void destruct(L2X9RebootCore plugin) {

    }

    @Override
    public void reloadConfig(ConfigurationSection config) {
        this.config = config;
    }

    private Image loadImage() {
        try {
            File file = new File(plugin.getDataFolder(), "Map.png");
            if (!file.exists()) Utils.unpackResource("Map.png", file);
            return ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
