package moomoo.netty.client.channel.message.outgoing;

import lombok.Getter;
import moomoo.netty.client.channel.message.base.TcpMessage;
import moomoo.netty.client.channel.message.base.TcpMessageTypes;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
public class HbReq extends TcpMessage {

    public HbReq(String transactionId, String heartbeatTime) {
        super(TcpMessageTypes.TCP_MSG_STR_HB_REQ, transactionId);
        this.hbTime = heartbeatTime;
    }

    public HbReq(String heartbeatTime) {
        super(TcpMessageTypes.TCP_MSG_STR_HB_REQ, UUID.randomUUID().toString());
        this.hbTime = heartbeatTime;
    }

    public HbReq() {
        super(TcpMessageTypes.TCP_MSG_STR_HB_REQ, UUID.randomUUID().toString());
        this.hbTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
    }

    @NotBlank
    private final String hbTime;
}
