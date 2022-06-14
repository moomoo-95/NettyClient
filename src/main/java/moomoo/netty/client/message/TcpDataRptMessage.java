package moomoo.netty.client.message;

import moomoo.netty.client.message.base.TcpDataRptBody;
import moomoo.netty.client.message.base.TcpMessageHeader;
import moomoo.netty.client.message.exception.TcpMessageException;
import moomoo.netty.client.util.ByteUtil;

import static moomoo.netty.client.message.base.TcpMessageType.*;

public class TcpDataRptMessage {

    private final TcpMessageHeader header;
    private final TcpDataRptBody body;

    public TcpDataRptMessage(byte[] data) throws TcpMessageException {
        if (data.length >= HEADER_SIZE + DATA_RPT_BODY_MIN_SIZE) {
            int index = 0;

            byte[] headerByteData = new byte[HEADER_SIZE];
            System.arraycopy(data, index, headerByteData, 0, headerByteData.length);
            this.header = new TcpMessageHeader(headerByteData);
            index += headerByteData.length;

            byte[] bodyByteData = new byte[data.length - HEADER_SIZE];
            System.arraycopy(data, index, bodyByteData, 0, bodyByteData.length);
            this.body = new TcpDataRptBody(bodyByteData);
        } else {
            this.header = null;
            this.body = null;
            throw new TcpMessageException("[TCP DATA RPT] Fail to create the Body. Data length: (" + data.length + ")");
        }
    }

    public TcpDataRptMessage(int systemId, long aiifPort, long aiifLen, long aiifSeq, byte[] pcm) {
        this.body = new TcpDataRptBody(aiifPort, aiifLen, aiifSeq, pcm);
        this.header = new TcpMessageHeader(HEADER_SIZE + ByteUtil.NUM_BYTES_IN_INT + body.getAiifLen(), DATA_RPT_ID, systemId);
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

    public TcpDataRptBody getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "TcpLoginReqMessage{" +
                "header=" + header +
                ", body=" + body +
                '}';
    }
}