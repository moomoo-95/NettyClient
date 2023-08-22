package moomoo.netty.client.channel.message.outgoing;

import lombok.Getter;
import moomoo.netty.client.channel.message.base.TcpMessage;
import moomoo.netty.client.channel.message.base.TcpMessageTypes;

import javax.validation.constraints.NotBlank;

@Getter
public class EchoReq extends TcpMessage {

    public EchoReq(String transactionId, String text) {
        super(TcpMessageTypes.TCP_MSG_STR_ECHO_REQ, transactionId);
        this.text = text;
    }

    @NotBlank
    private final String text;
}