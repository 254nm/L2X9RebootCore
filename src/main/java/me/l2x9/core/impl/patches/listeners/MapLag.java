package me.l2x9.core.impl.patches.listeners;

import me.l2x9.core.L2X9RebootCore;
import me.l2x9.core.boiler.event.CustomEventHandler;
import me.l2x9.core.boiler.event.Listener;
import me.l2x9.core.boiler.event.events.PacketEvent;
import me.l2x9.core.impl.patches.PatchManager;
import me.l2x9.core.boiler.util.Utils;
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

public class MapLag implements Listener {
    private final World world = ((CraftWorld) Bukkit.getServer().getWorlds().get(0)).getHandle();
    private final PatchManager manager;
    private Field iconsF;
    private Field idF;

    public MapLag(PatchManager manager) {
        this.manager = manager;
        try {
            iconsF = PacketPlayOutMap.class.getDeclaredField("d");
            iconsF.setAccessible(true);
            idF = PacketPlayOutMap.class.getDeclaredField("a");
            idF.setAccessible(true);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @CustomEventHandler
    public void onPacket(PacketEvent.Outgoing event) {
        if (event.getPacket() instanceof PacketPlayOutMap) {
            try {
                PacketPlayOutMap packet = (PacketPlayOutMap) event.getPacket();
                MapIcon[] icons = (MapIcon[]) iconsF.get(packet);
                int id = idF.getInt(packet);
                Bukkit.getScheduler().runTask(L2X9RebootCore.getInstance(), () -> {
                    WorldMap map = (WorldMap) world.a(WorldMap.class, "map_" + id);
                    if (map == null) return;
                    MapView view = map.mapView;
                    if (icons.length > 35) {
                        event.setCancelled(true);
                        if (view.getRenderers().get(0) instanceof DeleteRender) return;
                        view.removeRenderer(view.getRenderers().get(0));
                        view.addRenderer(new DeleteRender(manager));
                        Utils.log("&3Added delete renderer to map&r&a " + id);
                    }
                });
            } catch (Throwable t) {
                t.printStackTrace();
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
            mapCanvas.drawImage(0, 0, manager.getMapImage());
        }
    }
}
