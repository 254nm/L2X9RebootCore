package me.l2x9.core.impl.patches.listeners;

import lombok.RequiredArgsConstructor;
import me.l2x9.core.impl.patches.PatchManager;
import me.txmc.protocolapi.PacketEvent;
import me.txmc.protocolapi.PacketListener;
import me.txmc.protocolapi.reflection.GetField;
import me.l2x9.core.util.Utils;
import net.minecraft.server.v1_12_R1.MapIcon;
import net.minecraft.server.v1_12_R1.PacketPlayOutMap;
import net.minecraft.server.v1_12_R1.World;
import net.minecraft.server.v1_12_R1.WorldMap;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapCursor;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.lang.reflect.Field;
import java.util.List;

@RequiredArgsConstructor
public class MapLag implements PacketListener {
    private final World world = ((CraftWorld) Bukkit.getServer().getWorlds().get(0)).getHandle();
    private final PatchManager manager;

    @GetField(clazz = PacketPlayOutMap.class, name = "d")
    private Field iconsF;
    @GetField(clazz = PacketPlayOutMap.class, name = "a")
    private Field idF;

    @Override
    public void incoming(PacketEvent.Incoming event) throws Throwable {
    }

    @Override
    public void outgoing(PacketEvent.Outgoing event) throws Throwable {
        if (event.getPacket() instanceof PacketPlayOutMap) {
            PacketPlayOutMap packet = (PacketPlayOutMap) event.getPacket();
            MapIcon[] icons = (MapIcon[]) iconsF.get(packet);
            int id = idF.getInt(packet);
            if (icons.length > 35) {
                event.setCancelled(true);
                Utils.run(() -> {
                    WorldMap map = (WorldMap) world.a(WorldMap.class, "map_" + id);
                    if (map == null) return;
                    MapView view = map.mapView;
                    if (view.getRenderers().get(0) instanceof DeleteRender) return;
                    view.removeRenderer(view.getRenderers().get(0));
                    view.addRenderer(new DeleteRender(manager));
                    Utils.log("&3Added delete renderer to map&r&a " + id);
                });
            }
        }
    }

    private static class DeleteRender extends MapRenderer {
        private final PatchManager manager;
        private Field cursorsF;

        private DeleteRender(PatchManager manager) {
            this.manager = manager;
            try {
                cursorsF = org.bukkit.map.MapCursorCollection.class.getDeclaredField("cursors");
                cursorsF.setAccessible(true);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        @Override
        public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
            try {
                List<MapCursor> cursors = (List<MapCursor>) cursorsF.get(mapCanvas.getCursors());
                cursors.forEach(c -> mapCanvas.getCursors().removeCursor(c));
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            mapCanvas.drawImage(0, 0, manager.getImage());
        }
    }
}
