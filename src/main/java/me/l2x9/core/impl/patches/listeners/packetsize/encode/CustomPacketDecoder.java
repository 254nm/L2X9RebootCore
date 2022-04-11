package me.l2x9.core.impl.patches.listeners.packetsize.encode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.AllArgsConstructor;
import me.l2x9.core.event.LargePacketEvent;
import me.l2x9.core.util.Utils;
import net.minecraft.server.v1_12_R1.EnumProtocolDirection;
import net.minecraft.server.v1_12_R1.NetworkManager;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
public class CustomPacketDecoder extends ByteToMessageDecoder {

    private final EnumProtocolDirection direction;
    private final Player player;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> list) throws Exception {
        if (buf.readableBytes() != 0) {
            PacketDataSerializer pds = new PacketDataSerializer(buf);
            int id = pds.g(); //Read the second VarInt in the packet which should be the ID
            Packet<?> packet = ctx.channel().attr(NetworkManager.c).get().a(direction, id);
            if (packet == null) throw new IOException("Bad packet id " + id);
            if (pds.readableBytes() >= 262144) {
                String longPacketFormat = "&aPrevented a large&r&3 %s &r&apacket with length of&r&3 %d/%d &r&afrom being sent by player&r&3 %s&r&a near &r&a%s&r";
                Utils.log(String.format(longPacketFormat, packet.getClass().getSimpleName(), pds.readableBytes(), 100000, player.getName(), Utils.formatLocation(player.getLocation())));
                LargePacketEvent.Incoming incoming = new LargePacketEvent.Incoming(player, packet, pds, pds.readableBytes());
                Bukkit.getServer().getPluginManager().callEvent(incoming);
                if (incoming.isCancelled()) {
                    pds.clear();
                    return;
                }
            }
            packet.a(pds);
            if (pds.readableBytes() > 0)
                throw new IOException("Packet " + ctx.channel().attr(NetworkManager.c).get().a() + "/" + id + " (" + packet.getClass().getSimpleName() + ") was larger than I expected, found " + pds.readableBytes() + " bytes extra whilst reading packet " + id);
            list.add(packet);
        }
    }
}