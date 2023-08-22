package moomoo.netty.client.channel.module;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import moomoo.netty.client.channel.handler.EchoResHandler;
import moomoo.netty.client.channel.handler.HbResHandler;
import moomoo.netty.client.channel.message.base.TcpMessage;

import java.nio.charset.Charset;

import static moomoo.netty.client.channel.message.base.TcpMessageTypes.TCP_MSG_STR_ECHO_RES;
import static moomoo.netty.client.channel.message.base.TcpMessageTypes.TCP_MSG_STR_HB_RES;

@Slf4j
public class TcpMsgConsumer {
    public static final Gson gson = new Gson();

    private TcpMsgConsumer() {
    }

    public static void handle(byte[] data, TcpInfo tcpInfo) {
        String json = new String(data, Charset.defaultCharset()).trim();

        TcpMessage tcpMessage = gson.fromJson(json, TcpMessage.class);
        String msgType = tcpMessage.getMsgType();
        log.info("[TCP MSG] NTC<-NTS-[{}] [{}]\n{}", tcpInfo.getAddress(), msgType, json);
        switch (msgType) {
            case TCP_MSG_STR_HB_RES:
                new HbResHandler().proc(tcpMessage, json, tcpInfo);
                break;
            case TCP_MSG_STR_ECHO_RES:
                new EchoResHandler().proc(tcpMessage, json, tcpInfo);
                break;
            default:
                log.warn("Undefined Message Recv : [{}]", json);
        }
    }


}