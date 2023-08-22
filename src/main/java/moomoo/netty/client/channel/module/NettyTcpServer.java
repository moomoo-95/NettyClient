package moomoo.netty.client.channel.module;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import moomoo.netty.client.AppInstance;
import moomoo.netty.client.config.UserConfig;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
public class NettyTcpServer {
    private final NioEventLoopGroup serverGroup;
    private final NioEventLoopGroup clientGroup;

    private ServerBootstrap serverBootstrap;

    public NettyTcpServer() {
        // 로컬 포트로 바인딩된 서버 자체의 수신 소켓(ServerChannel)을 위한 EventLoopGroup
        serverGroup = new NioEventLoopGroup(1, new BasicThreadFactory.Builder()
                .namingPattern("TCP Server-%d")
                .daemon(true)
                .priority(Thread.MAX_PRIORITY)
                .build());
        // 서버가 수락한 클라이언트 연결마다 처리하기 위해 생성된 모든 Channel 을 위한 EventLoopGroup
        clientGroup = new NioEventLoopGroup(new BasicThreadFactory.Builder()
                .namingPattern("TCP Client-%d")
                .daemon(true)
                .priority(Thread.MAX_PRIORITY)
                .build());
    }

    public void run() {
        try {
            UserConfig userConfig = AppInstance.getInstance().getUserConfig();
            serverBootstrap = new ServerBootstrap();
            // 1) 서버부트스트랩에 두 개의 EventLoopGroup 지정
            serverBootstrap.group(serverGroup, clientGroup)
                    .channel(NioServerSocketChannel.class)
                    // send, receive 버퍼 크기 설정
                    .option(ChannelOption.SO_SNDBUF, userConfig.getNettyRecvBufSize())
                    .option(ChannelOption.SO_RCVBUF, userConfig.getNettyRecvBufSize())
                    // TIME_WAIT 상태의 포트에도 bind 가능
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
                    // 풀링
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) {
                            final ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new NettyInboundHandler());
                        }
                    });
        } catch (Exception e){
            log.error("NettyTcpServer.run.Exception", e);
        }
    }

    public void stop(){
        try {
            serverGroup.shutdownGracefully().sync();
            clientGroup.shutdownGracefully().sync();
            log.debug("NettyTcpServer close Complete");
        } catch (Exception e){
            log.error("NettyTcpServer.stop.Exception", e);
            Thread.currentThread().interrupt();
        }
    }

    public Channel openChannel(String ip, int port) {
        InetAddress inetAddress;
        final ChannelFuture channelFuture;

        try {
            inetAddress = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            log.warn("NettyTcpServer.openChannel.UnknownHostException ip={} {}", ip, e);
            return null;
        }

        try {
            channelFuture = serverBootstrap.bind(inetAddress, port).sync();
            return channelFuture.channel();
        } catch (InterruptedException e) {
            log.warn("NettyTcpServer.openChannel.InterruptedException socket={}:{} {}", ip, port, e);
            Thread.currentThread().interrupt();
            return null;
        }
    }

    public void closeChannel(Channel channel) {
        if (channel != null) {
            channel.closeFuture();
            channel.close();
        }
    }
}
