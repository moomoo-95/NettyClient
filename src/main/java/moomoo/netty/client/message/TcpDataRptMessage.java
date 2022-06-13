package moomoo.netty.client.message;

import moomoo.netty.client.message.base.TcpDataRptBody;
import moomoo.netty.client.message.base.TcpMessageHeader;
import moomoo.netty.client.message.exception.TcpMessageException;

import static moomoo.netty.client.message.base.TcpMessageType.*;

public class TcpDataRptMessage {

    private final TcpMessageHeader header;
    private final TcpDataRptBody body;

    public TcpDataRptMessage(byte[] data) throws TcpMessageException {
        if (data.length == HEADER_SIZE + DATA_RPT_BODY_SIZE) {
            int index = 0;

            byte[] headerByteData = new byte[HEADER_SIZE];
            System.arraycopy(data, index, headerByteData, 0, headerByteData.length);
            this.header = new TcpMessageHeader(headerByteData);
            index += headerByteData.length;

            byte[] bodyByteData = new byte[DATA_RPT_BODY_SIZE];
            System.arraycopy(data, index, bodyByteData, 0, bodyByteData.length);
            this.body = new TcpDataRptBody(bodyByteData);
        } else {
            this.header = null;
            this.body = null;
            throw new TcpMessageException("[TCP DATA RPT] Fail to create the Body. Data length: (" + data.length + ")");
        }
    }

    public TcpDataRptMessage(int systemId, long aiifPort, long aiifLen, long aiifSeq, short pcm) {
        this.header = new TcpMessageHeader(HEADER_SIZE + DATA_RPT_BODY_SIZE, DATA_RPT_ID, systemId);
        this.body = new TcpDataRptBody(aiifPort, aiifLen, aiifSeq, pcm);
    }

    public byte[] getData() {
        byte[] data = new byte[header.getLength()];
        int index = 0;

        byte[] headerByteData = header.getData();
        System.arraycopy(headerByteData, 0, data, index, headerByteData.length);
        index += headerByteData.length;

        byte[] bodyByteData = body.getData();
        System.arraycopy(bodyByteData, 0, data, index, bodyByteData.length);

        return data;
    }

    @Override
    public String toString() {
        return "TcpLoginReqMessage{" +
                "header=" + header +
                ", body=" + body +
                '}';
    }
}
