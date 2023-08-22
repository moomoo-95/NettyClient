package moomoo.netty.client.channel.module;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import moomoo.netty.client.channel.NettyChannelManager;

@Slf4j
public class NettyInboundHandler extends ChannelInboundHandlerAdapter {
    private static final NettyChannelManager nettyChannelManager = NettyChannelManager.getInstance();

    public NettyInboundHandler() {
        // nothing
    }

    /**
     * @fn public void channelActive
     * @brief Channel 의 연결과 바인딩이 완료되어 활성화 될 때 호출되는 메서드
     * @param channelHandlerContext
     */
    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) {
        Channel channel = channelHandlerContext.channel();
        nettyChannelManager.putClientChannel(channel);
    }

    /**
     * @fn public void channelInactive
     * @brief Channel 이 활성 상태에서 벗어나 로컬 피어에 대한 연결이 해제되었을 때 호출되는 메서드
     * @param channelHandlerContext
     */
    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) {
        Channel channel = channelHandlerContext.channel();
        nettyChannelManager.removeClientChannel(channel.id().toString());
    }

    /**
     * @fn protected void channelRead
     * @brief 메시지가 들어올 때마다 호출되는 메서드
     * @param channelHandlerContext 인바운드된 Context
     * @param message 인바운드된 메시지
     */
    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object message){
        try {
            ByteBuf byteBuf = (ByteBuf) message;
            byte[] data = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(data);
            Channel channel = channelHandlerContext.channel();
            TcpInfo tcpInfo = nettyChannelManager.getClientChannel(channel.id().toString());
            if(tcpInfo != null){
                TcpMsgConsumer.handle(data, tcpInfo);
            } else {
                log.warn("[TCP MSG] NTC->NTS-[{}] [{}]\n{}", channel.id().toString(), "Do not exist tcpInfo.", data);
            }
        } catch (Exception e) {
            log.error("NettyServerHandler.channelRead0.Exception ", e);
        }
    }

    /**
     * @fn public void exceptionCaught
     * @brief 예외사항 발생시 예외스택을 출력하고 채널을 닫는 메서드
     * @param channelHandlerContext 채널
     * @param cause 예외사항
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause){
        log.error("NettyClientHandler.exceptionCaught.Exception ", cause);
        channelHandlerContext.close();
    }

}