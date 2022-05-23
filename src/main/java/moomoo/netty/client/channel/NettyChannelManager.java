package moomoo.netty.client.channel;


import moomoo.netty.client.channel.module.TcpClientChannel;

public class NettyChannelManager {

    private TcpClientChannel tcpClientChannel = null;

    private static class SingleTon {
        public static final NettyChannelManager INSTANCE = new NettyChannelManager();
    }

    public NettyChannelManager() {
        //
    }

    public static NettyChannelManager getInstance() {
        return SingleTon.INSTANCE;
    }

    public void startTcpClientChannel(String ip, int port) {
        tcpClientChannel = new TcpClientChannel("TCP-CLIENT-CHANNEL-01", ip, port);
        tcpClientChannel.start();
        tcpClientChannel.openChannel();
    }

    public void stopTcpClientChannel() {
        tcpClientChannel.closeChannel();
        tcpClientChannel.stop();
    }

    public TcpClientChannel getTcpClientChannel() {
        return tcpClientChannel;
    }
}
