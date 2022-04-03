package me.l2x9.core.impl.patches;

import lombok.Getter;
import me.l2x9.core.L2X9RebootCore;
import me.l2x9.core.Manager;
import me.l2x9.core.impl.patches.listeners.*;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.configuration.ConfigurationSection;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

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
        plugin.registerListener(new DispenserCrash());
        plugin.registerListener(new BoatFly());
        plugin.registerListener(new ElytraSpeedLimit());
        plugin.registerListener(new LeverRateLimit());
        plugin.registerListener(new CraftingLag(this));
        plugin.registerListener(new PacketFly(), PacketPlayInFlying.class, PacketPlayInFlying.PacketPlayInPosition.class, PacketPlayInFlying.PacketPlayInPositionLook.class);
        plugin.registerListener(new MapLag(this), PacketPlayOutMap.class);
        plugin.registerListener(new NoCom(), PacketPlayOutBlockChange.class, PacketPlayInBlockDig.class);
        plugin.registerListener(new Damage());
        plugin.registerListener(new ChunkBan(), PacketPlayOutMapChunk.class);
        plugin.registerListener(new ProjectileVelocity());
        plugin.registerListener(new PacketPerSecondLimit(), null); //null means that this listener receives every packet
        plugin.registerListener(new Redstone());
        plugin.registerListener(new IllegalBlock(plugin.getConfig()));
        WitherLag lag = new WitherLag();
        plugin.registerListener(lag);
        new LightLag();
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
            if (!file.exists()) {
                InputStream stream = getClass().getClassLoader().getResourceAsStream("Map.png");
                if (stream == null) return null;
                Files.copy(stream, file.toPath());
            }
            return ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
