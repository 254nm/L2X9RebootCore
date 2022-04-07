package me.l2x9.core.impl.patches.listeners.packetsize.compress;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;
import me.l2x9.core.util.Utils;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.zip.Inflater;

public class CustomPacketDecompressor extends ByteToMessageDecoder {
    private final Inflater inflater;
    private int threshold;
    private final Player player;

    public CustomPacketDecompressor(int threshold, Player player) {
        this.threshold = threshold;
        this.inflater = new Inflater();
        this.player = player;
    }

    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> list) throws Exception {
        if (buf.readableBytes() != 0) {
            PacketDataSerializer pds = new PacketDataSerializer(buf);
            int totalLength = pds.g(); //Read the first VarInt which should be length of Data length + compressed length of (Packet ID + Data)
            if (totalLength >= 1927951) {
                pds.clear();
                Utils.log(String.format("&aPrevented a large packet with length of&r&3 %d/%d &r&afrom player %s near %s&r", totalLength, 1927951, player.getName(), Utils.formatLocation(player.getLocation())));
                return;
            }
            if (totalLength == 0) {
                list.add(pds.readBytes(pds.readableBytes()));
                return;
            }
            if (totalLength < threshold) throw new DecoderException("Badly compressed packet - size of " + totalLength + " is below server threshold of " + threshold);

            byte[] compressed = new byte[pds.readableBytes()];
            pds.readBytes(compressed);
            inflater.setInput(compressed);
            byte[] deCompressed = new byte[totalLength];
            inflater.inflate(deCompressed);
            list.add(Unpooled.wrappedBuffer(deCompressed));
            inflater.reset();

        }
    }

    public void a(int threshold) {
        this.threshold = threshold;
    }
}

