package moomoo.netty.client.channel;


import io.netty.channel.Channel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import moomoo.netty.client.channel.module.NettyTcpServer;
import moomoo.netty.client.channel.module.TcpInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Getter
public class NettyChannelManager {
    private static final class Singleton { private static final NettyChannelManager INSTANCE = new NettyChannelManager(); }

    private final NettyTcpServer nettyTcpServer = new NettyTcpServer();
    private Channel serverChannel = null;
    private Map<String, TcpInfo> tcpInfoMap = new ConcurrentHashMap<>();

    private NettyChannelManager() { /* nothing */ }

    public static NettyChannelManager getInstance(){
        return Singleton.INSTANCE;
    }

    public void startServer(String ip, int port){
        nettyTcpServer.run();
        openServerChannel(ip, port);
    }

    public void stopServer(){
        closeChannel();
        nettyTcpServer.stop();
    }

    private void openServerChannel(String ip, int port){
        try {
            serverChannel = nettyTcpServer.openChannel(ip, port);
            log.debug("Open server channel complete : {}", serverChannel);
        } catch (Exception e){
            log.error("NettyChannelManager.openChannel.Exception ", e);
        }

    }

    private void closeServerChannel(){
        try {
            nettyTcpServer.closeChannel(serverChannel);
            log.debug("Close server channel complete : {}", serverChannel);
        } catch (Exception e) {
            log.error("NettyChannelManager.closeChannel.Exception ", e);
        }
    }

    public void putClientChannel(Channel channel){
        TcpInfo tcpInfo = getClientChannel(channel.id().toString());
        if(tcpInfo != null){
            log.warn("{} Channel is Already exist.", tcpInfo.getLogPrefix());
        } else {
            tcpInfo = new TcpInfo(channel);
            tcpInfoMap.put(channel.id().toString(), tcpInfo);
            log.info("{} Channel is connected.", tcpInfo.getLogPrefix());
        }
    }

    public void removeClientChannel(String channelId){
        TcpInfo tcpInfo = tcpInfoMap.remove(channelId);
        tcpInfo.channelClose();
    }

    public TcpInfo getClientChannel(String channelId){
        return tcpInfoMap.get(channelId);
    }

    public void removeAllClientChannel() {
        tcpInfoMap.forEach((channelId, channel) -> removeClientChannel(channelId));
    }

    private void closeChannel() {
        removeAllClientChannel();
        closeServerChannel();
    }
}
