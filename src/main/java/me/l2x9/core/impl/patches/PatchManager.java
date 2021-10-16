package me.l2x9.core.impl.patches;

import me.l2x9.core.L2X9RebootCore;
import me.l2x9.core.Manager;
import me.l2x9.core.boiler.util.ConfigCreator;
import me.l2x9.core.impl.patches.listeners.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class PatchManager extends Manager {
    private Image image;

    public PatchManager() {
        super("Patches");
        InputStream stream = getClass().getClassLoader().getResourceAsStream("Map.png");
        if (stream == null) return;
        try {
            File file = new File(L2X9RebootCore.getInstance().getDataFolder(), "Map.png");
            if (!file.exists()) Files.copy(stream, file.toPath());
            image = ImageIO.read(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Image getMapImage() {
        return image;
    }

    @Override
    public void init(L2X9RebootCore plugin) {
        plugin.registerListener(new DispenserCrash());
        plugin.registerListener(new BoatFly());
        plugin.registerListener(new ElytraSpeedLimit());
        plugin.registerListener(new LeverRateLimit());
        plugin.registerListener(new CraftingLag());
        plugin.registerListener(new PacketFly());
        plugin.registerListener(new MapLag(this));
        plugin.registerListener(new NoCom());
        plugin.registerListener(new Damage());
        WitherLag lag = new WitherLag();
        plugin.registerListener(lag);
    }

    @Override
    public void destruct(L2X9RebootCore plugin) {

    }

    @Override
    public void reloadConfig(ConfigCreator creator) {

    }
}
