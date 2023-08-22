package moomoo.netty.client.channel.message.incoming;

import lombok.Getter;
import moomoo.netty.client.channel.message.base.TcpMessage;
import moomoo.netty.client.channel.message.base.TcpMessageTypes;

import javax.validation.constraints.NotBlank;

@Getter
public class EchoRes extends TcpMessage {

    public EchoRes(String transactionId, String text) {
        super(TcpMessageTypes.TCP_MSG_STR_ECHO_RES, transactionId);
        this.text = text;
    }

    @NotBlank
    private final String text;
}