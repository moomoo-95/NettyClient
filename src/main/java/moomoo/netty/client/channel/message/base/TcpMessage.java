package moomoo.netty.client.channel.message.base;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TcpMessage {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public TcpMessage(String msgType, String transactionId) {
        this.msgType = msgType;
        this.transactionId = transactionId;
    }

    @NotBlank
    private final String msgType;

    @NotBlank
    private final String transactionId;

    public String toJson() {
        return GSON.toJson(this);
    }
}
