package me.l2x9.core.impl.patches.listeners.packetsize.encode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import me.l2x9.core.util.Utils;
import net.minecraft.server.v1_12_R1.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class CustomPacketEncoder extends MessageToByteEncoder<Packet<?>> {
    private static final Logger logger = LogManager.getLogger();
    private final EnumProtocolDirection direction;
    private final int MAX_LEN = 2097152;

    public CustomPacketEncoder(EnumProtocolDirection var1) {
        this.direction = var1;
    }

    @Override
    protected void encode(ChannelHandlerContext context, Packet<?> packet, ByteBuf buf) throws Exception {
        EnumProtocol protocol = context.channel().attr(NetworkManager.c).get();
        if (protocol == null) throw new RuntimeException("ConnectionProtocol unknown: " + packet.toString());
        Integer id = protocol.a(direction, packet);
        if (id == null) throw new IOException("Can't serialize unregistered packet");
        PacketDataSerializer dataSerializer = new PacketDataSerializer(buf);
        dataSerializer.d(id);
        try {
            packet.b(dataSerializer);
            int len = dataSerializer.readableBytes();
            if (len >= MAX_LEN) {
                dataSerializer.clear();
                Utils.log(String.format("&aPrevented an extremely large&r&3 %s &r&apacket with length of&r&3 %d/%d &r&afrom being sent&r", packet.getClass().getName(), len, MAX_LEN));
            }
        } catch (Throwable throwable) {
            logger.error(throwable);
        }
    }
}

