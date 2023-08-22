package moomoo.netty.client.channel.message.incoming;

import lombok.Getter;
import moomoo.netty.client.channel.message.base.TcpMessage;
import moomoo.netty.client.channel.message.base.TcpMessageTypes;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
public class HbRes extends TcpMessage {

    public HbRes(String transactionId, String heartbeatTime) {
        super(TcpMessageTypes.TCP_MSG_STR_HB_RES, transactionId);
        this.hbTime = heartbeatTime;
    }

    public HbRes(String heartbeatTime) {
        super(TcpMessageTypes.TCP_MSG_STR_HB_RES, UUID.randomUUID().toString());
        this.hbTime = heartbeatTime;
    }

    public HbRes() {
        super(TcpMessageTypes.TCP_MSG_STR_HB_RES, UUID.randomUUID().toString());
        this.hbTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
    }

    @NotBlank
    private final String hbTime;
}
