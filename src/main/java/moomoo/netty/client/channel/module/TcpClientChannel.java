package moomoo.netty.client.channel.module;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import moomoo.netty.client.AppInstance;
import moomoo.netty.client.channel.handler.TcpChannelHandler;
import moomoo.netty.client.config.DefaultConfig;
import moomoo.netty.client.util.NettyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

/**
 * @class public class TcpNettyChannel
 * @brief 클라이언트가 수신할 포트 바인딩, 연결 요청 수락, Channel 구성을 담당하는 매니저 클래스
 */
public class TcpClientChannel {

    private static final Logger logger = LoggerFactory.getLogger(TcpClientChannel.class);

    private static final int THREAD_POOL_SIZE = 3;

    private EventLoopGroup eventLoopGroup;
    private Bootstrap b;

    private Channel channel;

    private final String channelId;
    private final String targetIp;
    private final int targetPort;

    public TcpClientChannel(String channelId, String ip, int port) {
        this.channelId = channelId;
        this.targetIp = ip;
        this.targetPort = port;
    }

    public void start() {
        DefaultConfig config = AppInstance.getInstance().getDefaultConfig();

        eventLoopGroup = new NioEventLoopGroup(THREAD_POOL_SIZE);
        b = new Bootstrap();
        b.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_RCVBUF, config.getNettyRecvBufSize())
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        socketChannel.pipeline().addLast(new TcpChannelHandler(channelId, targetIp, targetPort));
                    }
                });
    }

    /**
     * Netty Channel 을 종료하는 함수
     */
    public void stop () {
        eventLoopGroup.shutdownGracefully();
    }

    /**
     * Netty Server Channel 을 생성하는 함수
     */
    public Channel openChannel () {
        if (channel != null) {
            logger.warn("({}) ({}:{}) Channel is already opened.", channelId, targetIp, targetPort);
            return null;
        }

        InetAddress address;
        ChannelFuture channelFuture;

        try {
            address = InetAddress.getByName(targetIp);
        } catch (UnknownHostException e) {
            logger.warn("UnknownHostException is occurred. (ip={})", targetIp, e);
            return null;
        }

        try {
            channelFuture = b.connect(address, targetPort).sync();
            channel = channelFuture.channel();
            logger.debug("({}) ({}:{}) Channel is opened.", channelId, targetIp, targetPort);

            return channel;
        } catch (Exception e) {
            logger.error("({}) ({}:{}) Channel is interrupted. ", channelId, targetIp, targetPort, e);
            Thread.currentThread().interrupt();
            return channel;
        }
    }

    public void closeChannel() {
        if (channel == null) {
            logger.warn("Channel is already closed.");
            return;
        }

        channel.close();
        channel = null;
        logger.debug("Channel is closed.");
    }

    ////////////////////////////////////////////////////////////////////////////////

    public void sendMessage(String message) {
        closeChannel();
        byte[] bytesMsg = message.getBytes(StandardCharsets.UTF_8);
        ByteBuf byteBuf = NettyUtil.createPooledHeapByteBuf(16);
        byteBuf.writeBytes(bytesMsg);
        openChannel();
        if (channel != null) {
            try {
                channel.writeAndFlush(byteBuf);
                String logMessage = byteBuf.toString(StandardCharsets.UTF_8);
                logger.debug("({}) ({}:{}) Send the request. [\n{}\n]", channelId, targetIp, targetPort, logMessage);
            } catch (Exception e) {
                logger.error("TcpClientChannel.sendMessage.Exception ", e);
            }

        } else {
            logger.warn("({}) ({}:{}) Channel is null. Fail to send the request. ({})", channelId, targetIp, targetPort, message);
        }
    }
}
