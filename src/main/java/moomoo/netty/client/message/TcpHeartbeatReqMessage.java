package moomoo.netty.client.message;

import moomoo.netty.client.message.exception.TcpMessageException;
import moomoo.netty.client.message.base.TcpMessageHeader;

import static moomoo.netty.client.message.base.TcpMessageType.*;

public class TcpHeartbeatReqMessage {

    private final TcpMessageHeader header;

    public TcpHeartbeatReqMessage(byte[] data) throws TcpMessageException {
        if (data.length == HEADER_SIZE) {
            byte[] headerByteData = new byte[HEADER_SIZE];
            System.arraycopy(data, 0, headerByteData, 0, headerByteData.length);
            this.header = new TcpMessageHeader(headerByteData);
        } else {
            this.header = null;
            throw new TcpMessageException("[TCP HB REQ] Fail to create the Body. Data length: (" + data.length + ")");
        }
    }

    public TcpHeartbeatReqMessage(int systemId) {
        this.header = new TcpMessageHeader(HEADER_SIZE, HEARTBEAT_REQ_ID, systemId);
    }

    public byte[] getData() {
        byte[] data = new byte[header.getLength()];

        byte[] headerByteData = header.getData();
        System.arraycopy(headerByteData, 0, data, 0, headerByteData.length);

        return data;
    }

    @Override
    public String toString() {
        return "TcpHeartbeatReqMessage{" +
                "header=" + header +
                '}';
    }
}
