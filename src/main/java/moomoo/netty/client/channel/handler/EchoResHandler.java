package moomoo.netty.client.channel.handler;

import lombok.extern.slf4j.Slf4j;
import moomoo.netty.client.channel.handler.base.TcpIncomingMsgHandler;
import moomoo.netty.client.channel.message.incoming.EchoRes;


@Slf4j
public class EchoResHandler extends TcpIncomingMsgHandler<EchoRes> {

    @Override
    public void handle() {
        log.info("{} Received ok. [{}]\n{}", tcpInfo.getLogPrefix(), msgType, msgBody);
        onSuccess();
    }

    @Override
    protected void onFail(String reason) {

    }

    @Override
    protected void onSuccess() {
    }
}
