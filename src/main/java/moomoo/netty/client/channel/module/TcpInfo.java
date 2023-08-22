package moomoo.netty.client.channel.module;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import moomoo.netty.client.channel.message.base.TcpMessage;
import moomoo.netty.client.util.NettyUtil;

@Slf4j
public class TcpInfo {
    @Getter
    private final String channelId;
    private final Channel channel;
    @Getter
    private final String address;

    public TcpInfo(@NonNull Channel channel) {
        this.channel = channel;
        this.channelId = channel.id().toString();
        this.address = channel.remoteAddress().toString().substring(1);
    }

    public void sendMessage(@NonNull TcpMessage msg) {
        String json = msg.toJson();
        String msgType = msg.getMsgType();
        log.info("[TCP MSG] NTC->NTS-[{}] [{}]\n{}", channelId, msgType, json);
        this.sendMessage(msg.toJson().getBytes());
    }

    private void sendMessage(byte[] data) {
        ByteBuf byteBuf = NettyUtil.createPooledHeapByteBuf(data.length);
        byteBuf.writeBytes(data);
        try {
            channel.writeAndFlush(byteBuf);
        } catch (Exception e) {
            log.error("TcpInfo.sendMessage ", e);
        }
    }

    public boolean isActive() {
        return channel.isActive();
    }

    public void channelClose(){
        if (channel != null) {
            channel.closeFuture();
            channel.close();
            log.info("{} Channel is disconnected.", getLogPrefix());
        } else {
            log.warn("{} Channel is not found.", getLogPrefix());
        }
    }

    public String getLogPrefix() {
        return "("+channelId+") ("+address+")";
    }
}
