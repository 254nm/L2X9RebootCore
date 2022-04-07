package me.l2x9.core.impl.patches.listeners.packetsize.encode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.RequiredArgsConstructor;
import me.l2x9.core.util.Utils;
import net.minecraft.server.v1_12_R1.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.entity.Player;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomPacketEncoder extends MessageToByteEncoder<Packet<?>> {
    private final Logger logger = LogManager.getLogger();
    private final Player player;

    @Override
    protected void encode(ChannelHandlerContext context, Packet<?> packet, ByteBuf buf) throws Exception {
        EnumProtocol protocol = context.channel().attr(NetworkManager.c).get();
        if (protocol == null) throw new RuntimeException("ConnectionProtocol unknown: " + packet.toString());
        Integer id = protocol.a(EnumProtocolDirection.CLIENTBOUND, packet);
        if (id == null) throw new IOException("Can't serialize unregistered packet");
        PacketDataSerializer dataSerializer = new PacketDataSerializer(buf);
        dataSerializer.d(id);
        try {
            packet.b(dataSerializer);
            int len = dataSerializer.readableBytes();
            if (len >= 2097152) {
                dataSerializer.clear();
                String longPacketFormat = "&aPrevented a large&r&3 %s &r&apacket with length of&r&3 %d/%d &r&afrom being sent to player&r&3 %s&r&a near &r&a%s&r";
                Utils.log(String.format(longPacketFormat, packet.getClass().getSimpleName(), len, 2097152, player.getName(), Utils.formatLocation(player.getLocation())));
            }
        } catch (Throwable throwable) {
            logger.error(throwable);
        }
    }
}

